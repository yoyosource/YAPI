// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.statemachine;

import java.util.Objects;

@Deprecated(since = "Version 1.2, now found in yapi/statemachine/")
public class State {

    private String name;

    public State(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof State)) return false;
        State state = (State) o;
        return Objects.equals(name, state.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "State{" +
                "name='" + name + '\'' +
                '}';
    }

}