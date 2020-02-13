// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.encryption.passwordtable;

public class PWAnyCharacter extends PWObject {

    public PWAnyCharacter() {
        super(' ');
    }

    @Override
    public boolean update() {
        if (getChar() == '~') {
            setChar(' ');
            return true;
        }
        setChar((char)(getChar() + 1));
        return false;
    }
}