// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.calculator.scientific;

import yapi.math.calculator.scientific.function.InfixFunction;
import yapi.math.calculator.scientific.implementedFunctions.Power;
import yapi.math.calculator.scientific.operator.Operator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

public class OperatorList {

    private static Map<String, Operator> defaultInternalOperatorMap = new HashMap<>();

    static {
        addDefaultInternalOperator(new Operator() {
            @Override
            public String operator() {
                return "+";
            }

            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2, MathContext mathContext) {
                return v1.add(v2, mathContext);
            }
        });
        addDefaultInternalOperator(new Operator() {
            @Override
            public String operator() {
                return "-";
            }

            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2, MathContext mathContext) {
                return v1.subtract(v2, mathContext);
            }
        });
        addDefaultInternalOperator(new Operator() {
            @Override
            public String operator() {
                return "*";
            }

            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2, MathContext mathContext) {
                return v1.multiply(v2, mathContext);
            }
        });
        addDefaultInternalOperator(new Operator() {
            @Override
            public String operator() {
                return "/";
            }

            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2, MathContext mathContext) {
                return v1.divide(v2, mathContext);
            }
        });

        addDefaultInternalOperator(new InfixFunction("^", new Power()));
    }

    private static void addDefaultInternalOperator(Operator operator) {
        defaultInternalOperatorMap.put(operator.operator(), operator);
    }

    private static Map<String, Operator> defaultOperatorMap = new HashMap<>();
    private Map<String, Operator> operatorMap = new HashMap<>();

    public static void addDefaultOperator(Operator operator) {
        defaultOperatorMap.put(operator.operator(), operator);
    }

    public void addOperator(Operator operator) {
        operatorMap.put(operator.operator(), operator);
    }

    public boolean hasOperator(String operator) {
        return operatorMap.containsKey(operator) || defaultOperatorMap.containsKey(operator) || defaultInternalOperatorMap.containsKey(operator);
    }

    public Operator getOperator(String operator) {
        if (!hasOperator(operator)) {
            return null;
        }

        if (operatorMap.containsKey(operator)) {
            return operatorMap.get(operator);
        }
        if (defaultOperatorMap.containsKey(operator)) {
            return defaultOperatorMap.get(operator);
        }
        if (defaultInternalOperatorMap.containsKey(operator)) {
            return defaultInternalOperatorMap.get(operator);
        }
        return null;
    }

}