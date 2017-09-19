package edu.berkeley.nwbqueryengineweb.data.dao;

import edu.berkeley.nwbqueryengine.api.FileInput;
import edu.berkeley.nwbqueryengineweb.data.pojo.NwbData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
 * NwbDao, 2017/09/18 16:30 petr-jezek
 *
 **********************************************************************************************************************/
@Repository
public class NwbDao implements GenericDao<NwbData> {


    FileInput nwbQueryEngine = new FileInput();

    @Override
    public List<NwbData> getData(String query) {
        try {
            nwbQueryEngine.executeQuery(null, query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new LinkedList<NwbData>();
    }
}
