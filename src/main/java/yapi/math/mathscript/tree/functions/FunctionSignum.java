// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.mathscript.tree.functions;

import yapi.math.mathscript.state.VariableState;
import yapi.math.mathscript.tree.Knot;

import java.math.BigDecimal;

public class FunctionSignum extends Knot {

    @Override
    public Knot addKnot(Knot knot) {
        if (getKnots().isEmpty()) {
            getKnots().add(knot);
        }
        return this;
    }

    @Override
    public BigDecimal getResult(VariableState variableState) {
        return BigDecimal.valueOf(getKnots().get(0).getResult(variableState.copy()).signum());
    }

    @Override
    public String toString(int i) {
        return "signum\n" + " ".repeat(i) + getKnots().get(0).toString(i + 1);
    }
}