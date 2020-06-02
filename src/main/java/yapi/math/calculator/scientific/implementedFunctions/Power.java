// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.calculator.scientific.implementedFunctions;

import ch.obermuhlner.math.big.BigDecimalMath;
import yapi.math.calculator.scientific.function.AbstractFunction;

import java.math.BigDecimal;
import java.math.MathContext;

public class Power extends AbstractFunction {

    public Power() {
        super("pow", 2);
    }

    @Override
    public BigDecimal eval(BigDecimal[] args, MathContext mathContext) {
        return BigDecimalMath.pow(args[0], args[1], mathContext);
    }
}