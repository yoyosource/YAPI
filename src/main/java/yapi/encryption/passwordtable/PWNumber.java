// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.encryption.passwordtable;

public class PWNumber extends PWObject {

    public PWNumber() {
        super('0');
    }

    @Override
    public boolean update() {
        if (getChar() == '9') {
            setChar('0');
            return true;
        }
        setChar((char)(getChar() + 1));
        return false;
    }
}