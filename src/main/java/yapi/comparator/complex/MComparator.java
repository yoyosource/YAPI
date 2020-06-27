// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.comparator.complex;

@FunctionalInterface
public interface MComparator<T> {

    int compare(T o1, T o2);

}