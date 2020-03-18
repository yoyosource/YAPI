// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.yapion.value;

import yapi.manager.yapion.YAPIONType;
import yapi.manager.yapion.YAPIONVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class YAPIONMap extends YAPIONType {

    private Map<String, YAPIONType> variables = new HashMap<>();

    public List<String> getKeys() {
        return variables.keySet().stream().collect(Collectors.toList());
    }

    public YAPIONVariable getVariable(String key) {
        return new YAPIONVariable(key, variables.get(key));
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

    public void add(String key, YAPIONType value) {
        variables.put(key, value);
    }

    @Override
    public String getType() {
        return "YAPIONObject";
    }

    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();
        st.append("{");
        for (String s : getKeys()) {
            st.append(getVariable(s).toString());
        }
        return st.append("}").toString();
    }

    public String toHierarchyString() {
        return toHierarchyString(0);
    }

    public String toHierarchyString(int index) {
        StringBuilder st = new StringBuilder();
        st.append("{\n");
        for (String s : getKeys()) {
            st.append(getVariable(s).toHierarchyString(index == 0 ? index + 1 : index)).append('\n');
        }
        if (index > 1) {
            st.append(" ".repeat(2 * (index - 1)));
        }
        st.append("}");
        return st.toString();
    }

}