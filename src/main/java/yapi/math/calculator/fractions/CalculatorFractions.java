// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.calculator.fractions;

import yapi.math.NumberUtils;
import yapi.string.StringSplitting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CalculatorFractions {

    private Fraction result;

    public CalculatorFractions(String toCalculate) {
        List<String> strings = split(toCalculate);
        List<Part> parts = parts(strings);
        List<Long> priorities = priorities(parts);
        result = calculate(parts, priorities);

    }

    public Fraction getResult() {
        return result;
    }

    private List<String> split(String s) {
        List<String> strings = Arrays.stream(StringSplitting.splitString(s.replaceAll(" +", " "), new String[]{"+", "-", "*", "/", "%", "(", ")", "^", " "}, true, false)).collect(Collectors.toList());
        for (int i = 0; i < strings.size() - 1; i++) {
            if (strings.get(i).matches("[0-9]+(\\.[0-9]+)?") && strings.get(i + 1).equals("%")) {
                if (i < strings.size() - 2) {
                    if (strings.get(i + 2).matches("\\+|-|\\*|/|%")) {
                        strings.set(i + 1, "");
                        strings.set(i, strings.get(i) + "%");
                    }
                } else {
                    strings.set(i + 1, "");
                    strings.set(i, strings.get(i) + "%");
                }
            }
        }
        return strings;
    }

    private List<Part> parts(List<String> strings) {
        List<Part> parts = new ArrayList<>();
        for (String s : strings) {
            if (s.trim().isEmpty()) {
                continue;
            }
            if (s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/") || s.equals("%") || s.equals("(") || s.equals(")") || s.equals("^")) {
                parts.add(new Part(s));
            } else {
                parts.add(new Part(Fraction.decode(s)));
            }
        }
        return parts;
    }

    private List<Long> priorities(List<Part> parts) {
        long l = 0;
        List<Long> priorities = new ArrayList<>();
        for (Part part : parts) {
            if (part.isOperator()) {
                if (part.getOperator().equals("(")) {
                    l += 2;
                    priorities.add(0L);
                    continue;
                } else if (part.getOperator().equals(")")) {
                    l -= 2;
                    priorities.add(0L);
                    continue;
                }
                if (part.getOperator().equals("+")) {
                    priorities.add(l + 1);
                } else if (part.getOperator().equals("-")) {
                    priorities.add(l + 1);
                } else if (part.getOperator().equals("*")) {
                    priorities.add(l + 2);
                } else if (part.getOperator().equals("/")) {
                    priorities.add(l + 2);
                } else if (part.getOperator().equals("%")) {
                    priorities.add(l + 2);
                }
                else {
                    priorities.add(l);
                }
            } else {
                priorities.add(0L);
            }
        }
        return priorities;
    }

    private Fraction calculate(List<Part> parts, List<Long> priorities) {
        try {
            while (parts.size() != 1) {
                long index = getIndex(priorities);
                result(parts, index);
                remove(parts, priorities, index);
            }
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
        return parts.get(0).getFraction().add(Fraction.decode("0|1"));
    }

    private long getIndex(List<Long> priorities) {
        return NumberUtils.maxIndex(priorities);
    }

    private void result(List<Part> parts, long index) {
        Part number2 = parts.get((int)index + 1);
        Part operation = parts.get((int)index);
        Part number1 = parts.get((int)index - 1);

        if (!operation.isOperator()) {
            return;
        }
        if (!number1.isFraction()) {
            return;
        }
        if (!number2.isFraction()) {
            return;
        }

        Fraction f = null;
        if (operation.getOperator().equals("+")) {
            f = number1.getFraction().add(number2.getFraction());
        } else if (operation.getOperator().equals("-")) {
            f = number1.getFraction().subtract(number2.getFraction());
        } else if (operation.getOperator().equals("*")) {
            f = number1.getFraction().multiply(number2.getFraction());
        } else if (operation.getOperator().equals("/")) {
            f = number1.getFraction().divide(number2.getFraction());
        } else if (operation.getOperator().equals("%")) {
            f = number1.getFraction().remainder(number2.getFraction());
        }
        if (f != null) {
            parts.set((int)index, new Part(f));
        }
    }

    private void remove(List<Part> parts, List<Long> priorities, long index) {
        parts.remove((int)index + 1);
        parts.remove((int)index - 1);
        priorities.remove((int)index + 1);
        priorities.set((int)index, 0L);
        priorities.remove((int)index - 1);
        index--;
        if (index > 0 && parts.get((int)index - 1).getOperator().equals("(") && parts.get((int)index + 1).getOperator().equals(")")) {
            parts.remove((int)index + 1);
            parts.remove((int)index - 1);
            priorities.remove((int)index + 1);
            priorities.remove((int)index - 1);
        }
    }

    private class Part {

        private Fraction fraction;
        private String operator;

        public Part(Fraction fraction) {
            this.fraction = fraction;
            operator = null;
        }

        public Part(String operator) {
            this.operator = operator;
            fraction = null;
        }

        public boolean isFraction() {
            return fraction != null && operator == null;
        }

        public boolean isOperator() {
            return operator != null && fraction == null;
        }

        public Fraction getFraction() {
            return fraction;
        }

        public String getOperator() {
            return operator;
        }

        @Override
        public String toString() {
            if (fraction != null) {
                return "Part{" +
                        "fraction=" + fraction +
                        '}';
            } else {
                return "Part{" +
                        "operator='" + operator + '\'' +
                        '}';
            }
        }
    }

}