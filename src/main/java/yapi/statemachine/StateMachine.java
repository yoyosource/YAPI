// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.statemachine;

import yapi.internal.annotations.yapi.WorkInProgress;
import yapi.internal.annotations.yapi.WorkInProgressType;
import yapi.math.NumberRandom;

import java.util.ArrayList;
import java.util.List;

@WorkInProgress(context = WorkInProgressType.ALPHA)
public class StateMachine {

    public static void main(String[] args) {
        StateMachine stateMachine = new StateMachine();
        State start = stateMachine.getState(StateType.START, "s1");
        State number = stateMachine.getState(StateType.STOP, "s2");

        start.addTransition(number, stateMachine.getTransitionModifier().add("123456789"));
        number.addTransition(number, stateMachine.getTransitionModifier().add("0123456789"));

        stateMachine.build();
    }

    private List<State> states = new ArrayList<>();
    private NumberRandom numberRandom = NumberRandom.getInstance();

    private boolean builded = false;

    private State startNode = null;
    private State current = null;

    public StateMachine() {

    }

    public State getState(StateType type, String name) {
        if (builded) throw new IllegalStateException();
        State state = new State(type, name);
        state.stateMachine = this;
        states.add(state);
        return state;
    }

    public StateTransitionModifier getTransitionModifier() {
        if (builded) throw new IllegalStateException();
        return new StateTransitionModifier(true);
    }

    public StateTransitionModifier getNegatedTransitionModifier() {
        if (builded) throw new IllegalStateException();
        return new StateTransitionModifier(true);
    }

    public StateMachine build() {
        builded = true;

        boolean hasStart = false;
        boolean hasStop = false;
        for (State state : states) {
            if (state.type == StateType.START) {
                if (hasStart) {
                    throw new IllegalStateException();
                }
                startNode = state;
                hasStart = true;
            }
            if (state.type == StateType.STOP) {
                hasStop = true;
            }
        }
        if (!hasStart) {
            throw new IllegalStateException();
        }
        if (!hasStop) {
            throw new IllegalStateException();
        }

        if (!checkRecursive(startNode, new ArrayList<>())) {
            throw new IllegalStateException();
        }

        return this;
    }

    private boolean checkRecursive(State currentNode, List<State> checkedStates) {
        if (checkedStates.contains(currentNode)) {
            return currentNode.type.equals(StateType.STOP);
        }

        List<State> checked = copy(checkedStates);
        checked.add(currentNode);

        List<StateTransition> transitions = currentNode.transitions;
        if (transitions.isEmpty()) {
            return currentNode.type.equals(StateType.STOP);
        }

        for (StateTransition transition : transitions) {
            if (!checkRecursive(transition.nextState, checked)) {
                return false;
            }
        }
        return true;
    }

    private List<State> copy(List<State> list) {
        List<State> states = new ArrayList<>();
        states.addAll(list);
        return states;
    }

    public StateMachine copy() {
        if (!builded) throw new IllegalStateException();
        StateMachine stateMachine = new StateMachine();
        stateMachine.states.addAll(states);
        return stateMachine.build();
    }

}