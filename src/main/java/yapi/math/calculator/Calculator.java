// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.calculator;

import yapi.internal.exceptions.math.CalculatorException;
import yapi.math.calculator.fractions.CalculatorFractions;
import yapi.math.Fraction;
import yapi.math.calculator.normal.CalculatorNormal;

public class Calculator {

    private String s;

    public Calculator(String calculate) {
        this.s = calculate;
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator("(2+3)*3+(0-3*2)");
        System.out.println(calculator.calc().encodeNumber());
    }

    public Fraction calc() {
        Fraction fraction;
        if (s.contains("^") || s.contains("root") || s.contains("sin") || s.contains("cos") || s.contains("tan") || s.contains("asin") || s.contains("acos") || s.contains("atan") || s.contains("log") || s.contains("gauss") || s.contains("sigmoid") || s.contains("sig")) {
            Object o = calcNormal();
            if (o instanceof Integer) {
                fraction = new Fraction((Integer)o);
            } else if (o instanceof Double) {
                fraction = new Fraction((Double)o);
            } else if (o instanceof Long) {
                fraction = new Fraction((Long) o);
            } else {
                fraction = null;
            }
        } else {
            fraction = calcFraction();
        }
        return fraction;
    }

    public Fraction calcFraction() {
        if (s.contains("^") || s.contains("root") || s.contains("sin") || s.contains("cos") || s.contains("tan") || s.contains("asin") || s.contains("acos") || s.contains("atan") || s.contains("log") || s.contains("gauss") || s.contains("sigmoid") || s.contains("sig")) {
            throw new CalculatorException("Invalid calculations for FractionCalculator");
        }
        try {
            return new CalculatorFractions(s).getResult();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CalculatorException("Unexpected exception got thrown: " + e.getLocalizedMessage());
        }
    }

    public Object calcNormal() {
        try {
            Object n = new CalculatorNormal(s).calculate();
            if (n == null) {
                throw new CalculatorException("No result");
            }
            return n;
        } catch (Exception e) {
            throw new CalculatorException("Unexpected exception got thrown: " + e.getLocalizedMessage());
        }
    }

}