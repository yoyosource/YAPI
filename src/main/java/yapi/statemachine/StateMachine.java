package yapi.statemachine;

import java.util.ArrayList;
import java.util.List;

public class StateMachine {

    public static void main(String[] args) {
        StateMachine stateMachine = new StateMachine();
        State state = new State("Hello").addTransition("World", "a");
        State state1 = new State("World").addTransition("Hello", "a");
        stateMachine.addStart(state).addEnd(state1).validate();
    }

    private List<State> states = new ArrayList<>();
    private List<StateTransition> stateTransitions = new ArrayList<>();
    private List<StateTransition> transitions = new ArrayList<>();

    private State startNode = null;
    private List<State> endNodes = new ArrayList<>();

    private boolean validated = false;
    private String error = "";

    public StateMachine() {
        State.setCompact(true);
        StateTransition.setCompact(true);
    }

    public StateMachine(boolean compact) {
        State.setCompact(compact);
        StateTransition.setCompact(compact);
    }

    public StateMachine validate() {
        // Reset validation flag
        validated = false;
        error = "";

        // Check if startNode is set
        if (startNode == null) {
            error = "No Start Node";
            return this;
        }
        // Check if endNodes are given
        if (endNodes.isEmpty()) {
            error = "No End Node(s)";
            return this;
        }

        // Add every stateTransition to the internal List
        if (!stateTransitions.isEmpty()) {
            for (StateTransition transition : stateTransitions) {
                transitions.add(transition.build(this));
            }
            stateTransitions.clear();
        }

        // Check what state is reachable by adding every ID to a list and removing those which are reachable
        List<Long> reachable = new ArrayList<>();
        for (State state : states) {
            reachable.add(state.getID());
        }
        reachable.remove(startNode.getID());
        for (StateTransition transition : transitions) {
            reachable.remove(transition.getTransitionID());
        }

        // Check reachable by empty list
        if (reachable.isEmpty()) {
            validated = true;
            return this;
        }

        // Assembling the error message
        StringBuilder st = new StringBuilder();
        st.append("Unreachable States:\n");
        boolean b = false;
        for (Long l : reachable) {
            if (b) {
                st.append(", ");
            }
            b = true;
            State node = getNode(l);
            if (node != null) {
                st.append("'").append(node.getName()).append("'").append(" -> (ID:").append(node.getID()).append(')');
            }
        }
        error = st.toString();
        return this;
    }

    public boolean isValidated() {
        return validated;
    }

    public String getError() {
        if (!isValidated()) {
            return "";
        }
        return error;
    }

    private List<StateTransition> getTransitions(State node) {
        List<StateTransition> transitionList = new ArrayList<>();
        for (int i = 0; i < transitions.size(); i++) {
            if (transitions.get(i).getFromID() == node.getID()) {
                transitionList.add(transitions.get(i));
            }
        }
        return transitionList;
    }

    private State getNode(long id) {
        for (int i = 0; i < states.size(); i++) {
            if (states.get(i).getID() == id) {
                return states.get(i);
            }
        }
        return null;
    }

    public StateMachine addStart(State state) {
        validated = false;
        startNode = state;
        return addState(state);
    }

    public StateMachine addEnd(State state) {
        validated = false;
        endNodes.add(state);
        return addState(state);
    }

    public StateMachine addState(State state) {
        validated = false;
        states.add(state);
        stateTransitions.addAll(state.getInternalTransitions());
        return this;
    }

    public StateMachine addTransition(StateTransition transition) {
        validated = false;
        stateTransitions.add(transition);
        return this;
    }

    long getStateID(String name) {
        for (State state : states) {
            if (state.getName().equals(name)) {
                return state.getID();
            }
        }
        return -1;
    }

}
