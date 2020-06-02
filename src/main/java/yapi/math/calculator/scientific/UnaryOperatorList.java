// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.calculator.scientific;

import yapi.math.calculator.scientific.function.SuffixFunction;
import yapi.math.calculator.scientific.implementedFunctions.Factorial;
import yapi.math.calculator.scientific.operator.UnaryOperator;

import java.util.HashMap;
import java.util.Map;

public class UnaryOperatorList {

    private static Map<String, UnaryOperator> defaultInternalOperatorMap = new HashMap<>();

    static {
        addDefaultInternalOperator(new SuffixFunction("!", new Factorial()));
    }

    private static void addDefaultInternalOperator(UnaryOperator unaryOperator) {
        defaultInternalOperatorMap.put(unaryOperator.operator(), unaryOperator);
    }

    private static Map<String, UnaryOperator> defaultOperatorMap = new HashMap<>();
    private Map<String, UnaryOperator> operatorMap = new HashMap<>();

    public static void addDefaultOperator(UnaryOperator unaryOperator) {
        defaultOperatorMap.put(unaryOperator.operator(), unaryOperator);
    }

    public void addOperator(UnaryOperator unaryOperator) {
        operatorMap.put(unaryOperator.operator(), unaryOperator);
    }

    public boolean hasOperator(String operator) {
        return operatorMap.containsKey(operator) || defaultOperatorMap.containsKey(operator) || defaultInternalOperatorMap.containsKey(operator);
    }

    public UnaryOperator getOperator(String operator) {
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