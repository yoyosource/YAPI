// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.streams.predicates;

import java.util.function.Predicate;

public class Filter<R> implements Predicate<Object> {

    private R r;

    public Filter(R r) {
        this.r = r;
    }

    @Override
    public boolean test(Object o) {
        return o.getClass().isInstance(r);
    }
}