package yapi.math.mathscript.tree.functions;

import ch.obermuhlner.math.big.BigDecimalMath;
import yapi.math.mathscript.state.VariableState;
import yapi.math.mathscript.tree.Knot;

import java.math.BigDecimal;
import java.math.MathContext;

public class FunctionATan extends Knot {

    @Override
    public Knot addKnot(Knot knot) {
        if (getKnots().isEmpty()) {
            getKnots().add(knot);
        }
        return this;
    }

    @Override
    public BigDecimal getResult(VariableState variableState) {
        return BigDecimalMath.atan(getKnots().get(0).getResult(variableState.copy()), getMathContext());
    }

    @Override
    public String toString(int i) {
        return "atan\n" + " ".repeat(i) + getKnots().get(0).toString(i + 1);
    }
}
