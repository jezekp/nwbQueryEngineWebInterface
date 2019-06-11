package edu.berkeley.nwbqueryengineweb.data.utils;

import edu.berkeley.nwbqueryengineweb.data.pojo.NwbData;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.LinkedList;
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
 * PythonProcess, 2019/06/06 13:03 petr-jezek
 *
 **********************************************************************************************************************/
public class PythonProcess {

    private String[] params;
    private String query;
    private boolean isOK;
    private List<String> errorOutput;
    Log logger = LogFactory.getLog(getClass());
    private boolean executed = false;
    private boolean errorWritten = false;


    public PythonProcess(String[] params, String query) {
        this.params = params;
        this.query = query;
    }

    public List<String> execute() throws Exception {
        if(executed) {
            throw new Exception("Already executed;");
        }
        errorOutput = new LinkedList<>();
        String[] copy = new String[0];
        copy = ArrayUtils.addAll(copy, params);
        copy = ArrayUtils.add(copy, query);

        Process p = Runtime.getRuntime().exec(copy);

        InputStream errorStream = p.getErrorStream();
        new Thread() {

            @Override
            public void run() {
                try {
                    final List<String> s = getLines(errorStream);
                    if(s != null) {
                        s.forEach(i -> errorOutput.add(i));
                    }
                    setErrorWritten(true);
                } catch (IOException e) {
                    logger.error(e);
                    throw new RuntimeException(e);
                }
            }
        }.start();


        InputStream result = p.getInputStream();
        List<String> res = getLines(result);
        isOK = res.size() > 0;
        executed = true;
        return res;

    }

    private List<String> getLines(InputStream s) throws IOException {
        return IOUtils.readLines(s, Charset.defaultCharset());
    }

    public boolean isOK() {
        return isOK;
    }

    public List<String> getErrorOutput() {
        return errorOutput;
    }

    public static void main(String[] args) {
        String location = "/usr/bin/python3.6";
        String script = "/home/petr-jezek/python/nwbindexer/search_nwb.py";
        String db = "/tmp/basic_example.nwb";
        String[] params = new String[]{location, script, db};
        PythonProcess p = new PythonProcess(params, "units: id, location, spike_times, quality > 0.80");
        try {
            List<String> lines = p.execute();
            //lines.forEach(i -> System.out.println(i));
            JsonParser parser = new JsonParser(lines);
            List<NwbData> res = parser.parse();
            res.forEach(i -> System.out.println(i));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean isErrorWritten() {
        return errorWritten;
    }

    public void setErrorWritten(boolean errorWritten) {
        this.errorWritten = errorWritten;
    }
}
