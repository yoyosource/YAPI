// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.calculator.scientific.implementedFunctions;

import ch.obermuhlner.math.big.BigDecimalMath;
import yapi.math.calculator.scientific.function.AbstractFunction;

import java.math.BigDecimal;
import java.math.MathContext;

public class Sqrt extends AbstractFunction {

    public Sqrt() {
        super("sqrt", 1);
    }

    @Override
    public BigDecimal eval(BigDecimal[] args, MathContext mathContext) {
        return BigDecimalMath.root(args[0], BigDecimal.valueOf(2), mathContext);
    }
}