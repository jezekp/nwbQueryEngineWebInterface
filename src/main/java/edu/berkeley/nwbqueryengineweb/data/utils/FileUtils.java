package edu.berkeley.nwbqueryengineweb.data.utils;

import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.util.Iterator;
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
 * FileUtils, 2019/06/07 09:18 petr-jezek
 *
 **********************************************************************************************************************/
public class FileUtils {

    public static File[] getFiles(String root) {
        List<File> res = new LinkedList<>();
        Iterator<File> files = org.apache.commons.io.FileUtils.iterateFiles(new File(root), new NwbFileFilter(), TrueFileFilter.INSTANCE);
        while (files.hasNext()) {
            res.add(files.next());
        }
        return res.toArray(new File[]{});
    }
}
