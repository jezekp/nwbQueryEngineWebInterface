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

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.markup.html.bootstrap.list.BootstrapListView;
import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;
import edu.berkeley.nwbqueryengineweb.data.pojo.NwbData;
import edu.berkeley.nwbqueryengineweb.services.GenericService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;

import java.util.LinkedList;


public class HomePage extends BasePage {
    private static final long serialVersionUID = 1L;

    Log logger = LogFactory.getLog(getClass());

    @SpringBean
    GenericService<NwbData> dataService;

    public HomePage(final PageParameters parameters) {
        super(parameters);


        final BootstrapForm form = new BootstrapForm("form");
        final PageableListView<NwbData> listview = new PageableListView<NwbData>("listview", new LinkedList<NwbData>(), 50) {
            protected void populateItem(ListItem<NwbData> item) {
                final NwbData nwbData = item.getModelObject();

                item.add(new Label("fileName", nwbData.getFile().getName()));
                item.add(new Label("dataset", nwbData.getDataSet()));
                item.add(new Label("value", nwbData.getValue().toString()));
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


        final TextField<String> searchField = new TextField<String>("searchField", Model.of(""));


        final WebMarkupContainer tableDiv = new WebMarkupContainer("tableDiv");
        tableDiv.setVisible(false);

        add(tableDiv);
        add(form);
        add(listview);
        final BootstrapPagingNavigator pagingNavigator = new BootstrapPagingNavigator("navigator", listview);
        pagingNavigator.setVisible(false);
        add(pagingNavigator);
        add(new Label("countOfFiles", dataService.countOfFiles() + " " + searchField.getValue()));


        BootstrapButton send = new BootstrapButton("send", Buttons.Type.Primary) {

            @Override
            public void onSubmit() {
                listview.setModel(Model.ofList(dataService.loadData(searchField.getValue())));
                boolean visible = searchField.getValue().length() > 0;
                tableDiv.setVisible(visible);
                pagingNavigator.setVisible(visible);
            }
        };
        form.add(send);
        form.add(searchField);

    }

}
