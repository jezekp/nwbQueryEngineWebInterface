package edu.berkeley.nwbqueryengineweb.data.dao;

import edu.berkeley.nwbqueryengineweb.data.pojo.NwbData;
import edu.berkeley.nwbqueryengineweb.data.utils.PythonData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.List;

/***********************************************************************************************************************
 *
 * This file is part of the nwb-query-engine-web-interface project

 * ==========================================
 *
 * Copyright (C) 2019 by University of West Bohemia (http://www.zcu.cz/en/)
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
 * SearchPythonDao, 2019/06/07 09:13 petr-jezek
 *
 **********************************************************************************************************************/
@Repository
public class SearchPythonDao implements GenericDao<NwbData, File> {

    Log logger = LogFactory.getLog(getClass());

    @Value("${python.location}")
    private String python;

    @Value("${search.python.script}")
    private String searchPythonScript;

    @Value("${index.db}")
    private String indexDb;

    @Value("${files.folder}")
    private String fileFolder;


    @Override
    public List getData(String query, File file) throws Exception {
        logger.debug("I'm called: " + file);
        String[] params = new String[]{python, searchPythonScript, file.getAbsolutePath()};
        PythonData pythonData = new PythonData();
        return pythonData.getData(params, query);
    }

    @Override
    public File[] getFiles() {
        return edu.berkeley.nwbqueryengineweb.data.utils.FileUtils.getFiles(getRootDir());
    }

    @Override
    public String getRootDir() {
        return fileFolder;
    }
}
