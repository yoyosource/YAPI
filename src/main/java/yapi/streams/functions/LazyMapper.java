// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.streams.functions;

import java.util.function.Function;

public class LazyMapper<R> implements Function<Object, R> {
    @Override
    public R apply(Object o) {
        return (R)o;
    }
}