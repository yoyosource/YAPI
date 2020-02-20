// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.json;

import yapi.file.FileUtils;
import yapi.internal.exceptions.objectnotation.JSONException;
import yapi.manager.json.value.JSONArray;
import yapi.manager.json.value.JSONObject;
import yapi.manager.json.value.JSONValue;
import yapi.manager.yapion.YAPIONVariable;
import yapi.manager.yapion.value.YAPIONArray;
import yapi.manager.yapion.value.YAPIONObject;
import yapi.manager.yapion.value.YAPIONValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JSONParser {

    public static void main(String[] args) {
        String s = Arrays.stream(FileUtils.fileContentFromResourceAsString("sample.json")).collect(Collectors.joining("\n"));
        JSONArray jsonObject = parseArray(s);
        //System.out.println(jsonObject);
        YAPIONArray yapionObject = toYAPION(jsonObject);
        //System.out.println(yapionObject);
        System.out.println(yapionObject.toHierarchyString());
    }

    private JSONParser() {
        throw new IllegalStateException();
    }

    public static synchronized YAPIONObject toYAPION(JSONObject jsonObject) {
        YAPIONObject yapionObject = new YAPIONObject();
        List<String> keys = jsonObject.getKeys();
        for (String s : keys) {
            JSONType jsonType = jsonObject.getVariable(s).getJsonType();
            if (jsonType instanceof JSONValue) {
                yapionObject.add(new YAPIONVariable(s, toYAPION((JSONValue) jsonType)));
            } else if (jsonType instanceof JSONArray) {
                yapionObject.add(new YAPIONVariable(s, toYAPION((JSONArray) jsonType)));
            } else if (jsonType instanceof JSONObject) {
                yapionObject.add(new YAPIONVariable(s, toYAPION((JSONObject) jsonType)));
            }
        }
        return yapionObject;
    }

    public synchronized static YAPIONArray toYAPION(JSONArray jsonArray) {
        YAPIONArray yapionArray = new YAPIONArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONType jsonType = jsonArray.get(i);
            if (jsonType instanceof JSONValue) {
                yapionArray.add(toYAPION((JSONValue) jsonType));
            } else if (jsonType instanceof JSONArray) {
                yapionArray.add(toYAPION((JSONArray) jsonType));
            } else if (jsonType instanceof JSONObject) {
                yapionArray.add(toYAPION((JSONObject) jsonType));
            }
        }
        return yapionArray;
    }

    private static YAPIONValue toYAPION(JSONValue jsonValue) {
        return new YAPIONValue(jsonValue.toString());
    }

    public static synchronized JSONObject parseObject(String json) {
        char[] chars = json.toCharArray();
        return parseObject(chars);
    }

    public static synchronized JSONArray parseArray(String json) {
        char[] chars = json.toCharArray();
        return parseArray(chars);
    }

    private static JSONObject parseObject(char[] chars) {
        if (!(chars[0] == '{' && chars[chars.length - 1] == '}')) {
            throw new JSONException("No JSON Object");
        }
        JSONObject jsonObject = new JSONObject();

        boolean escaped = false;
        boolean inString = false;
        int lastSeperation = 0;

        int i = 1;
        while (i < chars.length) {
            if (chars[i] == '\\') {
                escaped = true;
                i++;
                continue;
            }

            if (!escaped && chars[i] == '\"') {
                inString = !inString;
                i++;
                continue;
            }

            if (!escaped && !inString && chars[i] == ':') {
                String name = getKey(chars, lastSeperation);
                while (chars[i + 1] == ' ' || chars[i + 1] == '\n' || chars[i + 1] == '\t') {
                    i++;
                }
                i++;
                if (chars[i] == '{') {
                    char[] object = getObject(chars, i);
                    i += object.length;
                    JSONObject subObject = parseObject(object);
                    jsonObject.add(new JSONVariable(name, subObject));
                    continue;
                }
                if (chars[i] == '[') {
                    char[] object = getArray(chars, i);
                    i += object.length;
                    if (!(object[0] == '[' && object[object.length - 1] == ']')) {
                        throw new JSONException("No JSON Array");
                    }
                    jsonObject.add(new JSONVariable(name, parseArray(object)));
                    continue;
                }

                jsonObject.add(new JSONVariable(name, new JSONValue(getValue(chars, i))));
                continue;
            }

            if (!escaped && !inString && chars[i] == ',') {
                lastSeperation = i;
            }

            escaped = false;
            i++;
        }
        return jsonObject;
    }

    private static JSONArray parseArray(char[] chars) {
        if (!(chars[0] == '[' && chars[chars.length - 1] == ']')) {
            throw new JSONException("No JSON Array");
        }
        boolean escaped = false;
        boolean inString = false;

        StringBuilder st = new StringBuilder();
        List<String> strings = new ArrayList<>();
        int bracket = 0;

        int i = 1;
        while (i < chars.length - 1) {
            if (chars[i] == '\\') {
                st.append(chars[i]);
                escaped = true;
                i++;
                continue;
            }

            if (!escaped && chars[i] == '\"') {
                st.append(chars[i]);
                inString = !inString;
                i++;
                continue;
            }

            if (!escaped && !inString && (chars[i] == '[' || chars[i] == '{')) {
                bracket++;
            }
            if (!escaped && !inString && (chars[i] == ']' || chars[i] == '}')) {
                bracket--;
            }

            if (!escaped && !inString && bracket == 0 && chars[i] == ',') {
                strings.add(st.toString());
                st = new StringBuilder();
                if (chars[i + 1] == ' ') {
                    i++;
                }
            } else {
                st.append(chars[i]);
            }

            escaped = false;
            i++;
        }
        if (st.length() != 0) {
            strings.add(st.toString());
        }

        JSONArray jsonArray = new JSONArray();

        for (String s : strings) {
            s = s.trim();
            if (s.startsWith("{") && s.endsWith("}")) {
                jsonArray.add(parseObject(s.toCharArray()));
            } else if (s.startsWith("[") && s.endsWith("]")) {
                jsonArray.add(parseArray(s.toCharArray()));
            } else {
                jsonArray.add(new JSONValue(s));
            }
        }

        return jsonArray;
    }

    private static String getKey(char[] chars, int index) {
        boolean escaped = false;

        boolean startOfString = false;
        StringBuilder st = new StringBuilder();

        for (int i = index; i < chars.length; i++) {
            if (chars[i] == '\\') {
                escaped = true;
                continue;
            }

            if (!escaped && chars[i] == '\"') {
                if (startOfString) {
                    return st.toString();
                } else {
                    startOfString = true;
                }
                continue;
            }

            if (startOfString) {
                st.append(chars[i]);
            }
            escaped = false;
        }
        return "";
    }

    private static String getValue(char[] chars, int index) {
        StringBuilder st = new StringBuilder();

        if (chars[index] == '\"') {
            boolean escaped = false;
            boolean start = true;

            for (int i = index + 1; i < chars.length; i++) {
                if (chars[i] == '\\') {
                    st.append(chars[i]);
                    escaped = true;
                    continue;
                }

                if (!escaped && !start && chars[i] == '\"') {
                    break;
                }
                st.append(chars[i]);
                start = false;
                escaped = false;
            }

            return "\"" + st.toString() + "\"";
        } else {
            for (int i = index; i < chars.length; i++) {
                if (chars[i] == ',' || chars[i] == '}' || chars[i] == ']' || chars[i] == '\n') {
                    break;
                }
                st.append(chars[i]);
            }

            return st.toString();
        }
    }

    private static char[] getArray(char[] chars, int index) {
        boolean escaped = false;
        boolean inString = false;
        int bracket = 0;

        StringBuilder st = new StringBuilder();

        for (int i = index; i < chars.length; i++) {
            if (chars[i] == '\\') {
                st.append(chars[i]);
                escaped = true;
                continue;
            }

            if (!escaped && chars[i] == '\"') {
                st.append(chars[i]);
                inString = !inString;
                continue;
            }

            st.append(chars[i]);
            if (!escaped && !inString && chars[i] == '[') {
                bracket++;
            }
            if (!escaped && !inString && chars[i] == ']') {
                bracket--;
            }

            escaped = false;
            if (bracket == 0) {
                return st.toString().toCharArray();
            }
        }
        return "[]".toCharArray();
    }

    private static char[] getObject(char[] chars, int index) {
        boolean escaped = false;
        boolean inString = false;
        int bracket = 0;

        StringBuilder st = new StringBuilder();

        for (int i = index; i < chars.length; i++) {
            if (chars[i] == '\\') {
                st.append(chars[i]);
                escaped = true;
                continue;
            }

            if (!escaped && chars[i] == '\"') {
                st.append(chars[i]);
                inString = !inString;
                continue;
            }

            st.append(chars[i]);
            if (!escaped && !inString && chars[i] == '{') {
                bracket++;
            }
            if (!escaped && !inString && chars[i] == '}') {
                bracket--;
            }

            escaped = false;
            if (bracket == 0) {
                return st.toString().toCharArray();
            }
        }
        return "{}".toCharArray();
    }

}