package yapi.math.mathscript.tree.functions;

import ch.obermuhlner.math.big.BigDecimalMath;
import yapi.math.mathscript.state.VariableState;
import yapi.math.mathscript.tree.Knot;

import java.math.BigDecimal;
import java.math.MathContext;

public class FunctionRound extends Knot {

    @Override
    public Knot addKnot(Knot knot) {
        if (getKnots().size() < 2) {
            getKnots().add(knot);
        }
        return this;
    }

    @Override
    public BigDecimal getResult(VariableState variableState) {
        if (getKnots().size() == 1) {
            return BigDecimalMath.round(getKnots().get(0).getResult(variableState.copy()), new MathContext(0));
        } else {
            BigDecimal d = getKnots().get(0).getResult(variableState.copy());
            return BigDecimalMath.acos(getKnots().get(0).getResult(variableState.copy()).multiply(d), getMathContext()).divide(d);
        }
    }

    @Override
    public String toString(int i) {
        if (getKnots().size() == 1) {
            return "round\n" + " ".repeat(i) + getKnots().get(0).toString(i + 1);
        } else {
            return "round\n" + " ".repeat(i) + getKnots().get(1).toString(i + 1);
        }
    }
}
