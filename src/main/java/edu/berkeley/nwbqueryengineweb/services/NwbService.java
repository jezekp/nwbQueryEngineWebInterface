package edu.berkeley.nwbqueryengineweb.services;

import edu.berkeley.nwbqueryengineweb.data.dao.GenericDao;
import edu.berkeley.nwbqueryengineweb.data.pojo.NwbData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
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
 * NwbService, 2017/09/18 15:32 petr-jezek
 *
 **********************************************************************************************************************/
@Service
public class NwbService implements GenericService<NwbData> {


    @Autowired
    GenericDao<NwbData> nwbDao;

    @Override
    public List<NwbData> loadData(String query) {
        System.out.println("Loading data");
        List<NwbData> res;
        res = nwbDao.getData(query);
        return res;
    }

    @Override
    public int countOfFiles() {
        return nwbDao.countOfFiles();
    }

    @Override
    public String getRootDir() {
        return nwbDao.getRootDir();
    }
}