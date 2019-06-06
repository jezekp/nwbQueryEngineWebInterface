package edu.berkeley.nwbqueryengineweb.data.dao;

import edu.berkeley.nwbqueryengineweb.data.pojo.NwbData;
import edu.berkeley.nwbqueryengineweb.data.utils.JsonParser;
import edu.berkeley.nwbqueryengineweb.data.utils.PythonProcess;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

/***********************************************************************************************************************
 *
 * This file is part of the nwbQueryEngineWebInterface project

 * ==========================================
 *
 * Copyright (C) 2019 by Petr Jezek
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
 * IndexerDao, 2019/03/24 17:06 petr-jezek
 *
 **********************************************************************************************************************/
@Repository
public class IndexerDao implements GenericDao<NwbData, File> {

    Log logger = LogFactory.getLog(getClass());

    @Value("${python.location}")
    private String python;

    @Value("${indexer.script}")
    private String indexerScript;

    @Value("${index.db}")
    private String indexDb;

    @Value("${files.folder}")
    private String fileFolder;

    @Override
    public List<NwbData> getData(String query, File file) throws Exception {
        logger.debug("I'm called: " + file);
        List<NwbData> data = new LinkedList<>();


            String[] params = new String[]{python, indexerScript, indexDb};

            PythonProcess pythonProcess = new PythonProcess(params, query);

            List<String> lines = pythonProcess.execute();

            if(pythonProcess.isOK()) {

                JsonParser parser = new JsonParser(lines);
                data = parser.parse();
            } else {
                int timeout = 0;
                while (!pythonProcess.isErrorWritten() && timeout++ < 100) {
                    synchronized (this) {
                        wait(100);
                    }
                }
                String s = StringUtils.join(pythonProcess.getErrorOutput());
                Exception e = new Exception(s);
                throw e;

            }


        return data;

    }

    @Override
    public File[] getFiles() {
        return new File[] {new File(indexDb)};
    }

    @Override
    public String getRootDir() {
        return fileFolder;
    }
}
