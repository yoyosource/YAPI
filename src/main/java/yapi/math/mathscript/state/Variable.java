package yapi.math.mathscript.state;

import yapi.math.calculator.fractions.Fraction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

public class Variable {

    private String name = "";
    private BigDecimal value = BigDecimal.ZERO;
    private boolean constant = false;

    public Variable(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public Variable setValue(double value) {
        if (constant) {
            return this;
        }
        this.value = BigDecimal.valueOf(value);
        return this;
    }

    public Variable setValue(BigInteger value) {
        if (constant) {
            return this;
        }
        this.value = new BigDecimal(value);
        return this;
    }

    public Variable setValue(BigDecimal value) {
        if (constant) {
            return this;
        }
        this.value = value;
        return this;
    }

    Variable setConstant() {
        constant = true;
        return this;
    }

    @Override
    public String toString() {
        if (constant) {
            return "Constant{" +
                    "name='" + name + '\'' +
                    ", value=" + value +
                    '}';
        } else {
            return "Variable{" +
                    "name='" + name + '\'' +
                    ", value=" + value +
                    '}';
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Variable)) return false;
        Variable variable = (Variable) o;
        return Objects.equals(name, variable.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
