// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.graphics;

public class YGraphicObject {

    private long id = YGraphicsManager.getID();

    long getId() {
        return id;
    }

}