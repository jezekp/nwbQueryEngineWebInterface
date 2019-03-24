package edu.berkeley.nwbqueryengineweb.services;

import edu.berkeley.nwbqueryengineweb.data.dao.GenericDao;
import edu.berkeley.nwbqueryengineweb.data.pojo.NwbData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
 * IndexerService, 2019/03/24 18:05 petr-jezek
 *
 **********************************************************************************************************************/
@Service
public class IndexerService implements GenericService<NwbData> {

    @Autowired
    GenericDao<NwbData, File> indexerDao;

    @Override
    public List<NwbData> loadData(String query, File file) {
        return indexerDao.getData(query, file);
    }

    @Override
    public File[] getFiles() {
        return indexerDao.getFiles();
    }

    @Override
    public String getRootDir() {
        return indexerDao.getRootDir();
    }
}
