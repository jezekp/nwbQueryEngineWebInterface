package edu.berkeley.nwbqueryengineweb.data.dao;

import java.io.File;

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
 * GenericDao, 2017/09/18 16:29 petr-jezek
 *
 **********************************************************************************************************************/
public interface GenericDao<T, Z> {

    List<T> getData(String query, Z file) throws Exception;
    File[] getFiles();
    String getRootDir();
}
