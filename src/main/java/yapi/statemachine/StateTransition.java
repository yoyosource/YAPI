// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.statemachine;

import java.util.Arrays;

public class StateTransition {

    private static boolean compact = false;

    public static void setCompact(boolean compact) {
        StateTransition.compact = compact;
    }

    private long fromID = -1;
    private String fromIDName = "";
    private long transitionID = -1;
    private String transitionIDName = "";
    private char[] transitionChars = null;

    private boolean build = false;

    public StateTransition(String fromIDName, String toIDName, char c) {
        this.fromIDName = fromIDName;
        this.transitionIDName = toIDName;
        transitionChars = new char[]{c};
    }

    public StateTransition(long fromID, long toID, char c) {
        this.fromID = fromID;
        this.transitionID = toID;
        transitionChars = new char[]{c};
        build = true;
    }

    public StateTransition(String fromIDName, String toIDName, char[] chars) {
        this.fromIDName = fromIDName;
        this.transitionIDName = toIDName;
        transitionChars = chars;
    }

    public StateTransition(long fromID, long toID, char[] chars) {
        this.fromID = fromID;
        this.transitionID = toID;
        transitionChars = chars;
        build = true;
    }

    public StateTransition(String fromIDName, String toIDName, String s) {
        this.fromIDName = fromIDName;
        this.transitionIDName = toIDName;
        transitionChars = s.toCharArray();
    }

    public StateTransition(long fromID, long toID, String s) {
        this.fromID = fromID;
        this.transitionID = toID;
        transitionChars = s.toCharArray();
        build = true;
    }

    public StateTransition(String fromIDName, String toIDName, StringBuilder s) {
        this.fromIDName = fromIDName;
        this.transitionIDName = toIDName;
        transitionChars = s.toString().toCharArray();
    }

    public StateTransition(long fromID, long toID, StringBuilder st) {
        this.fromID = fromID;
        this.transitionID = toID;
        transitionChars = st.toString().toCharArray();
        build = true;
    }

    public StateTransition() {}

    public StateTransition setFromID(long fromID) {
        if (build) {
            return this;
        }
        this.fromID = fromID;
        return this;
    }

    public StateTransition setFromID(String name) {
        if (build) {
            return this;
        }
        this.fromIDName = name;
        return this;
    }

    public StateTransition setToID(long toID) {
        if (build) {
            return this;
        }
        this.transitionID = toID;
        return this;
    }

    public StateTransition setToID(String name) {
        if (build) {
            return this;
        }
        this.transitionIDName = name;
        return this;
    }

    public StateTransition setStateChangers(char c) {
        if (build) {
            return this;
        }
        transitionChars = new char[]{c};
        return this;
    }

    public StateTransition setStateChangers(char[] chars) {
        if (build) {
            return this;
        }
        transitionChars = chars;
        return this;
    }

    public StateTransition setStateChangers(String s) {
        if (build) {
            return this;
        }
        transitionChars = s.toCharArray();
        return this;
    }

    public StateTransition setStateChangers(StringBuilder s) {
        if (build) {
            return this;
        }
        transitionChars = s.toString().toCharArray();
        return this;
    }

    StateTransition build(StateMachine stateMachine) {
        if (build) {
            return this;
        }
        if (!fromIDName.isEmpty()) {
            fromID = stateMachine.getStateID(fromIDName);
        }
        if (!transitionIDName.isEmpty()) {
            transitionID = stateMachine.getStateID(transitionIDName);
        }
        build = true;
        return this;
    }

    public long getFromID() {
        return fromID;
    }

    public long getTransitionID() {
        return transitionID;
    }

    public long transition(char c) {
        for (int i = 0; i < transitionChars.length; i++) {
            if (transitionChars[i] == c) {
                return transitionID;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        if (compact) {
            return fromID + "->" + transitionID + "(" + Arrays.toString(transitionChars) + ")";
        }
        return "StateTransition{" +
                "fromID=" + fromID +
                ", transitionID=" + transitionID +
                ", transitionChars=" + Arrays.toString(transitionChars) +
                '}';
    }
}