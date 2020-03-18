// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.yapion.internal.parser;

public class YAPIONTypeStack {

    private static int compact = 0;

    public static void setCompact(int compact) {
        YAPIONTypeStack.compact = compact;
    }

    private int pointer = -1;
    private YAPIONCurrentType[] types = new YAPIONCurrentType[65536];
    private int[] indices = new int[65536];

    public void push(int i, int index) {
        push(YAPIONCurrentType.valueOf(i), index);
    }

    public void push(YAPIONCurrentType yapionCurrentType, int index) {
        pointer++;
        types[pointer] = yapionCurrentType;
        indices[pointer] = index;
    }

    public YAPIONCurrentType pop() {
        if (empty()) {
            throw new IndexOutOfBoundsException();
        }
        YAPIONCurrentType yapionCurrentTypes = types[pointer];
        types[pointer] = null;
        indices[pointer] = 0;
        pointer--;
        return yapionCurrentTypes;
    }

    public int popIndex() {
        if (empty()) {
            throw new IndexOutOfBoundsException();
        }
        int index = indices[pointer];
        types[pointer] = null;
        indices[pointer] = 0;
        pointer--;
        return index;
    }

    public YAPIONCurrentType peek() {
        if (empty()) {
            throw new IndexOutOfBoundsException();
        }
        return types[pointer];
    }

    public int peekIndex() {
        if (empty()) {
            throw new IndexOutOfBoundsException();
        }
        return indices[pointer];
    }

    public YAPIONCurrentType get(int index) {
        if (index > pointer || index < 0 || empty()) {
            throw new IndexOutOfBoundsException();
        }
        return types[index];
    }

    public int getIndex(int index) {
        if (index > pointer || index < 0 || empty()) {
            throw new IndexOutOfBoundsException();
        }
        return indices[index];
    }

    public boolean empty() {
        return pointer == -1;
    }

    public boolean isEmpty() {
        return empty();
    }

    public boolean isNotEmpty() {
        return !empty();
    }

    public boolean hasValue() {
        return isNotEmpty();
    }

    @Override
    public String toString() {
        if (compact == 2) {
            return toStringCompact().toString();
        }
        StringBuilder st = new StringBuilder();
        st.append("[");
        for (int i = 0; i <= pointer; i++) {
            if (i != 0) {
                st.append(", ");
            }
            st.append(get(i)).append("(").append(indices[i]).append(")");
        }
        st.append("]");
        if (compact == 1) {
            return st.toString();
        }
        return "YAPIONTypeStack{" +
                "types=" + st.toString() +
                '}';
    }

    private StringBuilder toStringCompact() {
        StringBuilder st = new StringBuilder();
        for (int i = 0; i <= pointer; i++) {
            int index = indices[i];
            String indexString = " ".repeat(6 - (index + "").length()) + index;
            if (i > 0 && get(i - 1).equals(get(i))) {
                st.append(" -> ").append(indexString);
            } else {
                if (i > 0) {
                    st.append(") ");
                }
                st.append(get(i).toString().charAt(0)).append("(").append(indexString);
            }
        }
        if (pointer >= 0) {
            st.append(")");
        }
        return st;
    }
}