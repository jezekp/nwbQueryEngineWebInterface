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

import com.sun.deploy.security.ruleset.DRSResult;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.components.progress.ProgressBar;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.markup.html.bootstrap.list.BootstrapListView;
import edu.berkeley.nwbqueryengineweb.data.pojo.NwbData;
import edu.berkeley.nwbqueryengineweb.services.GenericService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;



public class HomePage extends BasePage {
    private static final long serialVersionUID = 1L;

    @SpringBean
    GenericService<NwbData> dataService;

    public HomePage(final PageParameters parameters) {
        super(parameters);

        final BootstrapForm form = new BootstrapForm("form");
        final BootstrapListView<NwbData> listview = new BootstrapListView<NwbData>("listview", new LinkedList<NwbData>()) {
            protected void populateItem(ListItem<NwbData> item) {
                NwbData nwbData = item.getModelObject();
                item.add(new Label("dataset", nwbData.getDataSet()));
                item.add(new Label("value", nwbData.getValue().toString()));
                item.add(new Label("file", nwbData.getFile()));
            }
        };



        add(form);
        add(listview);



        final TextField<String> searchField = new TextField<String>("searchField", Model.of(""));
        form.add(searchField);

        BootstrapButton send = new BootstrapButton("send", Buttons.Type.Primary) {

            @Override
            public void onSubmit() {
                listview.setModel(Model.ofList(dataService.loadData(searchField.getValue())));
            }
        };
        form.add(send);

    }

}
