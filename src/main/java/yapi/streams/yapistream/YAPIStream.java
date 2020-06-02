// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.streams.yapistream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class YAPIStream<T> {

    public static <T> YAPIStream<T> of(boolean copy, List<T> list) {
        return new YAPIStream<>(list, copy);
    }

    public static <T> YAPIStream<T> of(List<T> list) {
        return of(false, list);
    }

    public static <T> YAPIStream<T> of(boolean copy, T... list) {
        return of(copy, Arrays.asList(list));
    }
    public static <T> YAPIStream<T> of(T... list) {
        return of(false, list);
    }

    private List<T> list = new ArrayList<>();
    private int size = 0;

    private YAPIStream(List<T> list, boolean copy) {
        if (!copy) {
            this.list = list;
        } else {
            this.list.addAll(list);
        }
        size = this.list.size();
    }

}