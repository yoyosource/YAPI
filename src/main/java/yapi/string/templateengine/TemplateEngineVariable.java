// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.string.templateengine;

import java.util.Objects;

public class TemplateEngineVariable {

    private String name = null;
    private Object value = null;

    public TemplateEngineVariable(String name, Object value) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        if (!name.matches("[a-z][a-zA-z0-9_\\-]*")) {
            throw new IllegalArgumentException("Name needs to match '[a-z][a-zA-z0-9_\\-]*'");
        }
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TemplateEngineVariable)) return false;
        TemplateEngineVariable that = (TemplateEngineVariable) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}