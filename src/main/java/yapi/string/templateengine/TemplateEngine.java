// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.string.templateengine;

import yapi.string.StringSplitting;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class TemplateEngine {

    public static void main(String[] args) {
        TemplateEngine templateEngine = new TemplateEngine(new TemplateEngineVariable("name", "yoyosource"), new TemplateEngineVariable("world", 9));
        templateEngine.variableMode();
        //templateEngine.replaceString("${hello.substring(1).substring(hello.length() - 1)}");
        String s = templateEngine.replaceString("Hello ${name}");
        System.out.println(s);
    }

    private List<TemplateEngineVariable> variables;
    private boolean variableMode;

    public TemplateEngine() {
        variables = new ArrayList<>();
        variableMode = true;
    }

    public TemplateEngine(TemplateEngineVariable... variables) {
        this.variables = Arrays.asList(variables);
        variableMode = true;
    }

    public TemplateEngine(List<TemplateEngineVariable> variables) {
        this.variables = variables;
        variableMode = true;
    }

    public TemplateEngine variableMode() {
        variableMode = true;
        return this;
    }

    public TemplateEngine executionMode() {
        variableMode = false;
        return this;
    }

    public TemplateEngine methodMode() {
        return executionMode();
    }

    private Integer[] templateOccurrences(String s) {
        char[] chars = s.toCharArray();
        List<Integer> integers = new ArrayList<>();
        int bracket = 0;
        int line = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '{') {
                bracket++;
            }
            if (chars[i] == '$' && bracket == 0 && i < chars.length - 1 && chars[i + 1] == '{' && i < chars.length - 2) {
                if (chars[i + 2] == '}') {
                    continue;
                }
                integers.add(i);
            }
            if (chars[i] == '\n') {
                if (bracket != 0) {
                    System.out.println("Error line: " + line);
                }
                line++;
            }
            if (chars[i] == '}') {
                bracket--;
            }
        }
        if (bracket != 0) {
            System.out.println("Error line: " + line);
        }
        return integers.toArray(new Integer[0]);
    }

    public String replaceString(String s) {
        Integer[] integers = templateOccurrences(s);
        StringBuilder st = new StringBuilder();
        StringBuilder pd = new StringBuilder();

        char[] chars = s.toCharArray();
        int index = 0;
        int i = 0;
        int bracket = 0;
        while (i < s.length()) {
            if (pd.length() != 0) {
                if (chars[i] == '{') {
                    bracket++;
                }
                pd.append(chars[i]);
                if (chars[i] == '}') {
                    bracket--;
                }
                if (bracket == 0) {
                    String t = pd.toString().substring(2, pd.length() - 1);
                    pd = new StringBuilder();
                    Object o = null;
                    if (variableMode) {
                        o = variableLookUp(t);
                    } else {
                        o = methodLookUp(t);
                    }
                    if (o == null) {
                        st.append("${").append(t).append("}");
                    } else {
                        st.append(o.toString());
                    }
                }
                i++;
                continue;
            }
            if (index < integers.length && i == integers[index]) {
                index++;
                pd.append(chars[i]);
            } else {
                st.append(chars[i]);
            }
            i++;
        }

        return st.toString();
    }

    private Object variableLookUp(String s) {
        TemplateEngineVariable variable = null;
        try {
            variable = new TemplateEngineVariable(s, 0);
        } catch (IllegalArgumentException e) {
            return null;
        }
        int i = variables.indexOf(variable);
        if (i == -1) {
            return null;
        }
        return variables.get(i).getValue();
    }

    private Object methodLookUp(String s) {
        if (!(s.contains(".") || s.contains("[") || s.contains("]") || s.contains("(") || s.contains(")") || s.contains(",") || s.contains(" "))) {
            return variableLookUp(s);
        }
        String[] strings = StringSplitting.splitString(s, new String[]{".", "[", "]", "(", ")", ",", "?", " "}, true, false, false);
        List<String> stringList = new ArrayList<>();
        for (String t : strings) {
            if (t.equals(" ")) {
                continue;
            }
            stringList.add(t);
        }
        variableLookUp(stringList);
        return null;
    }

    private void variableLookUp(List<String> strings) {
        Stack<TemplateEngineToken> templateEngineTokens = new Stack<>() {};
        for (int i = 0; i < strings.size(); i++) {
            if (strings.get(i).equals(".") && i > 0 && strings.get(i - 1).matches("[a-z][a-zA-z0-9_\\-]*")) {
                templateEngineTokens.pop();
                templateEngineTokens.push(new TemplateEngineToken(variableLookUp(strings.get(i - 1)), "VAR"));
                templateEngineTokens.push(new TemplateEngineToken(strings.get(i), ""));
            } else {
                templateEngineTokens.push(new TemplateEngineToken(strings.get(i), ""));
            }
        }
        TemplateEngineToken.setCompact(true);
        System.out.println(templateEngineTokens.stream().map(TemplateEngineToken::toString).collect(Collectors.joining(" ")));
    }

    // for (TemplateEngineVariable variable : variables) {
    //     try {
    //         Method m[] = variable.getValue().getClass().getMethods();
    //         for (int i = 0; i < m.length; i++) {
    //             System.out.println(m[i].toString());
    //             //m[i].invoke();
    //         }
    //     } catch (Throwable e) {
    //         e.printStackTrace();
    //     }
    //     System.out.println();
    // }

}