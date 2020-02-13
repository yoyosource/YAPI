// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.encryption.passwordtable;

public class PWCharacterUpperCase extends PWObject {

    public PWCharacterUpperCase() {
        super('A');
    }

    @Override
    public boolean update() {
        if (getChar() == 'Z') {
            setChar('A');
            return true;
        }
        setChar((char)(getChar() + 1));
        return false;
    }
}