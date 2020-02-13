// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.yapion.value;

import yapi.manager.yapion.YAPIONType;

import java.util.ArrayList;
import java.util.List;

public class YAPIONArray extends YAPIONType {

    private List<YAPIONType> array = new ArrayList<>();

    public int size() {
        return array.size();
    }

    public YAPIONType get(int index) {
        return array.get(index);
    }

    public void add(YAPIONType yapionType) {
        array.add(yapionType);
    }

    @Override
    public String getType() {
        return "YAPIONArray";
    }

    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();
        st.append("[");
        boolean b = false;
        for (YAPIONType yapionType : array) {
            if (b) {
                st.append(",");
            }
            if (yapionType.getType().equals("YAPIONValue")) {
                st.append(yapionType.toString(), 1, yapionType.toString().length() - 1);
            } else {
                st.append(yapionType.toString());
            }
            b = true;
        }
        st.append("]");
        return st.toString();
    }

    public String toHierarchyString(int index) {
        StringBuilder st = new StringBuilder();
        st.append("[\n");
        boolean b = false;
        for (YAPIONType yapionType : array) {
            if (b) {
                st.append(",\n");
            }
            st.append(" ".repeat(2 * index));
            if (yapionType.getType().equals("YAPIONValue")) {
                st.append(yapionType.toString(), 1, yapionType.toString().length() - 1);
            } else if (yapionType instanceof YAPIONArray) {
                st.append(((YAPIONArray) yapionType).toHierarchyString(index + 1));
            } else if (yapionType instanceof YAPIONObject) {
                st.append(((YAPIONObject) yapionType).toHierarchyString(index + 1));
            }
            b = true;
        }
        st.append("\n").append(" ".repeat(2 * (index - 1))).append("]");
        return st.toString();
    }

}