package edu.berkeley.nwbqueryengineweb.data.utils;

import edu.berkeley.nwbqueryengineweb.data.pojo.NwbData;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
 * PythonData, 2019/06/07 09:10 petr-jezek
 *
 **********************************************************************************************************************/
public class PythonData {

    Log logger = LogFactory.getLog(getClass());


    public List<NwbData> getData(String[] params, String query) throws Exception {


        List<NwbData> data;

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
}
