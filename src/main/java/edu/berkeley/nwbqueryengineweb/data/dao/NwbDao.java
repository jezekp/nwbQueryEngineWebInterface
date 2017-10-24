package edu.berkeley.nwbqueryengineweb.data.dao;

import edu.berkeley.nwbqueryengine.api.FileInput;
import edu.berkeley.nwbqueryengine.data.NwbResult;
import edu.berkeley.nwbqueryengineweb.data.pojo.NwbData;
import edu.berkeley.nwbqueryengineweb.data.utils.NwbFileFilter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileFilter;
import java.util.Iterator;
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

    Log logger = LogFactory.getLog(getClass());


    @Override
    public List<NwbData> getData(String query, File file) {

        List<NwbData> result = new LinkedList<NwbData>();


        try {
            List<NwbResult> tmp = nwbQueryEngine.executeQuery(file.getAbsolutePath(), query);
            for (NwbResult nwbResult : tmp) {
                NwbData res = new NwbData();
                res.setDataSet(nwbResult.getDataSet());
                res.setValue(nwbResult.getValue());
                res.setFile(file);
                result.add(res);
            }

        } catch (Exception e) {
            logger.error(e);
            throw new RuntimeException(e);
        }


        return result;
    }

    @Override
    public File[] getFiles() {
        List<File> res = new LinkedList<>();
        Iterator<File> files = FileUtils.iterateFiles(new File(getRootDir()), new NwbFileFilter(), TrueFileFilter.INSTANCE);
        while (files.hasNext()) {
            res.add(files.next());
        }
        return res.toArray(new File[]{});
    }

    @Override
    public String getRootDir() {
        logger.debug("fileFolder: " + fileFolder);
        return fileFolder;
    }


}
