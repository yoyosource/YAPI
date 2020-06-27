// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.comparator.complex;

public class ComparatorListStaticEqual<T> extends ComparatorList<T> {

    private ComparatorListStaticEqual(MComparator<T>... comparators) {
        super(comparators);
    }

    public ComparatorListStaticEqual() {

    }

    @Override
    public int compare(T o1, T o2) {
        return 0;
    }

}