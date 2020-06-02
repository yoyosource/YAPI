// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.calculator.scientific.function;

import java.math.BigDecimal;
import java.math.MathContext;

public abstract class AbstractFunction implements Function {

    private final String name;
    private final int argCount;

    public AbstractFunction(String name, int argCount) {
        this.name = name;
        this.argCount = argCount;
    }

    @Override
    public final String name() {
        return name;
    }

    @Override
    public final int argCount() {
        return argCount;
    }

    @Override
    public abstract BigDecimal eval(BigDecimal[] args, MathContext mathContext);

}