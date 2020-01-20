package yapi.math.mathscript.tree.functions;

import yapi.math.mathscript.state.VariableState;
import yapi.math.mathscript.tree.Knot;

import java.math.BigDecimal;

public class FunctionMod extends Knot {

    @Override
    public Knot addKnot(Knot knot) {
        if (getKnots().size() < 2) {
            getKnots().add(knot);
        }
        return this;
    }

    @Override
    public BigDecimal getResult(VariableState variableState) {
        BigDecimal start = getKnots().get(0).getResult(variableState.copy());
        BigDecimal stop  = getKnots().get(1).getResult(variableState.copy());
        while (start.compareTo(stop) > 0) {
            start = stop.subtract(stop);
        }
        return start;
    }

    @Override
    public String toString(int i) {
        StringBuilder st = new StringBuilder();
        st.append("mod" + "\n");
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
