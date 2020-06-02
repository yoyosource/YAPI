// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.calculator.scientific.function;

import yapi.math.calculator.scientific.function.Function;
import yapi.math.calculator.scientific.operator.Operator;

import java.math.BigDecimal;
import java.math.MathContext;

public class InfixFunction implements Operator {

    private String operator;
    private Function function;

    public InfixFunction(String operator, Function function) {
        if (function.argCount() != 2) {
            throw new IllegalArgumentException();
        }

        this.operator = operator;
        this.function = function;
    }

    @Override
    public String operator() {
        return operator;
    }

    @Override
    public BigDecimal eval(BigDecimal v1, BigDecimal v2, MathContext mathContext) {
        BigDecimal[] bigDecimals = new BigDecimal[]{v1, v2};
        return function.eval(bigDecimals, mathContext);
    }
}