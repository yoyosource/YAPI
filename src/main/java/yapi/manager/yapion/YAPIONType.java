// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.yapion;

import yapi.internal.exceptions.objectnotation.YAPIONException;

public class YAPIONType {

    public String getType() {
        return "YAPIONType";
    }

    private YAPIONType parent = null;
    private boolean wasSet = false;

    public void setParent(YAPIONType parent) {
        if (wasSet) {
            return;
        }
        this.parent = parent;
        wasSet = true;
    }

    public YAPIONType getParent() {
        return parent;
    }

}