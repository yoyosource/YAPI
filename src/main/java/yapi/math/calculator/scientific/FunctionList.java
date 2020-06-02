// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.calculator.scientific;

import yapi.math.calculator.scientific.function.Function;
import yapi.math.calculator.scientific.implementedFunctions.*;

import java.util.HashMap;
import java.util.Map;

public class FunctionList {

    private static Map<String, Function> defaultInternalFunctionMap = new HashMap<>();

    static {
        addDefaultInternalFunction(new Power());
        addDefaultInternalFunction(new Root());
        addDefaultInternalFunction(new Sqrt());
        addDefaultInternalFunction(new Cbrt());

        addDefaultInternalFunction(new Factorial());

        System.out.println(defaultInternalFunctionMap);
    }

    private static void addDefaultInternalFunction(Function function) {
        defaultInternalFunctionMap.put(function.name() + ":" + function.argCount(), function);
    }

    private static Map<String, Function> defaultFunctionMap = new HashMap<>();
    private Map<String, Function> functionMap = new HashMap<>();

    public static void addDefaultFunction(Function function) {
        defaultFunctionMap.put(function.name() + ":" + function.argCount(), function);
    }

    public void addFunction(Function function) {
        functionMap.put(function.name() + ":" + function.argCount(), function);
    }


    public boolean hasFunctionLazy(String name) {
        if (hasFunction(name)) {
            return true;
        }
        for (Map.Entry<String, Function> e : functionMap.entrySet()) {
            if (e.getKey().startsWith(name)) {
                return true;
            }
        }
        for (Map.Entry<String, Function> e : defaultFunctionMap.entrySet()) {
            if (e.getKey().startsWith(name)) {
                return true;
            }
        }
        for (Map.Entry<String, Function> e : defaultInternalFunctionMap.entrySet()) {
            if (e.getKey().startsWith(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasFunction(String name) {
        return functionMap.containsKey(name) || defaultFunctionMap.containsKey(name) || defaultInternalFunctionMap.containsKey(name);
    }

    public Function getFunction(String name) {
        if (!hasFunction(name)) {
            return null;
        }

        if (functionMap.containsKey(name)) {
            return functionMap.get(name);
        }
        if (defaultFunctionMap.containsKey(name)) {
            return defaultFunctionMap.get(name);
        }
        if (defaultInternalFunctionMap.containsKey(name)) {
            return defaultInternalFunctionMap.get(name);
        }
        return null;
    }

}