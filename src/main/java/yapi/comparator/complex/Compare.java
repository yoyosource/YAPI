// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.comparator.complex;

import java.util.Comparator;
import java.util.function.Function;

public class Compare<T, U> implements MComparator<T> {

    private Comparator<U> comparator;
    private Function<? super T, ? extends U> mapper;

    public Compare(Function<? super T, ? extends U> keyExtractor, Comparator<U> comparator) {
        this.mapper = keyExtractor;
        this.comparator = comparator;
    }

    @Override
    public int compare(T o1, T o2) {
        return comparator.compare(mapper.apply(o1), mapper.apply(o2));
    }

}