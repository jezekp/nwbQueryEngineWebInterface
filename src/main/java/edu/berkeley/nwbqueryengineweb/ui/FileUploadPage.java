/*
 * Copyright 2014 dbeer.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.berkeley.nwbqueryengineweb.ui;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationMessage;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Alert;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.fileinput.BootstrapFileInput;

import java.util.List;

import edu.berkeley.nwbqueryengineweb.data.pojo.NwbData;
import edu.berkeley.nwbqueryengineweb.services.GenericService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;

import java.io.File;

/**
 * @author dbeer
 */
public class FileUploadPage extends BasePage {


    @SpringBean
    GenericService<NwbData> dataService;

    public FileUploadPage(final PageParameters params) {
        super(params);
        addFileUploafForm();
    }

    private void addFileUploafForm() {
        final IModel<List<FileUpload>> model = new ListModel<FileUpload>();
        final BootstrapForm<Void> form = new BootstrapForm<Void>("form");
        form.setOutputMarkupId(true);
        add(form);
        final NotificationPanel fp = new NotificationPanel("feedback");
        fp.setOutputMarkupId(true);
        fp.hideAfter(Duration.seconds(20));
        add(fp);

        BootstrapFileInput bootstrapFileInput = new BootstrapFileInput("bootstrapFileinput", model) {

            @Override
            protected void onError(AjaxRequestTarget target) {
                target.add(fp);
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                super.onSubmit(target);

                List<FileUpload> fileUploads = model.getObject();
                target.add(fp);
                if (fileUploads != null) {
                    for (FileUpload upload : fileUploads) {
                        if (upload != null) {

                            // write to a new file
                            File newFile = new File(dataService.getRootDir()
                                   + "/" + upload.getClientFileName());
                            try {

                                if(newFile.exists()) {
                                    throw new Exception("File already exists");
                                }

                                if(!upload.getClientFileName().toLowerCase().endsWith(".nwb")) {
                                    throw new Exception("File must be a nwb file");
                                }

                                newFile.createNewFile();
                                upload.writeTo(newFile);

                                success(new NotificationMessage(Model.of("Saved"), Model.of(upload.getClientFileName())));
                            } catch (Exception e) {
                                error(new NotificationMessage(Model.of(upload.getClientFileName()), Model.of(e.getLocalizedMessage())));
                            } finally {
                                upload.delete();
                            }

                        }
                    }
                }

            }
        };
        form.add(bootstrapFileInput);
    }


}
