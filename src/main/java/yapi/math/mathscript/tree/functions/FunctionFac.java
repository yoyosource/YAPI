// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.mathscript.tree.functions;

import yapi.exceptions.math.MathScriptException;
import yapi.math.NumberUtils;
import yapi.math.mathscript.state.VariableState;
import yapi.math.mathscript.tree.Knot;

import java.math.BigDecimal;

public class FunctionFac extends Knot {

    @Override
    public Knot addKnot(Knot knot) {
        if (getKnots().isEmpty()) {
            getKnots().add(knot);
        }
        return this;
    }

    @Override
    public BigDecimal getResult(VariableState variableState) {
        BigDecimal d = getKnots().get(0).getResult(variableState.copy());
        if ((d + "").endsWith(".0") && d.compareTo(BigDecimal.ZERO) > 0) {
            return new BigDecimal(NumberUtils.factorial(d.toBigInteger()));
        }
        throw new MathScriptException("Factorial not defined for negative numbers or non integers");
    }

    @Override
    public String toString(int i) {
        return "fac\n" + " ".repeat(i) + getKnots().get(0).toString(i + 1);
    }
}