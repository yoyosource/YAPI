// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.statemachine;

import java.util.ArrayList;
import java.util.List;

public class State {

    StateType type;
    String name;

    List<StateTransition> transitions = new ArrayList<>();

    StateMachine stateMachine;

    State(StateType type, String name) {
        this.type = type;
        this.name = name;
    }

    public State addTransition(State nextState, StateTransitionModifier transitionModifier) {
        transitions.add(new StateTransition(nextState, transitionModifier));
        return this;
    }

}