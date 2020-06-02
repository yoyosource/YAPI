// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.calculator.scientific.function;

import java.math.BigDecimal;
import java.math.MathContext;

public interface Function {

    String name();

    int argCount();

    BigDecimal eval(BigDecimal[] args, MathContext mathContext);

}