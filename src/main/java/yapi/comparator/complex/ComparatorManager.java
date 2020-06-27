// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.comparator.complex;

import java.util.Comparator;

public class ComparatorManager<T> implements Comparator<T> {

    private ComparatorList<T> comparatorList;

    public ComparatorManager(ComparatorList<T> comparatorList) {
        this.comparatorList = comparatorList;
    }

    @Override
    public int compare(T o1, T o2) {
        return comparatorList.compare(o1, o2);
    }

}