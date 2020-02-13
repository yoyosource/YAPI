// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.mathscript.tree.special;

import yapi.exceptions.math.MathScriptException;
import yapi.math.mathscript.state.Variable;
import yapi.math.mathscript.state.VariableState;
import yapi.math.mathscript.tree.Knot;
import yapi.math.mathscript.tree.values.FunctionVariable;

import java.math.BigDecimal;
import java.math.BigInteger;

public class FunctionSum extends Knot {

    @Override
    public Knot addKnot(Knot knot) {
        if (getKnots().size() < 4) {
            getKnots().add(knot);
        }
        return this;
    }

    @Override
    public BigDecimal getResult(VariableState variableState) {
        if (!(getKnots().get(0) instanceof FunctionVariable)) {
            throw new MathScriptException("First parameter of 'sum' needs to be a variable");
        }
        String name = ((FunctionVariable)getKnots().get(0)).getName();
        BigInteger start = getKnots().get(1).getResult(variableState.copy()).toBigIntegerExact();
        BigInteger stop = getKnots().get(2).getResult(variableState.copy()).toBigIntegerExact();

        BigDecimal output = BigDecimal.ZERO;
        while (start.compareTo(stop) < 0) {
            output = output.add(getKnots().get(3).getResult(variableState.add(new Variable(name).setValue(start))), getMathContext());
            start = start.add(BigInteger.ONE);
        }
        return output;
    }

    @Override
    public String toString(int i) {
        StringBuilder st = new StringBuilder();
        st.append("sum" + "\n");
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