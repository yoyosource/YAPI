// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.mathscript.tree.functions;

import ch.obermuhlner.math.big.BigDecimalMath;
import yapi.math.mathscript.state.VariableState;
import yapi.math.mathscript.tree.Knot;

import java.math.BigDecimal;

public class FunctionPow extends Knot {

    @Override
    public Knot addKnot(Knot knot) {
        if (knowCount() < 2) {
            super.addKnot(knot);
        }
        return this;
    }

    @Override
    public BigDecimal getResult(VariableState variableState) {
        return BigDecimalMath.pow(getKnots().get(0).getResult(variableState.copy()), getKnots().get(1).getResult(variableState.copy()), getMathContext());
    }

    @Override
    public String toString(int i) {
        StringBuilder st = new StringBuilder();
        st.append("pow" + "\n");
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