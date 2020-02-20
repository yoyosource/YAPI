// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.yapion.internal;

public enum YAPIONCurrentType {

    OBJECT(0),
    ARRAY(1),
    VALUE(2),
    POINTER(3),

    UNDEFINED(4);

    private int type;

    YAPIONCurrentType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static YAPIONCurrentType valueOf(int type) {
        if (type == 0) {
            return OBJECT;
        }
        if (type == 1) {
            return ARRAY;
        }
        if (type == 2) {
            return VALUE;
        }
        if (type == 3) {
            return POINTER;
        }
        return UNDEFINED;
    }

}