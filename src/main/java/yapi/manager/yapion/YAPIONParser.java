// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.yapion;

import yapi.internal.exceptions.YAPIException;
import yapi.internal.exceptions.objectnotation.YAPIONException;
import yapi.manager.json.JSONVariable;
import yapi.manager.json.value.JSONArray;
import yapi.manager.json.value.JSONObject;
import yapi.manager.json.value.JSONValue;
import yapi.manager.yapion.value.YAPIONArray;
import yapi.manager.yapion.value.YAPIONObject;
import yapi.manager.yapion.value.YAPIONValue;

import java.util.List;

public class YAPIONParser {

    private static int escapes = 0;

    public synchronized static JSONObject toJSON(YAPIONObject yapionObject) {
        JSONObject jsonObject = new JSONObject();
        List<String> keys = yapionObject.getKeys();
        for (String s : keys) {
            YAPIONType yapionType = yapionObject.getVariable(s).getYapionType();
            if (yapionType instanceof YAPIONValue) {
                jsonObject.add(new JSONVariable(s, toJSON((YAPIONValue) yapionType)));
            } else if (yapionType instanceof YAPIONArray) {
                jsonObject.add(new JSONVariable(s, toJSON((YAPIONArray) yapionType)));
            } else if (yapionType instanceof YAPIONObject) {
                jsonObject.add(new JSONVariable(s, toJSON((YAPIONObject) yapionType)));
            }
        }
        return jsonObject;
    }

