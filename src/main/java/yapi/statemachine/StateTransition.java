// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.statemachine;

public class StateTransition {

    String nextStateName;
    State nextState;
    StateTransitionModifier modifier;

    StateTransition(String nextState, StateTransitionModifier modifier) {
        this.nextStateName = nextState;
        this.modifier = modifier;
    }

    StateTransition(State nextState, StateTransitionModifier modifier) {
        this.nextState = nextState;
        this.modifier = modifier;
    }

}