// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.graphics;

import java.util.ArrayList;
import java.util.List;

public class YGraphicsManager {

    private static long id = 0;
    private List<YGraphicObject> objects = new ArrayList<>();
    private long activeID = -1;

    public YGraphicsManager() {

    }

    static final long getID() {
        return id++;
    }

    public void registerYGraphicObject(YGraphicObject yGraphicObject) {
        objects.add(yGraphicObject);
    }

    public void setActiveID(long id) {
        for (YGraphicObject yGraphicObject : objects) {
            if (yGraphicObject.getId() == id) {
                activeID = id;
                break;
            }
        }
        activeID = -1;
    }

}