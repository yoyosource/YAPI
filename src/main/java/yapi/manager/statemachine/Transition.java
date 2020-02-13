// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.statemachine;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Transition {

    private State currentState;
    private State nextState;

    private List<Character> stateChangers = new ArrayList<>();

    private TransitionModifier transitionModifier = null;

    public Transition(String currentState, String nextState) {
        this(currentState, nextState, new TransitionModifier());
    }

    public Transition(State currentState, State nextState) {
        this(currentState, nextState, new TransitionModifier());
    }

    public Transition(String currentState, String nextState, TransitionModifier transitionModifier) {
        this.currentState = new State(currentState);
        this.nextState = new State(nextState);
        this.transitionModifier = transitionModifier;
    }

    public Transition(State currentState, State nextState, TransitionModifier transitionModifier) {
        this.currentState = currentState;
        this.nextState = nextState;
        this.transitionModifier = transitionModifier;
    }

    public Transition(String currentState, String nextState, char... chars) {
        this(currentState, nextState, new TransitionModifier(), chars);
    }

    public Transition(State currentState, State nextState, char... chars) {
        this(currentState, nextState, new TransitionModifier(), chars);
    }

    public Transition(String currentState, String nextState, TransitionModifier transitionModifier, char... chars) {
        this.currentState = new State(currentState);
        this.nextState = new State(nextState);
        for (char c : chars) {
            stateChangers.add(c);
        }
        this.transitionModifier = transitionModifier;
    }

    public Transition(State currentState, State nextState, TransitionModifier transitionModifier, char... chars) {
        this.currentState = currentState;
        this.nextState = nextState;
        for (char c : chars) {
            stateChangers.add(c);
        }
        this.transitionModifier = transitionModifier;
    }

    public Transition(String currentState, String nextState, List<Character> stateChangers) {
        this(currentState, nextState, stateChangers, new TransitionModifier());
    }

    public Transition(State currentState, State nextState, List<Character> stateChangers) {
        this(currentState, nextState, stateChangers, new TransitionModifier());
    }

    public Transition(String currentState, String nextState, List<Character> stateChangers, TransitionModifier transitionModifier) {
        this.currentState = new State(currentState);
        this.nextState = new State(nextState);
        this.stateChangers = stateChangers;
        this.transitionModifier = transitionModifier;
    }

    public Transition(State currentState, State nextState, List<Character> stateChangers, TransitionModifier transitionModifier) {
        this.currentState = currentState;
        this.nextState = nextState;
        this.stateChangers = stateChangers;
        this.transitionModifier = transitionModifier;
    }

    public State getCurrentState() {
        return currentState;
    }

    public State getNextState() {
        return nextState;
    }

    public List<Character> getStateChangers() {
        return stateChangers;
    }

    public TransitionModifier getTransitionModifier() {
        return transitionModifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transition)) return false;
        Transition that = (Transition) o;
        return Objects.equals(currentState, that.currentState) &&
                Objects.equals(nextState, that.nextState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentState, nextState);
    }

    @Override
    public String toString() {
        return currentState.getName() + " -> " + nextState.getName() + " " + stateChangers + " " +  transitionModifier.toString().substring("TransitionModifier".length()) + "";
    }
}