package edu.berkeley.nwbqueryengineweb.data.utils;

import java.io.File;
import java.io.FileFilter;

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
 * NwbFileFilter, 2017/09/29 10:00 petr-jezek
 *
 **********************************************************************************************************************/
public class NwbFileFilter implements FileFilter {

    @Override
    public boolean accept(File pathname) {
        return pathname.getName().toLowerCase().endsWith(".nwb");
    }
}
