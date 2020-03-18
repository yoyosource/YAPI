// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.yapion.internal.serializable;

public class YAPIONDeserializable {

    private Class<?> clazz;
    private Object object;

    public YAPIONDeserializable(Class<?> clazz, Object object) {
        this.clazz = clazz;
        this.object = object;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Object getObject() {
        return object;
    }

    @Override
    public String toString() {
        return "YAPIONDeserializable{" +
                "clazz=" + clazz +
                ", object=" + object +
                '}';
    }
}