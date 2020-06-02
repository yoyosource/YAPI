// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.streams.functions;

import java.util.function.Function;

public class Mapper<R> implements Function<Object, R> {

    private R r;

    public Mapper(R r) {
        this.r = r;
    }

    @Override
    public R apply(Object o) {
        if (o.getClass().isInstance(r)) {
            return (R)o;
        }
        return null;
    }

}