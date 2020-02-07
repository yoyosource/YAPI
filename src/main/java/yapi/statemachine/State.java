package yapi.statemachine;

import yapi.manager.statemachine.Transition;

import java.util.ArrayList;
import java.util.List;

public class State {

    private static long currentID = 0;
    private static boolean compact = false;

    public static void setCompact(boolean compact) {
        State.compact = compact;
    }

    private String name;
    private long id = currentID++;

    private List<StateTransition> internalTransitions = new ArrayList<>();

    public State(String name) {
        if (name == null) {
            this.name = "";
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public long getID() {
        return id;
    }

    public State addTransition(String toState, char c) {
        internalTransitions.add(new StateTransition(name, toState, c));
        return this;
    }

    public State addTransition(String toState, char[] chars) {
        internalTransitions.add(new StateTransition(name, toState, chars));
        return this;
    }

    public State addTransition(String toState, String s) {
        internalTransitions.add(new StateTransition(name, toState, s));
        return this;
    }

    public State addTransition(String toState, StringBuilder s) {
        internalTransitions.add(new StateTransition(name, toState, s));
        return this;
    }

    List<StateTransition> getInternalTransitions() {
        List<StateTransition> transitions = new ArrayList<>(internalTransitions);
        internalTransitions.clear();
        return transitions;
    }

    @Override
    public String toString() {
        if (compact) {
            return name + "{" + id + "}";
        }
        return "State{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

}
