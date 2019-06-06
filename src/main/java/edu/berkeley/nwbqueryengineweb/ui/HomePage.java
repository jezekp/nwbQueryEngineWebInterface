/*
 * Copyright Copyright Petr Jezek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.berkeley.nwbqueryengineweb.ui;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.components.progress.ProgressBar;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapRadioChoice;
import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;
import edu.berkeley.nwbqueryengineweb.data.pojo.NwbData;
import edu.berkeley.nwbqueryengineweb.services.GenericService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.time.Duration;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class HomePage extends BasePage {
    private static final long serialVersionUID = 1L;

    private final static String QUERY_ENGINE = "NWB Query Engine";
    private final static String INDEXER = "NWB Indexer";

    private static final List<String> CHOICES = Arrays
            .asList(new String[]{QUERY_ENGINE, INDEXER});

    private String selected = QUERY_ENGINE;

    Log logger = LogFactory.getLog(getClass());
    private int counter;

    @SpringBean(name = "nwbService")
    GenericService<NwbData> nwbService;

    @SpringBean(name = "indexerService")
    GenericService<NwbData> indexerService;


    public HomePage(final PageParameters parameters) {
        super(parameters);


        logger.debug("Home page created");

        final TextField<String> searchField = new TextField<String>("searchField", Model.of(""));

        final List<NwbData> data = new LinkedList<NwbData>();

        final WebMarkupContainer dataDiv = new WebMarkupContainer("dataDiv");
        dataDiv.setOutputMarkupPlaceholderTag(true);

        final WebMarkupContainer progressDiv = new WebMarkupContainer("progressDiv");
        progressDiv.setOutputMarkupPlaceholderTag(true);
        dataDiv.add(progressDiv);
        final Label percCompleted = new Label("percCompleted", Model.of(0));
        percCompleted.setOutputMarkupPlaceholderTag(true);
        progressDiv.add(percCompleted);

        final ProgressBar progressBar = new ProgressBar("progressBar", Model.of(0));
        progressBar.setOutputMarkupPlaceholderTag(true);
        progressBar.striped(true);
        progressBar.active(true);
        progressDiv.add(progressBar);

        final IModel<List<NwbData>> dataModel = new LoadableDetachableModel() {
            File[] files = null;

            protected List<NwbData> load() {
                //logger.debug("I am called: " + data.size() + ", i: " + counter);
                //read continuously all files with data - each calling of this method reads one file
                boolean isQuery = !searchField.getValue().isEmpty();

                if (counter == 0) {
                    files = getFiles();
                }
                if (counter < files.length && isQuery) {
                    try {
                        data.addAll(getService().loadData(searchField.getValue(), files[increaseCounter()]));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    int progressValue = Math.round(counter / (float) files.length * 100);
                    logger.debug("Progress bar:" + progressValue);
                    progressBar.value(progressValue);
                    percCompleted.setDefaultModel(Model.of(progressValue));
                    if (progressBar.complete()) {
                        progressDiv.setVisible(false);
                    }
                }

                return data;
            }
        };


        final BootstrapForm form = new BootstrapForm("form");
        final PageableListView<NwbData> listview = new PageableListView<NwbData>("listview", dataModel, 50) {
            protected void populateItem(ListItem<NwbData> item) {
                final NwbData nwbData = item.getModelObject();

                item.add(new Label("fileName", nwbData.getFile().getName()));
                item.add(new Label("dataset", nwbData.getDataSet()));
                Object value = nwbData.getValue();
                Object printValue = value;
                if(value.getClass().isArray()) {
                    printValue = Arrays.toString((Object[]) value);
                }
                item.add(new Label("value", printValue.toString()));
                item.add(new BootstrapLink<String>("file", Model.of(nwbData.getFile().getAbsolutePath()), Buttons.Type.Link) {
                    @Override
                    public void onClick() {
                        final File file = new File(nwbData.getFile());
                        logger.debug("Downloading file: " + nwbData.getFile());
                        IResourceStream resourceStream = new FileResourceStream(
                                new org.apache.wicket.util.file.File(file));
                        getRequestCycle().scheduleRequestHandlerAfterCurrent(new ResourceStreamRequestHandler(resourceStream, file.getName()));

                    }
                });
            }

        };

        add(form);

        listview.setOutputMarkupPlaceholderTag(true);
        final WebMarkupContainer tableDiv = new WebMarkupContainer("tableDiv");

        tableDiv.setOutputMarkupPlaceholderTag(true);
        dataDiv.add(tableDiv);

        tableDiv.add(listview);
        final BootstrapPagingNavigator pagingNavigator = new BootstrapPagingNavigator("navigator", listview);
        pagingNavigator.setOutputMarkupPlaceholderTag(true);
        dataDiv.add(pagingNavigator);

        add(new Label("countOfFiles", getFiles().length + " " + searchField.getValue()));

        dataDiv.setVisible(false);

        final Behavior refresh1 = new AjaxSelfUpdatingTimerBehavior(Duration.milliseconds(1));


        dataDiv.add(refresh1);

        BootstrapAjaxButton send = new BootstrapAjaxButton("send", Buttons.Type.Primary) {

            @Override
            public void onSubmit(AjaxRequestTarget target) {
                boolean isQuery = !searchField.getValue().isEmpty();
                logger.debug("visible" + isQuery);
                dataDiv.setVisible(isQuery);
                progressDiv.setVisible(true);
                target.add(dataDiv);
                data.clear();
                progressBar.value(0);
                percCompleted.setDefaultModel(Model.of(0));
                clearCounter();
            }

        };

        BootstrapRadioChoice<String> radioChoice = new BootstrapRadioChoice<>("engine", new PropertyModel<String>(this, "selected"), CHOICES);

        form.add(send);
        form.add(searchField);
        form.add(radioChoice);
        add(dataDiv);

    }

    private File[] getFiles() {
        java.io.File[] files = getService().getFiles();
        File[] res = new File[files.length];

        for (int i = 0; i < files.length; i++) {
            res[i] = new File(files[i]);
        }
        return res;
    }

    private int increaseCounter() {
        return counter++;
    }

    private void clearCounter() {
        counter = 0;
    }

    private GenericService<NwbData> getService() {
        if (selected.equals(QUERY_ENGINE)) {
            logger.debug("QueryEngine");
            return nwbService;
        } else {
            logger.debug("Indexer");
            return indexerService;
        }

    }

}
