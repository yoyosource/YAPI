// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.yapion.value;

import yapi.manager.json.value.JSONObject;
import yapi.manager.yapion.YAPIONParser;
import yapi.manager.yapion.YAPIONType;
import yapi.manager.yapion.YAPIONVariable;

import java.util.ArrayList;
import java.util.List;

public class YAPIONObject extends YAPIONType {

    private List<YAPIONVariable> variables = new ArrayList<>();

    public List<String> getKeys() {
        List<String> keys = new ArrayList<>();
        for (YAPIONVariable yapionVariable : variables) {
            keys.add(yapionVariable.getName());
        }
        return keys;
    }

    public YAPIONVariable getVariable(String key) {
        for (YAPIONVariable yapionVariable : variables) {
            if (yapionVariable.getName().equals(key)) {
                return yapionVariable;
            }
        }
        return null;
    }

    public YAPIONObject getObject(String key) {
        YAPIONVariable yapionVariable = getVariable(key);
        if (yapionVariable == null) {
            return null;
        }
        YAPIONType yapionType = yapionVariable.getYapionType();
        if (yapionType instanceof YAPIONObject) {
            return (YAPIONObject) yapionType;
        }
        return null;
    }

    public YAPIONValue getValue(String key) {
        YAPIONVariable yapionVariable = getVariable(key);
        if (yapionVariable == null) {
            return null;
        }
        YAPIONType yapionType = yapionVariable.getYapionType();
        if (yapionType instanceof YAPIONValue) {
            return (YAPIONValue) yapionType;
        }
        return null;
    }

    public YAPIONArray getArray(String key) {
        YAPIONVariable yapionVariable = getVariable(key);
        if (yapionVariable == null) {
            return null;
        }
        YAPIONType yapionType = yapionVariable.getYapionType();
        if (yapionType instanceof YAPIONArray) {
            return (YAPIONArray) yapionType;
        }
        return null;
    }

    public void add(YAPIONVariable yapionVariable) {
        for (int i = variables.size() - 1; i >= 0; i--) {
            if (variables.get(i).getName().equals(yapionVariable.getName())) {
                variables.remove(i);
            }
        }
        variables.add(yapionVariable);
        yapionVariable.getYapionType().setParent(this);
    }

    @Override
    public String getType() {
        return "YAPIONObject";
    }

    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();
        st.append("{");
        for (YAPIONVariable yapionVariable : variables) {
            st.append(yapionVariable.toString());
        }
        return st.append("}").toString();
    }

    public JSONObject toJson() {
        return YAPIONParser.toJSON(this);
    }

    public String toHierarchyString() {
        return toHierarchyString(0);
    }

    public String toHierarchyString(int index) {
        StringBuilder st = new StringBuilder();
        st.append("{\n");
        for (YAPIONVariable yapionVariable : variables) {
            st.append(yapionVariable.toHierarchyString(index == 0 ? index + 1 : index)).append('\n');
        }
        if (index > 1) {
          st.append(" ".repeat(2 * (index - 1)));
        }
        st.append("}");
        return st.toString();
    }

}