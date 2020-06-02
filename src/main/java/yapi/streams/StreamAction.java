// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.streams;

@FunctionalInterface
public interface StreamAction<T> {

    T value(T t1, T t2);

}