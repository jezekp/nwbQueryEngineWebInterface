package edu.berkeley.nwbqueryengineweb.data.dao;

import edu.berkeley.nwbqueryengine.api.FileInput;
import edu.berkeley.nwbqueryengine.data.NwbResult;
import edu.berkeley.nwbqueryengineweb.data.pojo.NwbData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/***********************************************************************************************************************
 *
 * This file is part of the nwbQueryEngineWebInterface project

 * ==========================================
 *
 * Copyright (C) 2017 by Petr Jezek
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 *
 * NwbDao, 2017/09/18 16:30 petr-jezek
 *
 **********************************************************************************************************************/
@Repository
public class NwbDao implements GenericDao<NwbData> {

    @Autowired
    private FileInput nwbQueryEngine;
    @Value("${files.folder}")
    private String fileFolder;


    @Override
    public List<NwbData> getData(String query) {

        List<NwbData> result = new LinkedList<NwbData>();

        File files = new File(fileFolder);
        if(files.isDirectory()) {
            File[] filesNames = files.listFiles();
            for(File item : filesNames) {
                try {
                    List<NwbResult> tmp = nwbQueryEngine.executeQuery(item.getAbsolutePath(), query);
                    for(NwbResult nwbResult : tmp) {
                        NwbData res = new NwbData();
                        res.setDataSet(nwbResult.getDataSet());
                        res.setValue(nwbResult.getValue());
                        res.setFile(item.getName());
                        result.add(res);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }


        return result;
    }
}
