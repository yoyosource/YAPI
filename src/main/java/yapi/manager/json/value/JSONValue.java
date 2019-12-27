package yapi.manager.json.value;

import yapi.exceptions.JSONException;
import yapi.manager.json.JSONType;

import java.math.BigDecimal;

public class JSONValue extends JSONType {

    private boolean isNull    = false;
    private boolean isBoolean = false;
    private boolean isNumber  = false;
    private boolean isString  = false;

    private Object  value     = null;

    public JSONValue(String input) {
        if (input.equals("true") || input.equals("false")) {
            isBoolean = true;
            value = input.equals("true");
        }
        if (input.equals("null")) {
            isNull = true;
            value = null;
        }
        if (input.startsWith("\"") && input.endsWith("\"")) {
            isString = true;
            value = input.substring(1, input.length() - 1);
        }
        try {
            BigDecimal bigDecimal = new BigDecimal(input);
            isNumber = true;
            value = bigDecimal;
        } catch (NumberFormatException e) {

        }
    }

    public boolean isUnknown() {
        return !isNull && !isBoolean && !isNumber && !isString;
    }

    public boolean isNull() {
        return isNull;
    }

    public Object getNull() {
        return null;
    }

    public boolean isBoolean() {
        return isBoolean;
    }

    public Boolean getBoolean() {
        if (isBoolean) {
            return (boolean) value;
        } else {
            return null;
        }
    }

    public boolean isNumber() {
        return isNumber;
    }

    public BigDecimal getNumber() {
        if (isNumber) {
            return (BigDecimal)value;
        } else {
            return null;
        }
    }

    public boolean isString() {
        return isString;
    }

    public String getString() {
        if (isString) {
            return (String)value;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        if (isNull) {
            return "null";
        } else if (isBoolean) {
            return value.toString();
        } else if (isString) {
            return "\"" + value.toString() + "\"";
        } else if (isNumber) {
            return value.toString();
        } else {
            return "null";
        }
    }

}
