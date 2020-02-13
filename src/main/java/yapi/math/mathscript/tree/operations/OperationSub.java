// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.mathscript.tree.operations;

import yapi.math.mathscript.state.VariableState;
import yapi.math.mathscript.tree.Knot;

import java.math.BigDecimal;

public class OperationSub extends Knot {

    @Override
    public OperationSub addKnot(Knot knot) {
        super.addKnot(knot);
        return this;
    }

    @Override
    public BigDecimal getResult(VariableState variableState) {
        BigDecimal output = null;
        for (Knot k : getKnots()) {
            if (output == null) {
                output = k.getResult(variableState.copy());
            } else {
                output = output.subtract(k.getResult(variableState.copy()), getMathContext());
            }
        }
        return output;
    }

    @Override
    public String toString(int i) {
        StringBuilder st = new StringBuilder();
        st.append("sub" + "\n");
        boolean b = false;
        for (Knot knot : getKnots()) {
            if (b) {
                st.append("\n");
            }
            b = true;
            st.append(" ".repeat(i));
            st.append(knot.toString(i + 1));
        }
        return st.toString();
    }

}