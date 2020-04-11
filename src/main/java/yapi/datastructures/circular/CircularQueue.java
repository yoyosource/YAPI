// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.datastructures.circular;

import java.util.Arrays;

public class CircularQueue<T> {

    private T[] ts;

    private int addIndex = 0;
    private int removeIndex = 0;

    public CircularQueue(int size) {
        ts = (T[])new Object[size];
    }

    public void add(T t) {
        ts[addIndex] = t;
        addIndex = (addIndex + 1) % ts.length;
        if (addIndex == removeIndex) {
            removeIndex = (removeIndex + 1) % ts.length;
        }
    }

    public T remove() {
        if (removeIndex == addIndex) {
            throw new ArrayIndexOutOfBoundsException("Array is empty");
        }
        T t = ts[removeIndex];
        removeIndex = (removeIndex + 1) % ts.length;
        return t;
    }

    @Override
    public String toString() {
        return "CircularQueue{" +
                "addIndex=" + addIndex +
                ", removeIndex=" + removeIndex +
                ", ts=" + Arrays.toString(ts) +
                '}';
    }
}