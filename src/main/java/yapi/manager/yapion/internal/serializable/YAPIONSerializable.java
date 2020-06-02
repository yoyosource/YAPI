// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.yapion.internal.serializable;

import yapi.internal.runtimeexceptions.objectnotation.YAPIONException;
import yapi.manager.yapion.value.YAPIONObject;
import yapi.manager.yapion.value.YAPIONPointer;

public class YAPIONSerializable {

    private int id;
    private Object object;
    private YAPIONObject yapionObject;

    public YAPIONSerializable(Object object, int id, YAPIONObject yapionObject) {
        this.id = id;
        this.object = object;
        this.yapionObject = yapionObject;
    }

    public YAPIONPointer getPointer() {
        if (id > 1048576) {
            throw new YAPIONException();
        }
        int x = id % (YAPIONPointer.pointerChars.length * YAPIONPointer.pointerChars.length);
        int y = (id - x) / (YAPIONPointer.pointerChars.length * YAPIONPointer.pointerChars.length);
        return new YAPIONPointer(YAPIONPointer.toBase32(new int[]{y, x}) + YAPIONPointer.createReferenceId(yapionObject));
    }

    public boolean checkObjectEquality(Object object) {
        return this.object == object;
    }

    @Override
    public String toString() {
        return "YAPIONSerializable{" +
                "id=" + id +
                ", object=" + object +
                '}';
    }
}