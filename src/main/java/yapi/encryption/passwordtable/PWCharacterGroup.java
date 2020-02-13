// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.encryption.passwordtable;

public class PWCharacterGroup extends PWObject {

    char[] chars;
    int i = 0;

    public PWCharacterGroup(String s) {
        chars = s.toCharArray();
    }

    @Override
    public boolean update() {
        i++;
        if (i == chars.length) {
            i = 0;
            return true;
        }
        return false;
    }

    @Override
    public char getChar() {
        return chars[i];
    }
}