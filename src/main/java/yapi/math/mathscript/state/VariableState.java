// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.mathscript.state;

import yapi.exceptions.math.MathScriptException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VariableState {

    private List<Variable> variables = new ArrayList<>();

    public VariableState() {
        createConstants();
    }

    private VariableState(List<Variable> variables) {
        this.variables.addAll(variables);
    }

    private VariableState(List<Variable> variables, Variable variable) {
        this.variables.addAll(variables);
        if (this.variables.contains(variable)) {
            this.variables.get(this.variables.indexOf(variable)).setValue(variable.getValue());
        } else {
            this.variables.add(variable);
        }
    }

    private void createConstants() {
        variables.add(new Variable("pi").setValue(Math.PI).setConstant());
        variables.add(new Variable("π").setValue(Math.PI).setConstant());
        variables.add(new Variable("e").setValue(Math.E).setConstant());
        variables.add(new Variable("c").setValue(299792458).setConstant());
        variables.add(new Variable("golden").setValue((1 + Math.sqrt(5)) / 2).setConstant());
        variables.add(new Variable("goldenratio").setValue((1 + Math.sqrt(5)) / 2).setConstant());
    }

    public VariableState copy() {
        return new VariableState(variables);
    }

    public VariableState add(Variable variable) {
        return new VariableState(variables, variable);
    }

    public boolean hasVariable(String s) {
        return variables.contains(new Variable(s));
    }

    public boolean hasVariable(Variable variable) {
        return variables.contains(variable);
    }

    public BigDecimal getVariable(String s) {
        if (!hasVariable(s)) {
            throw new MathScriptException("Variable is not defined");
        }
        return variables.get(variables.indexOf(new Variable(s))).getValue();
    }

    public BigDecimal getVariable(Variable variable) {
        if (!hasVariable(variable)) {
            throw new MathScriptException("Variable is not defined");
        }
        return variables.get(variables.indexOf(variable)).getValue();
    }

    @Override
    public String toString() {
        return "VariableState{" +
                "variables=" + variables +
                '}';
    }
}