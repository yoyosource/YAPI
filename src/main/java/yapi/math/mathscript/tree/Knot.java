// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.mathscript.tree;

import yapi.math.mathscript.state.VariableState;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

public class Knot {

    private static MathContext mathContext = new MathContext(200);
    private final List<Knot> knots = new ArrayList<>();

    public final MathContext getMathContext() {
        return mathContext;
    }

    public static void setMathContext(int precision) {
        mathContext = new MathContext(precision);
    }

    public final List<Knot> getKnots() {
        return knots;
    }

    public final int knotCount() {
        return knots.size();
    }

    public Knot addKnot(Knot knot) {
        knots.add(knot);
        return this;
    }

    public BigDecimal getResult() {
        return getResult(new VariableState());
    }

    public BigDecimal getResult(VariableState variableState) {
        return BigDecimal.ZERO;
    }

    public String toString() {
        return toString(1);
    }

    public String toString(int i) {
        StringBuilder st = new StringBuilder();
        st.append("???" + "\n");
        for (Knot knot : knots) {
            st.append(" ".repeat(i));
            st.append(knot.toString(i + 1));
            st.append("\n");
        }
        return st.toString();
    }

}