    private static JSONArray toJSON(YAPIONArray yapionArray) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < yapionArray.size(); i++) {
            YAPIONType yapionType = yapionArray.get(i);
            if (yapionType instanceof YAPIONValue) {
                jsonArray.add(toJSON((YAPIONValue) yapionType));
            } else if (yapionType instanceof YAPIONArray) {
                jsonArray.add(toJSON((YAPIONArray) yapionType));
            } else if (yapionType instanceof YAPIONObject) {
                jsonArray.add(toJSON((YAPIONObject) yapionType));
            }
        }
        return jsonArray;
    }

    private static JSONValue toJSON(YAPIONValue yapionValue) {
        return new JSONValue(yapionValue.getValueAsJSON());
    }

    public static void main(String[] args) {
        YAPIONObject yapionObject = parseObject("{hello[true,false,null,Hello]}");
        YAPIONType yapionType = yapionObject.getArray("hello").get(3);
        if (yapionType instanceof YAPIONValue) {
            System.out.println(((YAPIONValue) yapionType).getValueType());
        }
    }

    /**
     * A new YAPION structure starts with '{' and ends with '}'.
     * A key starts with any character that is a non whitespace character and ends with '(', '[' or '{'.
     * The normal bracket indicates a value like a string, character, number or boolean.
     * The square bracket indicates a array out of values. In an array every value is separated by a comma and can be followed by a space.
     * The curly bracket indicates a object out of key-value pares.
     *
     * Example
     *   {}
     *   {Test(Hello World)}
     *   {TRUE(true)FALSE(false)}
     *   {Number(23746)}
     *   {Number(23746.827364)}
     *   {Test[10, 9, 8]}
     *   {Test{Test(Hello World)}}
     *
     * @since Version 1.1
     *
     * @param yapion
     * @return
     */
    public static synchronized YAPIONObject parseObject(String yapion) {
        char[] chars = yapion.toCharArray();
        if (chars[0] != '{' || chars[chars.length - 1] != '}') {
            throw new YAPIONException("No object input\n" + createErrorMessage(chars, 0, chars.length));
        }
        try {
            return parseObject(yapion.substring(1, yapion.length() - 1).toCharArray());
        } catch (YAPIONException e) {
            throw new YAPIException(e.getMessage());
        }
    }

    public static synchronized YAPIONArray parseArray(String yapion) {
        char[] chars = yapion.toCharArray();
        if (chars[0] != '[' || chars[chars.length - 1] != ']') {
            throw new YAPIONException("No object input\n" + createErrorMessage(chars, 0, chars.length));
        }
        try {
            return parseArray(yapion.substring(1, yapion.length() - 1).toCharArray(), 0, yapion.length() - 1);
        } catch (YAPIONException e) {
            throw new YAPIException(e.getMessage());
        }
    }

    private static YAPIONObject parseObject(char[] chars) {
        YAPIONObject yapionObject = new YAPIONObject();

        boolean escaped = false;
        StringBuilder key = new StringBuilder();
        int i = 0;
        while (i < chars.length) {
            if (!escaped && chars[i] == '\\') {
                escaped = true;
                i++;
                continue;
            }

            if (!escaped && chars[i] == '(') {
                String s = getValue(chars, i);
                i += s.length() + 1 + escapes;
                yapionObject.add(new YAPIONVariable(key.toString(), new YAPIONValue(s)));
                key = new StringBuilder();
            } else if (!escaped && chars[i] == '[') {
                String s = getValue(chars, i);
                yapionObject.add(new YAPIONVariable(key.toString(), parseArray(chars, i + 1, i + s.length() + 1)));
                i += s.length() + 1;
                key = new StringBuilder();
            } else if (!escaped && chars[i] == '{') {
                String s = getValue(chars, i);
                i += s.length() + 1;
                yapionObject.add(new YAPIONVariable(key.toString(), parseObject(s.toCharArray())));
                key = new StringBuilder();
            } else if ((chars[i] == ' ' || chars[i] == '\t' || chars[i] == '\n' || chars[i] == '\b') && key.length() > 0) {
                key.append(chars[i]);
            } else if (!(chars[i] == ' ' || chars[i] == '\t' || chars[i] == '\n' || chars[i] == '\b')) {
                key.append(chars[i]);
            }
            escaped = false;
            i++;
        }

        return yapionObject;
    }

    private static String createErrorMessage(char[] chars, int index, int indexEnd) {
        index--;
        indexEnd--;
        StringBuilder message = new StringBuilder();
        StringBuilder error   = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '\n') {
                message.append("\\n");
                if (i == index) {
                    error.append('^').append('~');
                } else if (i > index && i <= indexEnd) {
                    error.append('~').append('~');
                } else {
                    error.append(' ').append(' ');
                }
            } else if (chars[i] == '\t') {
                message.append("\\t");
                if (i == index) {
                    error.append('^').append('~');
                } else if (i > index && i <= indexEnd) {
                    error.append('~').append('~');
                } else {
                    error.append(' ').append(' ');
                }
            } else if (chars[i] == '\b') {
                message.append("\\b");
                if (i == index) {
                    error.append('^').append('~');
                } else if (i > index && i <= indexEnd) {
                    error.append('~').append('~');
                } else {
                    error.append(' ').append(' ');
                }
            } else {
                message.append(chars[i]);
                if (i == index) {
                    error.append('^');
                } else if (i > index && i <= indexEnd) {
                    error.append('~');
                } else {
                    error.append(' ');
                }
            }
        }
        return message + "\n" + error;
    }

    private static String getValue(char[] chars, int index) {
        escapes = 0;
        StringBuilder st = new StringBuilder();
        int bracket = 1;
        int type;
        if (chars[index] == '(') {
            type = 0;
        } else if (chars[index] == '[') {
            type = 1;
        } else if (chars[index] == '{') {
            type = 2;
        } else if (chars[index] == '<') {
            type = 3;
        } else {
            throw new YAPIONException("Unknown starting bracket at " + index + "\n" + createErrorMessage(chars, index, index));
        }
        index++;
        boolean escaped = false;
        for (int i = index; i < chars.length; i++) {
            if (!escaped && chars[i] == '\\') {
                escapes++;
                escaped = true;
                if (type != 0) {
                    st.append('\\');
                }
                continue;
            }
            if (type == 0 && !escaped) {
                if (chars[i] == '(') {
                    bracket++;
                }
                if (chars[i] == ')') {
                    bracket--;
                }
            } else if (type == 1 && !escaped) {
                if (chars[i] == '[') {
                    bracket++;
                }
                if (chars[i] == ']') {
                    bracket--;
                }
            } else if (type == 2 && !escaped) {
                if (chars[i] == '{') {
                    bracket++;
                }
                if (chars[i] == '}') {
                    bracket--;
                }
            } else if (!escaped) {
                if (chars[i] == '<') {
                    bracket++;
                }
                if (chars[i] == '>') {
                    bracket--;
                }
            }
            if (bracket == 0) {
                return st.toString();
            } else {
                st.append(chars[i]);
            }
            escaped = false;
        }
        throw new YAPIONException("Missing closing bracket for opening bracket at " + index + "\n" + createErrorMessage(chars, index, chars.length));
    }

    private static YAPIONArray parseArray(char[] chars, int start, int end) {
        YAPIONArray yapionArray = new YAPIONArray();
        boolean escaped = false;
        StringBuilder st = new StringBuilder();
        for (int i = start; i < end; i++) {
            if (!escaped && chars[i] == '\\') {
                escaped = true;
                continue;
            }
            if (!escaped && chars[i] == ',') {
                String s = st.toString();
                st = new StringBuilder();
                yapionArray.add(parseArrayValue(s));
            } else if ((chars[i] == ' ' || chars[i] == '\t' || chars[i] == '\n' || chars[i] == '\b') && st.length() > 0) {
                st.append(chars[i]);
            } else if (!(chars[i] == ' ' || chars[i] == '\t' || chars[i] == '\n' || chars[i] == '\b')) {
                st.append(chars[i]);
            }
            escaped = false;
        }
        if (st.length() != 0) {
            yapionArray.add(parseArrayValue(st.toString()));
        }
        return yapionArray;
    }

    private static YAPIONType parseArrayValue(String s) {
        YAPIONType yapionType;
        if (s.length() == 0) {
            return null;
        }
        char[] chars = s.substring(1, s.length() - 1).toCharArray();
        if (s.startsWith("[") && s.endsWith("]")) {
            yapionType = parseArray(chars, 0, chars.length);
        } else if (s.startsWith("{") && s.endsWith("}")) {
            yapionType = parseObject(chars);
        } else {
            yapionType = new YAPIONValue(s);
        }
        return yapionType;
    }

}