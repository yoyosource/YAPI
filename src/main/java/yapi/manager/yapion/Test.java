// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.yapion;

import yapi.internal.annotations.yapion.YAPIONSave;

import java.util.ArrayList;
import java.util.List;

@YAPIONSave
public class Test {

    public static volatile transient double i = 10.0;

    private static List<String> strings = new ArrayList<>();

    static {
        strings.add("Hello WOrld");
        strings.add("Hello WOrld!");
        strings.add("Hello WOrld!!");
        strings.add("Hello WOrld!!!");
        strings.add("Hello WOrld!!!!");
    }

    public Hugo hugo = new Hugo(this);

    public String tru = "true";

    public Hugo hugo2 = hugo;

    private Double test = 0.0;

    @YAPIONSave
    private class Hugo {

        public int i = 0;

        public Test t;

        public Hugo2 hugo2 = new Hugo2();

        private Hugo() {

        }

        public Hugo(Test t) {
            this.t = t;
        }

        public int getI() {
            return i;
        }

        @YAPIONSave
        private class Hugo2 {

            private int i = 0;

        }

    }

}