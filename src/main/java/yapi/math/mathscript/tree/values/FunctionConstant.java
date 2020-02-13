// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.mathscript.tree.values;

import yapi.math.mathscript.state.VariableState;
import yapi.math.mathscript.tree.Knot;

import java.math.BigDecimal;

public class FunctionConstant extends Knot {

    private BigDecimal fraction;

    public FunctionConstant(BigDecimal fraction) {
        this.fraction = fraction;
    }

    @Override
    public BigDecimal getResult(VariableState variableState) {
        return fraction;
    }

    @Override
    public String toString(int i) {
        StringBuilder st = new StringBuilder();
        st.append("constant: " + fraction);
        return st.toString();
    }

}