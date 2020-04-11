// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.datastructures.linkedlist;

public class Node<T> {

    long id = -1;

    T value;

    Node<T> prev = null;
    Node<T> next = null;

    Node(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        if (value == null) {
            return "null";
        }
        return value.toString();
    }

}