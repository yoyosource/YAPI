// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.yapion.value;

import yapi.internal.runtimeexceptions.NotNullPointerException;
import yapi.manager.yapion.YAPIONType;

import java.math.BigDecimal;
import java.math.BigInteger;

public class YAPIONValue extends YAPIONType {

    public static final String VALUE_BOOLEAN = "boolean";
    public static final String VALUE_NULL = "null";
    public static final String VALUE_INTEGER = "integer";
    public static final String VALUE_LONG = "long";
    public static final String VALUE_BIGINTEGER = "biginteger";
    public static final String VALUE_FLOAT = "float";
    public static final String VALUE_DOUBLE = "double";
    public static final String VALUE_BIGDECIMAL = "bigdecimal";
    public static final String VALUE_STRING = "string";
    public static final String VALUE_CHARACTER = "character";
    private String value;

    public YAPIONValue(String s) {
        value = s;
    }

    public YAPIONValue(Object object, boolean b) {
        value = object.toString();
        if (object instanceof String) {
            if (value.equals("true") || value.equals("false") || value.matches("[0-9]+(\\.[0-9]+)?")) {
                value = '"' + object.toString() + '"';
            }
        }
    }

    public String getValueType() {
        if (value.equals("true") || value.equals("false")) {
            return VALUE_BOOLEAN;
        }
        if (value.equals("null")) {
            return VALUE_NULL;
        }
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return VALUE_STRING;
        }
        if (value.startsWith("'") && value.endsWith("'")) {
            if (value.length() == 3) {
                return VALUE_CHARACTER;
            } else {
                return VALUE_STRING;
            }
        }
        if (value.matches("\\d+")) {
            try {
                Integer.parseInt(value);
                return VALUE_INTEGER;
            } catch (NumberFormatException e) {
                // Ignored
            }
            try {
                Long.parseLong(value);
                return VALUE_LONG;
            } catch (NumberFormatException e) {
                return VALUE_BIGINTEGER;
            }
        }
        if (value.matches("\\d+I")) {
            try {
                Integer.parseInt(value.substring(0, value.length() - 1));
                return VALUE_INTEGER;
            } catch (NumberFormatException e) {
                return VALUE_NULL;
            }
        }
        if (value.matches("\\d+L")) {
            try {
                Long.parseLong(value.substring(0, value.length() - 1));
                return VALUE_LONG;
            } catch (NumberFormatException e) {
                return VALUE_NULL;
            }
        }
        if (value.matches("\\d+(\\.\\d+)?F")) {
            try {
                Float.parseFloat(value.substring(0, value.length() - 1));
                return VALUE_FLOAT;
            } catch (NumberFormatException e) {
                return VALUE_NULL;
            }
        }
        if (value.matches("\\d+(\\.\\d+)?D")) {
            try {
                Double.parseDouble(value.substring(0, value.length() - 1));
                return VALUE_DOUBLE;
            } catch (NumberFormatException e) {
                return VALUE_NULL;
            }
        }
        if (value.matches("\\d+\\.\\d")) {
            try {
                Float.parseFloat(value);
                return VALUE_FLOAT;
            } catch (NumberFormatException e) {
                // Ignored
            }
            try {
                Double.parseDouble(value);
                return VALUE_DOUBLE;
            } catch (NumberFormatException e) {
                return VALUE_BIGDECIMAL;
            }
        }
        return VALUE_STRING;
    }

    public Boolean getBoolean() {
        if (getValueType().equals(VALUE_BOOLEAN)) {
            return value.equals("true");
        }
        return null;
    }

    public Object getNull() {
        if (getValueType().equals(VALUE_NULL)) {
            return null;
        }
        throw new NotNullPointerException("This is a joke");
    }

    public Integer getInteger() {
        if (getValueType().equals(VALUE_INTEGER)) {
            if (value.endsWith("I")) {
                return Integer.parseInt(value.substring(0, value.length() - 1));
            }
            return Integer.parseInt(value);
        }
        return null;
    }

    public Long getLong() {
        if (getValueType().equals(VALUE_LONG)) {
            if (value.endsWith("L")) {
                return Long.parseLong(value.substring(0, value.length() - 1));
            }
            return Long.parseLong(value);
        }
        return null;
    }

    public BigInteger getBigInteger() {
        if (getValueType().equals(VALUE_BIGINTEGER)) {
            return new BigInteger(value);
        }
        return null;
    }

    public Float getFloat() {
        if (getValueType().equals(VALUE_FLOAT)) {
            if (value.endsWith("F")) {
                return Float.parseFloat(value.substring(0, value.length() - 1));
            }
            return Float.parseFloat(value);
        }
        return null;
    }

    public Double getDouble() {
        if (getValueType().equals(VALUE_DOUBLE)) {
            if (value.endsWith("D")) {
                return Double.parseDouble(value.substring(0, value.length() - 1));
            }
            return Double.parseDouble(value);
        }
        return null;
    }

    public BigDecimal getBigDecimal() {
        if (getValueType().equals(VALUE_BIGDECIMAL)) {
            return new BigDecimal(value);
        }
        return null;
    }

    public String getString() {
        if (getValueType().equals(VALUE_STRING)) {
            if (value.startsWith("\"") && value.endsWith("\"")) {
                return value.substring(1, value.length() - 1);
            }
            return value;
        }
        return null;
    }

    public Character getCharacter() {
        if (getValueType().equals(VALUE_CHARACTER)) {
            return value.charAt(1);
        }
        return null;
    }

    public Object getValue() {
        switch (getValueType()) {
            case VALUE_BOOLEAN:
                return getBoolean();
            case VALUE_INTEGER:
                return getInteger();
            case VALUE_LONG:
                return getLong();
            case VALUE_BIGINTEGER:
                return getBigInteger();
            case VALUE_FLOAT:
                return getFloat();
            case VALUE_DOUBLE:
                return getDouble();
            case VALUE_BIGDECIMAL:
                return getBigDecimal();
            case VALUE_NULL:
                return getNull();
            case VALUE_STRING:
                return getString();
            case VALUE_CHARACTER:
                return getCharacter();
            default:
                return null;
        }
    }

    public String getValueAsJSON() {
        switch (getValueType()) {
            case VALUE_BOOLEAN:
                return getBoolean() + "";
            case VALUE_INTEGER:
                return getInteger() + "";
            case VALUE_LONG:
                return getLong() + "";
            case VALUE_BIGINTEGER:
                return getBigInteger() + "";
            case VALUE_FLOAT:
                return getFloat() + "";
            case VALUE_DOUBLE:
                return getDouble() + "";
            case VALUE_BIGDECIMAL:
                return getBigDecimal() + "";
            case VALUE_NULL:
                return getNull() + "";
            case VALUE_STRING:
                return '"' + getString() + '"';
            case VALUE_CHARACTER:
                return "'" + getCharacter() + "'";
            default:
                return "null";
        }
    }

    @Override
    public String getType() {
        return "YAPIONValue";
    }

    @Override
    public String toString() {
        return '(' + value.replaceAll("([()])", "\\\\$1") + ')';
    }

    public String toHierarchyString(int index) {
        return " ".repeat(2 * index) + toString();
    }

}