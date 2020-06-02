// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.calculator.scientific;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Context {

    private Map<String, BigDecimal> contextMap = new HashMap<>();

    public Context addVariable(String variable, BigDecimal value) {
        if (hasVariable(variable)) {
            contextMap.replace(variable, value);
        } else {
            contextMap.put(variable, value);
        }
        return this;
    }

    public Context clearContext() {
        contextMap.clear();
        return this;
    }

    public boolean hasVariable(String variable) {
        return contextMap.containsKey(variable);
    }

    public BigDecimal getVariable(String variable) {
        if (!hasVariable(variable)) {
            return null;
        }
        return contextMap.get(variable);
    }

}