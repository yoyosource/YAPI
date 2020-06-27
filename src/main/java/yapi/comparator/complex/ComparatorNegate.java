// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.comparator.complex;

public class ComparatorNegate<T> implements MComparator<T> {

    private ComparatorList<T> comparatorList;

    public ComparatorNegate(ComparatorList<T> comparatorList) {
        this.comparatorList = comparatorList;
    }

    @Override
    public int compare(T o1, T o2) {
        return comparatorList.compare(o1, o2) * -1;
    }
}