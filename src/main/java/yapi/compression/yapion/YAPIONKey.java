// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.compression.yapion;

public class YAPIONKey {

    private char c1 = '!';
    private char c2 = '!';

    public String getKey() {
        String s = c1 + "" + c2;
        c1 += 1;
        if (c1 > '~') {
            c1 = '!';
            c2 += 1;
            if (c2 > '~') {
                throw new NullPointerException();
            }
        }
        return s;
    }

}