package yapi.math.mathscript.tree.functions;

import ch.obermuhlner.math.big.BigDecimalMath;
import yapi.math.mathscript.state.VariableState;
import yapi.math.mathscript.tree.Knot;

import java.math.BigDecimal;

public class FunctionSinH extends Knot {

    @Override
    public Knot addKnot(Knot knot) {
        if (getKnots().isEmpty()) {
            getKnots().add(knot);
        }
        return this;
    }

    @Override
    public BigDecimal getResult(VariableState variableState) {
        return BigDecimalMath.sinh(getKnots().get(0).getResult(variableState.copy()), getMathContext());
    }

    @Override
    public String toString(int i) {
        return "sinh\n" + " ".repeat(i) + getKnots().get(0).toString(i + 1);
    }

}
