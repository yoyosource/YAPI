// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.comparator.complex;

public class ComparatorBranch<T> implements MComparator<T> {

    private Compare<T, ?> compare;
    private ComparatorList<T> compareNegative;
    private ComparatorList<T> compareEqual;
    private ComparatorList<T> comparePositive;

    public ComparatorBranch(Compare<T, ?> compare, ComparatorList<T> compareNegative, ComparatorList<T> compareEqual, ComparatorList<T> comparePositive) {
        this.compare = compare;
        this.compareNegative = compareNegative;
        this.compareEqual = compareEqual;
        this.comparePositive = comparePositive;
    }

    @Override
    public int compare(T o1, T o2) {
        int compare = this.compare.compare(o1, o2);
        if (compare == 0) {
            return compareEqual.compare(o1, o2);
        }
        if (compare == 1) {
            return comparePositive.compare(o1, o2);
        }
        if (compare == -1) {
            return compareNegative.compare(o1, o2);
        }
        return 0;
    }

}