// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.comparator.complex;

public class ComparatorStaticNegative<T> implements MComparator<T> {

    @Override
    public int compare(T o1, T o2) {
        return -1;
    }

}