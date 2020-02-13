// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.encryption.passwordtable;

public class PWObject {

    private char state;

    protected PWObject() {

    }

    public PWObject(char c) {
        this.state = c;
    }

    public boolean update() {
        return false;
    }

    public char getChar() {
        return state;
    }

    void setChar(char c) {
        state = c;
    }

}