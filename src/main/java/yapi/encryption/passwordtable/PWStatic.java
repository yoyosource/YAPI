// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.encryption.passwordtable;

public class PWStatic extends PWObject {

    public PWStatic(char c) {
        super(c);
    }

    @Override
    public boolean update() {
        return true;
    }

}