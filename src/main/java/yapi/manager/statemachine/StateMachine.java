// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.statemachine;

import yapi.internal.exceptions.StateMachineException;

import java.util.*;

@Deprecated(since = "Version 1.2, now found in yapi/statemachine/StateMachine.java")
public class StateMachine {

    public static final State STATE_START = new State("$START");
    public static final State STATE_END = new State("$END");

    private List<State> states = new ArrayList<>();
    private Map<State, List<Transition>> transitions = new HashMap<>();
    private boolean valid = false;

    public StateMachine() {
        states.add(STATE_START);
        states.add(STATE_END);
    }

    public static void main(String[] args) {
        // TODO: redo this StateMachine System
        StateMachine stateMachine = parse(".+");
        stateMachine.build();

        System.out.println(stateMachine);

        stateMachine.check("aaaaaaaaaaabbbbbaaaaaabbbbb");
    }

    public static StateMachine parse(String regex) {
        if (true) {
            throw new StateMachineException("Currently unsupported.");
        }
        if (regex.startsWith("*") || regex.startsWith("+")) {
            throw new StateMachineException("Regex cannot start with '*' or '+'");
        }

        StateMachine stateMachine = new StateMachine();
        char[] chars = regex.toCharArray();
        State currentState = STATE_START;
        int stateIndex = 0;

        boolean escaped = false;
        for (int i = 0; i < chars.length; i++) {
            if (!escaped && chars[i] == '\\') {
                escaped = true;
                continue;
            }

            if (!escaped && chars[i] == '*') {
                System.out.println(i + " " + chars[i]);
            }
            if (!escaped && chars[i] == '+') {
                char c = chars[i - 1];
                State nextState = new State("" + stateIndex++);
                State endState = new State("" + stateIndex);
                stateMachine.addState(nextState);
                stateMachine.addState(endState);
                Transition t1;
                Transition t2;
                if (c == '.' && (i > 1 && chars[i - 2] != '\\' || i > 0)) {
                    t1 = new Transition(currentState, nextState, new TransitionModifier(false, true, false, false));
                    t2 = new Transition(nextState, endState, new TransitionModifier(false, false, true, true));
                } else {
                    t1 = new Transition(currentState, nextState, new TransitionModifier(false, true, false, false), c);
                    t2 = new Transition(nextState, endState, new TransitionModifier(false, false, true, true), c);
                }
                stateMachine.addTransition(t1);
                stateMachine.addTransition(t2);
                stateIndex++;
            }

            escaped = false;
        }

        return stateMachine.build();
    }

    public void addState(State state) {
        if (!valid) {
            this.states.add(state);
        }
    }

    public void addTransition(Transition transition) {
        if (!valid) {
            if (transitions.containsKey(transition.getCurrentState())) {
                transitions.get(transition.getCurrentState()).add(transition);
            } else {
                transitions.put(transition.getCurrentState(), new ArrayList<>());
                transitions.get(transition.getCurrentState()).add(transition);
            }
        }
    }

    public StateMachine build() {
        if (valid) {
            return this;
        }
        for (Map.Entry<State, List<Transition>> e : transitions.entrySet()) {
            for (Transition transition : e.getValue()) {
                if (!states.contains(transition.getCurrentState())) {
                    throw new StateMachineException("State is unknown: " + transition.getCurrentState().getName());
                }
                if (!states.contains(transition.getNextState())) {
                    throw new StateMachineException("State is unknown: " + transition.getNextState().getName());
                }
            }
        }

        valid = true;
        return this;
    }

    public void check(String s) {
        if (!valid) {
            throw new StateMachineException("State Machine not validated use build() first");
        }

        char[] chars = s.toCharArray();
        State currentState = new State("$START");

        int inCapture = 0;
        boolean isExcluded = false;

        StringBuilder currentCapture = new StringBuilder();
        Stack<String> lastCaptures = new Stack<>();
        List<String> allCaptures = new ArrayList<>();

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            List<Transition> transitions = this.transitions.get(currentState);
            if (transitions == null) {
                break;
            }

            for (Transition transition : transitions) {

                if ((transition.getStateChangers().isEmpty() && !transition.getTransitionModifier().isInvert() || !transition.getStateChangers().isEmpty() && transition.getTransitionModifier().isInvert()) || (transition.getStateChangers().contains(c) && !transition.getTransitionModifier().isInvert()) || (!transition.getStateChangers().contains(c) && transition.getTransitionModifier().isInvert())) {
                    if (transition.getTransitionModifier().isStartCapture()) {
                        if (currentCapture.length() > 0) {
                            lastCaptures.push(currentCapture.toString());
                        }
                        inCapture++;
                    }
                    if (transition.getTransitionModifier().isStopCapture() && inCapture > 0) {
                        if (currentCapture.length() > 0) {
                            String string = currentCapture.toString();
                            allCaptures.add(string);
                            currentCapture = new StringBuilder();
                            if (!lastCaptures.isEmpty()) {
                                currentCapture.append(lastCaptures.pop());
                                currentCapture.append(string);
                            }
                        }
                        inCapture--;
                    }
                    if (transition.getTransitionModifier().isExcludeInCapture()) {
                        isExcluded = true;
                    }
                    if (inCapture < 0) {
                        inCapture = 0;
                    }

                    System.out.println(leading(chars.length, i) + i + ": " + transition.toString());
                    currentState = transition.getNextState();
                }

            }
            if (inCapture > 0 && !isExcluded) {
                currentCapture.append(c);
            }
            isExcluded = false;
        }

        if (currentCapture.length() > 0) {
            allCaptures.add(currentCapture.toString());
        }

        System.out.println(allCaptures);
    }

    private String leading(int length, int i) {
        return " ".repeat((length + "").length() - (i + "").length());
    }

    @Override
    public String toString() {
        return "StateMachine{" +
                "states=" + states +
                ", transitions=" + transitions +
                ", valid=" + valid +
                '}';
    }
}