// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.calculator.scientific.function;

import yapi.math.calculator.scientific.operator.UnaryOperator;

import java.math.BigDecimal;
import java.math.MathContext;

public class SuffixFunction implements UnaryOperator {

    private String operator;
    private Function function;

    public SuffixFunction(String operator, Function function) {
        if (function.argCount() != 1) {
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
    public BigDecimal eval(BigDecimal v1, MathContext mathContext) {
        BigDecimal[] bigDecimals = new BigDecimal[]{v1};
        return function.eval(bigDecimals, mathContext);
    }
}