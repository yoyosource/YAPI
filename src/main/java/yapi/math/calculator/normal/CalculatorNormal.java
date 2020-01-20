package yapi.math.calculator.normal;

import java.util.ArrayList;
import java.util.List;

public class CalculatorNormal {

    private List<Token> tokens;

    public CalculatorNormal(List<Token> tokens) {
        List<Token> tok = new ArrayList<>();
        tok.addAll(tokens);
        this.tokens = tok;
    }

    public CalculatorNormal(String s) {
        List<Token> tok = new Lexer().createTokens(s).getTokens();
        this.tokens = tok;
    }

    public Object calculate() {
        System.out.println(tokens);
        if (!checkIfCalculation()) {
            return null;
        }
        replace();
        calc(priorityArray());
        return tokens.get(0).getVal();
    }

    private boolean checkIfCalculation() {
        if (tokens.isEmpty()) {
            return false;
        }
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i).getKey();
            if (!token.equals("int") && !token.equals("num") && !token.equals("lon") && !token.equals("OPE") && !token.equals("STb") && !token.equals("MAT")) {
                return false;
            }
        }
        return true;
    }

    private void replace() {
        List<Token> nTokens = new ArrayList<>();

        if (tokens.get(0).getKey().equals("OPE")) {
            nTokens.add(new Token("int", 0));
        }

        for (int i = 0; i < tokens.size(); i++) {

            if (i == 0) {
                nTokens.add(tokens.get(i));
                continue;
            }

            String key = tokens.get(i).getKey();
            String val = tokens.get(i).getVal().toString();

            String key2 = tokens.get(i - 1).getKey();
            String val2 = tokens.get(i - 1).getVal().toString();
            if (key.equals("STb")) {
                if (val.equals("(") && (key2.equals("int") || key2.equals("num") || key2.equals("lon"))) {
                    nTokens.add(new Token("OPE", "*"));
                }
            }
            if (tokens.get(i).isType()) {
                if (tokens.get(i - 1).isType()) {
                    nTokens.add(new Token("OPE", "*"));
                }
                if (key2.equals("STb") && val2.equals(")")) {
                    nTokens.add(new Token("OPE", "*"));
                }
            }
            if (key.equals("OPE")) {
                if (val.equals("ln")) {
                    nTokens.add(new Token("num", Math.E));
                    nTokens.add(new Token("OPE", "log"));
                    continue;
                }
                if (val.equals("root")) {
                    if (!(key2.equals("num") || key2.equals("lon") || key2.equals("int"))) {
                        nTokens.add(new Token("int", "2"));
                    }
                }
            }
            nTokens.add(tokens.get(i));
        }
        tokens = nTokens;
    }

    private List<Integer> priorityArray() {
        int p = 0;

        List<Integer> priorities = new ArrayList<>();
        ArrayList<Boolean> absolute = new ArrayList<>();
        absolute.add(false);

        for (int i = 0; i < tokens.size(); i++) {
            String key = tokens.get(i).getKey();
            String val = tokens.get(i).getVal().toString();

            if (key.equals("STb")) {
                if (val.equals("(")) {
                    p += 3;
                    absolute.add(false);
                } else {
                    p -= 3;
                    if (absolute.get(absolute.size() - 1)) {
                        return new ArrayList<>();
                    }
                    absolute.remove(absolute.size() - 1);
                }
            }
            if (key.equals("MAT")) {
                if (absolute.get(absolute.size() - 1)) {
                    p -= 4;
                    absolute.set(absolute.size() - 1, false);
                } else {
                    p += 4;
                    absolute.set(absolute.size() - 1, true);
                }
            }

            if (key.equals("OPE")) {
                if (val.matches("[+]|[-]")) {
                    priorities.add(p + 1);
                } else if (val.matches("[*]|[/]|[%]|[!]")) {
                    priorities.add(p + 2);
                } else if (val.matches("[\\^]|(root)|(sin)|(cos)|(tan)|(asin)|(acos)|(atan)|(log)|(gauss)|(sigmoid)|(sig)")) {
                    priorities.add(p + 3);
                }
            } else {
                priorities.add(0);
            }

        }
        return priorities;
    }

    private int getMax(List<Integer> priorities) {
        int c = 0;
        int x = 0;
        for (int i = 0; i < priorities.size(); i++) {
            if (priorities.get(i) > c) {
                c = priorities.get(i);
                x = i;
            }
        }
        return x;
    }

    private void calc(List<Integer> priorities) {
        removeBrackets(priorities);

        if (priorities.size() == 1) {
            return;
        }

        int max = getMax(priorities);

        Object val = calcC(tokens.get(max), tokens.get(max - 1), tokens.get(max + 1));

        if (val instanceof Integer) {
            tokens.set(max, new Token("int", val));
            priorities.set(max, 0);
            remove(max, priorities);
            remove(max, tokens);
        }
        if (val instanceof Double) {
            tokens.set(max, new Token("num", val));
            priorities.set(max, 0);
            remove(max, priorities);
            remove(max, tokens);
        }
        if (val instanceof Long) {
            tokens.set(max, new Token("lon", val));
            priorities.set(max, 0);
            remove(max, priorities);
            remove(max, tokens);
        }

        removeBrackets(priorities);

        if (tokens.size() != 1) {
            calc(priorities);
        }
    }

    private void removeBrackets(List<Integer> priorities) {
        for (int i = tokens.size() - 2; i >= 1; i--) {
            try {
                if (tokens.get(i + 1).getVal().toString().equals(")")) {
                    if (tokens.get(i - 1).getVal().toString().equals("(")) {
                        remove(i, tokens);
                        remove(i, priorities);
                    }
                }
                if (tokens.get(i + 1).getVal().toString().equals("|")) {
                    if (tokens.get(i - 1).getVal().toString().equals("|")) {
                        remove(i, tokens);
                        remove(i, priorities);
                    }
                }
            } catch (IndexOutOfBoundsException e) {

            }
        }
    }

    private void remove(int index, List<?> toRemove) {
        toRemove.remove(index + 1);
        toRemove.remove(index - 1);
    }

    private Object calcC(Token calculation, Token t1, Token t2) {
        String typ1 = t1.getKey();

        if (typ1.equals("int")) {
            return intT(calculation, t1, t2);
        }

        if (typ1.equals("num")) {
            return numT(calculation, t1, t2);
        }

        if (typ1.equals("lon")) {
            return lonT(calculation, t1, t2);
        }

        return null;
    }

    private Object intT(Token calculation, Token t1, Token t2) {
        String calc = calculation.getVal().toString();

        Object val1 = t1.getVal();

        String typ2 = t2.getKey();
        Object val2 = t2.getVal();

        if (typ2.equals("int")) {
            if (calc.equals("+")) {
                return (int)val1 + (int)val2;
            }
            if (calc.equals("-")) {
                return (int)val1 - (int)val2;
            }
            if (calc.equals("*")) {
                return (int)val1 * (int)val2;
            }
            if (calc.equals("/")) {
                return (int)val1 / (int)val2;
            }

            if (calc.equals("%")) {
                return (int)val1 % (int)val2;
            }

            if (calc.equals("^")) {
                return Math.pow((int)val1, (int)val2);
            }
            if (calc.equals("root")) {
                if ((int)val1 == 2) {
                    return Math.sqrt((int)val2);
                }
                if ((int)val1 == 3) {
                    return Math.cbrt((int)val2);
                }
                return (int)Math.pow(Math.E, Math.log((int)val2) / (int)val1);
            }

            if (calc.equals("sin")) {
                return (int)val1 * (int)Math.sin((int)val2);
            }
            if (calc.equals("cos")) {
                return (int)val1 * (int)Math.cos((int)val2);
            }
            if (calc.equals("tan")) {
                return (int)val1 * (int)Math.tan((int)val2);
            }
            if (calc.equals("asin")) {
                return (int)val1 * (int)Math.asin((int)val2);
            }
            if (calc.equals("acos")) {
                return (int)val1 * (int)Math.acos((int)val2);
            }
            if (calc.equals("atan")) {
                return (int)val1 * (int)Math.atan((int)val2);
            }
            if (calc.equals("log")) {
                return (int) (Math.log((int)val2) / Math.log((int)val1));
            }
            if (calc.equals("gauss")) {
                return ((int)val1 * (1 / (Math.sqrt(2 * Math.PI)) * Math.exp(-Math.pow((int)val2, 2) / 2)));
            }
            if (calc.equals("sig") || calc.equals("sigmoid")) {
                return (int)val1 * (int)(1 / (1 + Math.exp(-(int)val2)));
            }
        }
        if (typ2.equals("num")) {
            if (calc.equals("+")) {
                return (int)val1 + (double)val2;
            }
            if (calc.equals("-")) {
                return (int)val1 - (double)val2;
            }
            if (calc.equals("*")) {
                return (int)val1 * (double)val2;
            }
            if (calc.equals("/")) {
                return (int)val1 / (double)val2;
            }

            if (calc.equals("%")) {
                return (int)val1 % (double)val2;
            }

            if (calc.equals("^")) {
                return Math.pow((int)val1, (double)val2);
            }
            if (calc.equals("root")) {
                if ((int)val1 == 2) {
                    return Math.sqrt((double)val2);
                }
                if ((int)val1 == 3) {
                    return Math.cbrt((double)val2);
                }
                return Math.pow(Math.E, Math.log((double)val2) / (int)val1);
            }

            if (calc.equals("sin")) {
                return (int)val1 * Math.sin((double)val2);
            }
            if (calc.equals("cos")) {
                return (int)val1 * Math.cos((double)val2);
            }
            if (calc.equals("tan")) {
                return (int)val1 * Math.tan((double)val2);
            }
            if (calc.equals("asin")) {
                return (int)val1 * Math.asin((double)val2);
            }
            if (calc.equals("acos")) {
                return (int)val1 * Math.acos((double)val2);
            }
            if (calc.equals("atan")) {
                return (int)val1 * Math.atan((double)val2);
            }
            if (calc.equals("log")) {
                return (int) (Math.log((double)val2) / Math.log((int)val1));
            }
            if (calc.equals("gauss")) {
                return ((int)val1 * (1 / (Math.sqrt(2 * Math.PI)) * Math.exp(-Math.pow((double)val2, 2) / 2)));
            }
            if (calc.equals("sig") || calc.equals("sigmoid")) {
                return (int)val1 * (1 / (1 + Math.exp(-(double)val2)));
            }
        }
        if (typ2.equals("lon")) {
            if (calc.equals("+")) {
                return (int)val1 + (long)val2;
            }
            if (calc.equals("-")) {
                return (int)val1 - (long)val2;
            }
            if (calc.equals("*")) {
                return (int)val1 * (long)val2;
            }
            if (calc.equals("/")) {
                return (int)val1 / (long)val2;
            }

            if (calc.equals("%")) {
                return (int)val1 % (long)val2;
            }

            if (calc.equals("^")) {
                return Math.pow((int)val1, (long)val2);
            }
            if (calc.equals("root")) {
                if ((int)val1 == 2) {
                    return Math.sqrt((long)val2);
                }
                if ((int)val1 == 3) {
                    return Math.cbrt((long)val2);
                }
                return Math.pow(Math.E, Math.log((long)val2) / (int)val1);
            }

            if (calc.equals("sin")) {
                return (int)val1 * Math.sin((long)val2);
            }
            if (calc.equals("cos")) {
                return (int)val1 * Math.cos((long)val2);
            }
            if (calc.equals("tan")) {
                return (int)val1 * Math.tan((long)val2);
            }
            if (calc.equals("asin")) {
                return (int)val1 * Math.asin((long)val2);
            }
            if (calc.equals("acos")) {
                return (int)val1 * Math.acos((long)val2);
            }
            if (calc.equals("atan")) {
                return (int)val1 * Math.atan((long)val2);
            }
            if (calc.equals("log")) {
                return (int) (Math.log((long)val2) / Math.log((int)val1));
            }
            if (calc.equals("gauss")) {
                return ((int)val1 * (1 / (Math.sqrt(2 * Math.PI)) * Math.exp(-Math.pow((long)val2, 2) / 2)));
            }
            if (calc.equals("sig") || calc.equals("sigmoid")) {
                return (int)val1 * (1 / (1 + Math.exp(-(long)val2)));
            }
        }
        return null;
    }

    private Object numT(Token calculation, Token t1, Token t2) {
        String calc = calculation.getVal().toString();

        Object val1 = t1.getVal();

        String typ2 = t2.getKey();
        Object val2 = t2.getVal();

        if (typ2.equals("int")) {
            if (calc.equals("+")) {
                return (double)val1 + (int)val2;
            }
            if (calc.equals("-")) {
                return (double)val1 - (int)val2;
            }
            if (calc.equals("*")) {
                return (double)val1 * (int)val2;
            }
            if (calc.equals("/")) {
                return (double)val1 / (int)val2;
            }

            if (calc.equals("%")) {
                return (double)val1 % (int)val2;
            }

            if (calc.equals("^")) {
                return Math.pow((double)val1, (int)val2);
            }
            if (calc.equals("root")) {
                if ((double)val1 == 2) {
                    return Math.sqrt((int)val2);
                }
                if ((double)val1 == 3) {
                    return Math.cbrt((int)val2);
                }
                return Math.pow(Math.E, Math.log((int)val2) / (double)val1);
            }

            if (calc.equals("sin")) {
                return (double)val1 * (int)Math.sin((int)val2);
            }
            if (calc.equals("cos")) {
                return (double)val1 * (int)Math.cos((int)val2);
            }
            if (calc.equals("tan")) {
                return (double)val1 * (int)Math.tan((int)val2);
            }
            if (calc.equals("asin")) {
                return (double)val1 * (int)Math.asin((int)val2);
            }
            if (calc.equals("acos")) {
                return (double)val1 * (int)Math.acos((int)val2);
            }
            if (calc.equals("atan")) {
                return (double)val1 * (int)Math.atan((int)val2);
            }
            if (calc.equals("log")) {
                return (Math.log((int)val2) / Math.log((double)val1));
            }
            if (calc.equals("gauss")) {
                return ((double)val1 * (1 / (Math.sqrt(2 * Math.PI)) * Math.exp(-Math.pow((int)val2, 2) / 2)));
            }
            if (calc.equals("sig") || calc.equals("sigmoid")) {
                return (double)val1 * (int)(1 / (1 + Math.exp(-(int)val2)));
            }
        }
        if (typ2.equals("num")) {
            if (calc.equals("+")) {
                return (double)val1 + (double)val2;
            }
            if (calc.equals("-")) {
                return (double)val1 - (double)val2;
            }
            if (calc.equals("*")) {
                return (double)val1 * (double)val2;
            }
            if (calc.equals("/")) {
                return (double)val1 / (double)val2;
            }

            if (calc.equals("%")) {
                return (double)val1 % (double)val2;
            }

            if (calc.equals("^")) {
                return Math.pow((double)val1, (double)val2);
            }
            if (calc.equals("root")) {
                if ((double)val1 == 2) {
                    return Math.sqrt((double)val2);
                }
                if ((double)val1 == 3) {
                    return Math.cbrt((double)val2);
                }
                return Math.pow(Math.E, Math.log((double)val2) / (double)val1);
            }

            if (calc.equals("sin")) {
                return (double)val1 * Math.sin((double)val2);
            }
            if (calc.equals("cos")) {
                return (double)val1 * Math.cos((double)val2);
            }
            if (calc.equals("tan")) {
                return (double)val1 * Math.tan((double)val2);
            }
            if (calc.equals("asin")) {
                return (double)val1 * Math.asin((double)val2);
            }
            if (calc.equals("acos")) {
                return (double)val1 * Math.acos((double)val2);
            }
            if (calc.equals("atan")) {
                return (double)val1 * Math.atan((double)val2);
            }
            if (calc.equals("log")) {
                return (Math.log((double)val2) / Math.log((double)val1));
            }
            if (calc.equals("gauss")) {
                return ((double)val1 * (1 / (Math.sqrt(2 * Math.PI)) * Math.exp(-Math.pow((double)val2, 2) / 2)));
            }
            if (calc.equals("sig") || calc.equals("sigmoid")) {
                return (double)val1 * (1 / (1 + Math.exp(-(double)val2)));
            }
        }
        if (typ2.equals("lon")) {
            if (calc.equals("+")) {
                return (double)val1 + (long)val2;
            }
            if (calc.equals("-")) {
                return (double)val1 - (long)val2;
            }
            if (calc.equals("*")) {
                return (double)val1 * (long)val2;
            }
            if (calc.equals("/")) {
                return (double)val1 / (long)val2;
            }

            if (calc.equals("%")) {
                return (double)val1 % (long)val2;
            }

            if (calc.equals("^")) {
                return Math.pow((double)val1, (long)val2);
            }
            if (calc.equals("root")) {
                if ((double)val1 == 2) {
                    return Math.sqrt((long)val2);
                }
                if ((double)val1 == 3) {
                    return Math.cbrt((long)val2);
                }
                return Math.pow(Math.E, Math.log((long)val2) / (double)val1);
            }

            if (calc.equals("sin")) {
                return (double)val1 * Math.sin((long)val2);
            }
            if (calc.equals("cos")) {
                return (double)val1 * Math.cos((long)val2);
            }
            if (calc.equals("tan")) {
                return (double)val1 * Math.tan((long)val2);
            }
            if (calc.equals("asin")) {
                return (double)val1 * Math.asin((long)val2);
            }
            if (calc.equals("acos")) {
                return (double)val1 * Math.acos((long)val2);
            }
            if (calc.equals("atan")) {
                return (double)val1 * Math.atan((long)val2);
            }
            if (calc.equals("log")) {
                return (Math.log((long)val2) / Math.log((double)val1));
            }
            if (calc.equals("gauss")) {
                return ((double)val1 * (1 / (Math.sqrt(2 * Math.PI)) * Math.exp(-Math.pow((long)val2, 2) / 2)));
            }
            if (calc.equals("sig") || calc.equals("sigmoid")) {
                return (double)val1 * (1 / (1 + Math.exp(-(long)val2)));
            }
        }
        return null;
    }

    private Object lonT(Token calculation, Token t1, Token t2) {
        String calc = calculation.getVal().toString();

        Object val1 = t1.getVal();

        String typ2 = t2.getKey();
        Object val2 = t2.getVal();

        if (typ2.equals("int")) {
            if (calc.equals("+")) {
                return (long)val1 + (int)val2;
            }
            if (calc.equals("-")) {
                return (long)val1 - (int)val2;
            }
            if (calc.equals("*")) {
                return (long)val1 * (int)val2;
            }
            if (calc.equals("/")) {
                return (long)val1 / (int)val2;
            }

            if (calc.equals("%")) {
                return (long)val1 % (int)val2;
            }

            if (calc.equals("^")) {
                return Math.pow((long)val1, (int)val2);
            }
            if (calc.equals("root")) {
                if ((long)val1 == 2) {
                    return Math.sqrt((int)val2);
                }
                if ((long)val1 == 3) {
                    return Math.cbrt((int)val2);
                }
                return Math.pow(Math.E, Math.log((int)val2) / (long)val1);
            }

            if (calc.equals("sin")) {
                return (long)val1 * (int)Math.sin((int)val2);
            }
            if (calc.equals("cos")) {
                return (long)val1 * (int)Math.cos((int)val2);
            }
            if (calc.equals("tan")) {
                return (long)val1 * (int)Math.tan((int)val2);
            }
            if (calc.equals("asin")) {
                return (long)val1 * (int)Math.asin((int)val2);
            }
            if (calc.equals("acos")) {
                return (long)val1 * (int)Math.acos((int)val2);
            }
            if (calc.equals("atan")) {
                return (long)val1 * (int)Math.atan((int)val2);
            }
            if (calc.equals("log")) {
                return (Math.log((int)val2) / Math.log((long)val1));
            }
            if (calc.equals("gauss")) {
                return ((long)val1 * (1 / (Math.sqrt(2 * Math.PI)) * Math.exp(-Math.pow((int)val2, 2) / 2)));
            }
            if (calc.equals("sig") || calc.equals("sigmoid")) {
                return (long)val1 * (int)(1 / (1 + Math.exp(-(int)val2)));
            }
        }
        if (typ2.equals("num")) {
            if (calc.equals("+")) {
                return (long)val1 + (double)val2;
            }
            if (calc.equals("-")) {
                return (long)val1 - (double)val2;
            }
            if (calc.equals("*")) {
                return (long)val1 * (double)val2;
            }
            if (calc.equals("/")) {
                return (long)val1 / (double)val2;
            }

            if (calc.equals("%")) {
                return (long)val1 % (double)val2;
            }

            if (calc.equals("^")) {
                return Math.pow((long)val1, (double)val2);
            }
            if (calc.equals("root")) {
                if ((long)val1 == 2) {
                    return Math.sqrt((double)val2);
                }
                if ((long)val1 == 3) {
                    return Math.cbrt((double)val2);
                }
                return Math.pow(Math.E, Math.log((double)val2) / (long)val1);
            }

            if (calc.equals("sin")) {
                return (long)val1 * Math.sin((double)val2);
            }
            if (calc.equals("cos")) {
                return (long)val1 * Math.cos((double)val2);
            }
            if (calc.equals("tan")) {
                return (long)val1 * Math.tan((double)val2);
            }
            if (calc.equals("asin")) {
                return (long)val1 * Math.asin((double)val2);
            }
            if (calc.equals("acos")) {
                return (long)val1 * Math.acos((double)val2);
            }
            if (calc.equals("atan")) {
                return (long)val1 * Math.atan((double)val2);
            }
            if (calc.equals("log")) {
                return (Math.log((double)val2) / Math.log((long)val1));
            }
            if (calc.equals("gauss")) {
                return ((long)val1 * (1 / (Math.sqrt(2 * Math.PI)) * Math.exp(-Math.pow((double)val2, 2) / 2)));
            }
            if (calc.equals("sig") || calc.equals("sigmoid")) {
                return (long)val1 * (1 / (1 + Math.exp(-(double)val2)));
            }
        }
        if (typ2.equals("lon")) {
            if (calc.equals("+")) {
                return (long)val1 + (long)val2;
            }
            if (calc.equals("-")) {
                return (long)val1 - (long)val2;
            }
            if (calc.equals("*")) {
                return (long)val1 * (long)val2;
            }
            if (calc.equals("/")) {
                return (long)val1 / (long)val2;
            }

            if (calc.equals("%")) {
                return (long)val1 % (long)val2;
            }

            if (calc.equals("^")) {
                return Math.pow((long)val1, (long)val2);
            }
            if (calc.equals("root")) {
                if ((long)val1 == 2) {
                    return Math.sqrt((long)val2);
                }
                if ((long)val1 == 3) {
                    return Math.cbrt((long)val2);
                }
                return Math.pow(Math.E, Math.log((long)val2) / (long)val1);
            }

            if (calc.equals("sin")) {
                return (long)val1 * Math.sin((long)val2);
            }
            if (calc.equals("cos")) {
                return (long)val1 * Math.cos((long)val2);
            }
            if (calc.equals("tan")) {
                return (long)val1 * Math.tan((long)val2);
            }
            if (calc.equals("asin")) {
                return (long)val1 * Math.asin((long)val2);
            }
            if (calc.equals("acos")) {
                return (long)val1 * Math.acos((long)val2);
            }
            if (calc.equals("atan")) {
                return (long)val1 * Math.atan((long)val2);
            }
            if (calc.equals("log")) {
                return (Math.log((long)val2) / Math.log((long)val1));
            }
            if (calc.equals("gauss")) {
                return ((long)val1 * (1 / (Math.sqrt(2 * Math.PI)) * Math.exp(-Math.pow((long)val2, 2) / 2)));
            }
            if (calc.equals("sig") || calc.equals("sigmoid")) {
                return (long)val1 * (1 / (1 + Math.exp(-(long)val2)));
            }
        }
        return null;
    }

}

