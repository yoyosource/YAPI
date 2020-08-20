// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.json.value;

import yapi.manager.json.JSONType;
import yapi.manager.json.JSONVariable;

import java.util.ArrayList;
import java.util.List;

public class JSONObject extends JSONType {

    private final List<JSONVariable> jsonVariables = new ArrayList<>();

    public List<String> getKeys() {
        List<String> keys = new ArrayList<>();
        for (JSONVariable jsonVariable : jsonVariables) {
            keys.add(jsonVariable.getName());
        }
        return keys;
    }

    public JSONVariable getVariable(String key) {
        for (JSONVariable jsonVariable : jsonVariables) {
            if (jsonVariable.getName().equals(key)) {
                return jsonVariable;
            }
        }
        return null;
    }

    public JSONObject getObject(String key) {
        JSONVariable jsonVariable = getVariable(key);
        if (jsonVariable == null) {
            return null;
        }
        JSONType jsonType = jsonVariable.getJsonType();
        if (jsonType instanceof JSONObject) {
            return (JSONObject) jsonType;
        }
        return null;
    }

    public JSONValue getValue(String key) {
        JSONVariable jsonVariable = getVariable(key);
        if (jsonVariable == null) {
            return null;
        }
        JSONType jsonType = jsonVariable.getJsonType();
        if (jsonType instanceof JSONValue) {
            return (JSONValue) jsonType;
        }
        return null;
    }

    public JSONArray getArray(String key) {
        JSONVariable jsonVariable = getVariable(key);
        if (jsonVariable == null) {
            return null;
        }
        JSONType jsonType = jsonVariable.getJsonType();
        if (jsonType instanceof JSONArray) {
            return (JSONArray) jsonType;
        }
        return null;
    }

    public void add(JSONVariable jsonVariable) {
        jsonVariables.add(jsonVariable);
    }

    @Override
    public String getType() {
        return "JSONObject";
    }

    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();
        st.append("{");
        int i = 0;
        for (JSONVariable jsonVariable : jsonVariables) {
            st.append(jsonVariable.toString());
            i++;
            if (i != jsonVariables.size()) {
                st.append(",");
            }
        }
        st.append("}");
        return st.toString();
    }

    /*public YAPIONObject toYAPION() {
        return JSONParser.toYAPION(this);
    }*/

}