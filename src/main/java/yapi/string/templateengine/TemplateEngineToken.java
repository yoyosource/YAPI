// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.string.templateengine;

public class TemplateEngineToken {

    private static boolean compact = false;

    private static final String FLOAT = "Float";
    private static final String DOUBLE = "Double";

    public static void toggleCompact() {
        compact = !compact;
    }

    public static void setCompact(boolean compact) {
        TemplateEngineToken.compact = compact;
    }

    private Object value = null;
    private String key = "";

    public TemplateEngineToken(Object value, String key) {
        this.value = value;
        if (key.equals("VAR")) {
            this.key = key + ":" + setKey(value, true);
        } else {
            this.key = setKey(value);
        }
    }

    private String setKey(Object value) {
        return setKey(value, false);
    }

    private String setKey(Object value, boolean variable) {
        String s;
        if (value instanceof Integer) {
            s = "Integer";
        } else if (value instanceof Double) {
            s = DOUBLE;
        } else if (value instanceof Float) {
            s = FLOAT;
        } else if (value instanceof Long) {
            s = "Long";
        } else if (value instanceof String) {
            s = "String";
        } else {
            s = value.getClass().getName();
        }
        if (variable) {
            return s;
        }
        if (!(value instanceof String)) {
            return s;
        }

        return setKey(value.toString());
    }

    private String setKey(String v) {
        String s;
        if (v.matches("-?\\d+")) {
            try {
                Integer.parseInt(v);
                s = "Integer";
            } catch (NumberFormatException e) {
                s = "Long";
            }
        } else if (v.matches("-?\\d+L")) {
            this.value = v.substring(0, v.length() - 1);
            s = "Long";
        } else if (v.matches("-?\\d+\\.\\d+")) {
            try {
                Float.parseFloat(v);
                s = FLOAT;
            } catch (NumberFormatException e) {
                s = DOUBLE;
            }
        } else if (v.matches("-?\\d+(\\.\\d+)?F")) {
            this.value = v.substring(0, v.length() - 1);
            s = FLOAT;
        } else if (v.matches("-?\\d+(\\.\\d+)?D")) {
            this.value = v.substring(0, v.length() - 1);
            s = DOUBLE;
        }
        else if (v.equals("(") || v.equals(")") || v.equals("[") || v.equals("]")) {
            s = "Bracket";
        }
        else if (v.equals(".") || v.equals(":") || v.equals(";")) {
            s = "Seperator";
        } else if (v.equals("?")) {
            s = "Ternary";
        }
        else {
            s = "CODE";
        }

        return s;
    }

    public Object getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        if (compact) {
            return "'" + value + "'(" + key + ")";
        }
        return "TemplateEngineToken{" +
                "value=" + value +
                ", key='" + key + '\'' +
                '}';
    }
}