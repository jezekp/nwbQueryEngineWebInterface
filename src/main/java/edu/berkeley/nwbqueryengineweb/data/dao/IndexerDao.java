package edu.berkeley.nwbqueryengineweb.data.dao;

import edu.berkeley.nwbqueryengineweb.data.pojo.NwbData;
import org.springframework.stereotype.Repository;

import java.io.File;
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

    @Override
    public List<NwbData> getData(String query, File file) {
        return null;
    }

    @Override
    public File[] getFiles() {
        return new File[0];
    }

    @Override
    public String getRootDir() {
        return null;
    }
}
