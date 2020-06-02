// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.calculator.scientific.token;

public enum TokenType {

    Variable, Number,
    Operator_Generic, Operator, Operator_Suffix,
    Bracket_Open, Bracket_Close,
    Function_Open, Function_Close, Function_Separator, Function,

    Unknown

}