package yapi.math.mathscript.tree.functions;

import ch.obermuhlner.math.big.BigDecimalMath;
import yapi.math.mathscript.state.VariableState;
import yapi.math.mathscript.tree.Knot;

import java.math.BigDecimal;

public class FunctionGauss extends Knot {

    @Override
    public Knot addKnot(Knot knot) {
        if (getKnots().isEmpty()) {
            getKnots().add(knot);
        }
        return this;
    }

    @Override
    public BigDecimal getResult(VariableState variableState) {
        BigDecimal num1 = BigDecimal.ONE.divide(BigDecimal.valueOf(2 * Math.PI).sqrt(getMathContext()), getMathContext());
        BigDecimal num2 = BigDecimalMath.exp(BigDecimalMath.pow(getKnots().get(0).getResult(variableState.copy()), BigDecimal.valueOf(2), getMathContext()).multiply(BigDecimal.valueOf(-1)), getMathContext()).divide(BigDecimal.valueOf(2), getMathContext());
        return num1.multiply(num2);
    }

    @Override
    public String toString(int i) {
        return "gauss\n" + " ".repeat(i) + getKnots().get(0).toString(i + 1);
    }
}
