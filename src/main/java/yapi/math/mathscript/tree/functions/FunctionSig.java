package yapi.math.mathscript.tree.functions;

import ch.obermuhlner.math.big.BigDecimalMath;
import yapi.math.mathscript.state.VariableState;
import yapi.math.mathscript.tree.Knot;

import java.math.BigDecimal;

public class FunctionSig extends Knot {

    @Override
    public Knot addKnot(Knot knot) {
        if (getKnots().isEmpty()) {
            getKnots().add(knot);
        }
        return this;
    }

    @Override
    public BigDecimal getResult(VariableState variableState) {
        return BigDecimal.ONE.divide(BigDecimal.ONE.add(BigDecimalMath.exp(getKnots().get(0).getResult(variableState.copy()).multiply(BigDecimal.valueOf(-1)), getMathContext())), getMathContext());
    }

    @Override
    public String toString(int i) {
        return "sig\n" + " ".repeat(i) + getKnots().get(0).toString(i + 1);
    }
}
