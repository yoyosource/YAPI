// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.mathscript.tree.values;

import yapi.math.mathscript.state.VariableState;
import yapi.math.mathscript.tree.Knot;

import java.math.BigDecimal;

public class FunctionVariable extends Knot {

    private String name;

    public FunctionVariable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public BigDecimal getResult(VariableState variableState) {
        return variableState.getVariable(name);
    }

    @Override
    public String toString(int i) {
        StringBuilder st = new StringBuilder();
        st.append("variable: " + name);
        return st.toString();
    }
}