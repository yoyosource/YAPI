// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.yapion;

import yapi.manager.yapion.value.YAPIONArray;
import yapi.manager.yapion.value.YAPIONObject;

public class YAPIONVariable {

    private String name = "";
    private YAPIONType yapionType = null;

    private String pattern = "([({\\[)}\\]\\\\ ])";
    private String replacement = "\\\\$1";

    public YAPIONVariable(String name, YAPIONType yapionType) {
        this.name = name;
        this.yapionType = yapionType;
    }

    public String getName() {
        return name;
    }

    public YAPIONType getYapionType() {
        return yapionType;
    }

    @Override
    public String toString() {
        return name.replaceAll(pattern, replacement) + yapionType.toString();
    }

    public String toHierarchyString(int index) {
        if (yapionType instanceof YAPIONArray) {
            return " ".repeat(2 * index) + name.replaceAll(pattern, replacement) + ((YAPIONArray) yapionType).toHierarchyString(index + 1);
        } else if (yapionType instanceof YAPIONObject) {
            return " ".repeat(2 * index) + name.replaceAll(pattern, replacement) + ((YAPIONObject) yapionType).toHierarchyString(index + 1);
        } else {
            return " ".repeat(2 * index) + name.replaceAll(pattern, replacement) + yapionType.toString();
        }
    }

}