// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.calculator.scientific.implementedFunctions;

import yapi.math.NumberUtils;
import yapi.math.calculator.scientific.function.AbstractFunction;

import java.math.BigDecimal;
import java.math.MathContext;

public class Factorial extends AbstractFunction {

    public Factorial() {
        super("fac", 1);
    }

    @Override
    public BigDecimal eval(BigDecimal[] args, MathContext mathContext) {
        return new BigDecimal(NumberUtils.fastFactorial(args[0].toBigInteger()));
    }
}