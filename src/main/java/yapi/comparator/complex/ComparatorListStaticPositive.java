// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.comparator.complex;

public class ComparatorListStaticPositive<T> extends ComparatorList<T> {

    private ComparatorListStaticPositive(MComparator<T>... comparators) {
        super(comparators);
    }

    public ComparatorListStaticPositive() {

    }

    @Override
    public int compare(T o1, T o2) {
        return 1;
    }

}