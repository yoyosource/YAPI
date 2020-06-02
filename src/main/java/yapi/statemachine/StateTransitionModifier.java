// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.statemachine;

import java.util.ArrayList;
import java.util.List;

public class StateTransitionModifier {

    private boolean negation;
    private boolean builded = false;
    private List<Character> characters = new ArrayList<>();

    StateTransitionModifier(boolean negation) {
        this.negation = negation;
    }

    public StateTransitionModifier fromGroup(String s) {
        if (builded) {
            return this;
        }
        builded = true;
        characters.clear();
        if (s.startsWith("[") && s.endsWith("]")) {
            processGroup(s.substring(1, s.length() - 1));
        }
        if (s.equals(".")) {
            negation = true;
        }
        return this;
    }

    private void processGroup(String s) {

    }

    public StateTransitionModifier add(char c) {
        if (builded) {
            return this;
        }
        characters.add(c);
        return this;
    }

    public StateTransitionModifier add(char... chars) {
        if (builded) {
            return this;
        }
        for (char c : chars) {
            characters.add(c);
        }
        return this;
    }

    public StateTransitionModifier add(String s) {
        return add(s.toCharArray());
    }

    public StateTransitionModifier add(StringBuilder st) {
        return add(st.toString().toCharArray());
    }

    StateTransitionModifier build() {
        builded = true;
        return this;
    }

    boolean isCaptured(char c) {
        if (negation) {
            return !characters.contains(c);
        } else {
            return characters.contains(c);
        }
    }

}