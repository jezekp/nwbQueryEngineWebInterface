package edu.berkeley.nwbqueryengineweb.data.utils;

import edu.berkeley.nwbqueryengineweb.data.pojo.NwbData;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.util.*;

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
 * JsonParser, 2019/06/06 15:13 petr-jezek
 *
 **********************************************************************************************************************/
public class JsonParser {

    private List<String> data;

    public JsonParser(List<String> data) {
        this.data = data;
        if (data == null) {
            throw new NullPointerException("Data must be set");
        }
    }

    private List<String> prepareJson(List<String> data) {
        List<String> res = new LinkedList<>();
        for (String item : data) {
            if (!item.startsWith("Opening") && !item.startsWith("Found")) {
                res.add(item);
            }
        }
        return res;
    }

    public List<NwbData> parse() throws ParseException {
        List<NwbData> result = new LinkedList<>();

        String stringData = StringUtils.join(prepareJson(data));
        stringData = stringData.replaceAll("'", "\"");
        stringData = stringData.replaceAll("\\(|\\)", "");
        Object obj = new JSONParser().parse(stringData);

        JSONArray jo = (JSONArray) obj;
        if (jo != null) {
            for (Object o : jo) {
                if (o instanceof JSONArray) {
                    JSONArray results = ((JSONArray) o);
                    if (results != null) {
                        for (Object r : results) {
                            JSONObject resultObject = (JSONObject) r;
                            Object file = resultObject.get("file");
                            Object subQueryObject = resultObject.get("subqueries");
                            if (subQueryObject instanceof JSONArray) {
                                JSONArray subQueryArray = (JSONArray) subQueryObject;
                                if (subQueryArray != null) {
                                    for (Object subQuery : subQueryArray) {
                                        JSONArray item = (JSONArray) subQuery;
                                        if (item != null) {
                                            for (Object innerItem : item) {
                                                JSONObject innerItemObject = (JSONObject) innerItem;
                                                Object node = innerItemObject.get("node");
                                                Object vind = innerItemObject.get("vind");
                                                Map<String, Object> vindMap = processNode(vind);
                                                result.addAll(mapToNwbData(vindMap, (String) node, (String) file));
                                                Object vtbl = innerItemObject.get("vtbl");
                                                JSONObject vtblObject = (JSONObject) vtbl;
                                                Object combined = vtblObject.get("combined");
                                                JSONArray combinedArray = (JSONArray) combined;
                                                if (combinedArray != null) {
                                                    for (Object combinedObject : combinedArray) {
                                                        Map<String, Object> vtblMap = processNode(combinedObject);
                                                        result.addAll(mapToNwbData(vtblMap, (String) node, (String) file));
                                                    }
                                                }

                                            }
                                        }

                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
        return result;
    }

    private Map<String, Object> processNode(Object node) {
        Map<String, Object> res = new LinkedHashMap<>();
        if (node instanceof JSONObject) {
            JSONObject nodeObject = (JSONObject) node;
            Set<String> keys = nodeObject.keySet();
            for (String key : keys) {
                Object value = nodeObject.get(key);
                if (value instanceof JSONArray) {
                    JSONArray valueArray = (JSONArray) value;
                    List<Object> values = new LinkedList<>();
                    for (Object itemArray : valueArray) {
                        values.add(itemArray);
                    }
                    res.put(key, values.toArray());
                } else {
                    res.put(key, value);
                }
            }
        }
        return res;
    }

    private List<NwbData> mapToNwbData(Map<String, Object> map, String node, String file) {
        List<NwbData> res = new LinkedList<>();

        String dataset = node + "/";
        String value = "";
        for (String key : map.keySet()) {
            dataset += key + ", ";
            Object v = map.get(key);
            if(v.getClass().isArray()) {
                Object[] varray = (Object[]) v;
                String array = Arrays.toString(varray);
                value += array + ", ";
            } else {
                value += v + ", ";
            }


        }
        NwbData data = new NwbData();
        data.setFile(new File(file));
        data.setDataSet(removeTrailingComma(dataset));
        data.setValue(removeTrailingComma(value));
        res.add(data);
        return res;
    }

    private String removeTrailingComma(String s) {
        return s == null ? null : s.replaceAll(", $", "");
    }

}
