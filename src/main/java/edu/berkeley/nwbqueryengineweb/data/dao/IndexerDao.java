package edu.berkeley.nwbqueryengineweb.data.dao;

import edu.berkeley.nwbqueryengineweb.data.pojo.NwbData;
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

    @Override
    public List<NwbData> getData(String query, File file) {
        List<NwbData> data = new LinkedList<>();

        try {
            Process p = Runtime.getRuntime().exec(new String[]{python, indexerScript, indexDb, query});
      //      p.waitFor();

//            InputStream error = p.getErrorStream();
//            List<String> errorLines = IOUtils.readLines(error, Charset.defaultCharset());
//
//            errorLines.forEach(i -> logger.error(i));

            InputStream result =  p.getInputStream();
            List<String> lines = IOUtils.readLines(result, Charset.defaultCharset());
            for(String line : lines) {
                if(line.startsWith("(") && line.endsWith(")")) {
                    String[] split = StringUtils.strip(line, "(|)").split(",");
                    split = StringUtils.stripAll(split, " ");
                    split = StringUtils.stripAll(split, "'");
                    if (split.length > 3) {
                        NwbData nwbData = new NwbData();
                        nwbData.setFile(new File(split[0]));
                        nwbData.setDataSet(split[2]);
                        nwbData.setValue(split[3]);
                        data.add(nwbData);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }



        return data;

    }

    @Override
    public File[] getFiles() {
        return new File[] {new File(indexDb)};
    }

    @Override
    public String getRootDir() {
        return null;
    }
}
