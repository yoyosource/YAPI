// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.comparator.complex;

import java.util.Arrays;
import java.util.List;

public class ComparatorList<T> {

    private List<MComparator<T>> comparators;
    private int size = 0;

    @SafeVarargs
    public ComparatorList(MComparator<T>... comparators) {
        this.comparators = Arrays.asList(comparators);
        this.size = comparators.length;
    }

    public int compare(T o1, T o2) {
        for (int i = 0; i < size; i++) {
            int compare = comparators.get(i).compare(o1, o2);
            if (compare != 0) {
                return compare;
            }
        }
        return 0;
    }

}