package yapi.math.mathscript;

import yapi.exceptions.math.MathScriptException;
import yapi.file.FileUtils;
import yapi.math.mathscript.state.Variable;
import yapi.math.mathscript.state.VariableState;
import yapi.math.mathscript.tree.Knot;
import yapi.math.mathscript.tree.functions.*;
import yapi.math.mathscript.tree.operations.OperationAdd;
import yapi.math.mathscript.tree.operations.OperationDiv;
import yapi.math.mathscript.tree.operations.OperationMul;
import yapi.math.mathscript.tree.operations.OperationSub;
import yapi.math.mathscript.tree.special.FunctionProduct;
import yapi.math.mathscript.tree.special.FunctionSum;
import yapi.math.mathscript.tree.values.FunctionConstant;
import yapi.math.mathscript.tree.values.FunctionVariable;
import yapi.string.StringSplitting;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MathScript {

    private VariableState variableState = new VariableState();
    private List<Token> tokens = new ArrayList<>();

    public static void main(String[] args) {
        MathScript mathScript = new MathScript("/Users/jojo/IdeaProjects/YAPI/src/main/resources/Test.mathscript");
        mathScript.run();
    }

    public MathScript(String s) {
        if (s.endsWith(".mathscript") && s.split("\n").length == 1) {
            try {
                createScript(new File(s));
            } catch (MathScriptException e) {
                createScript(s);
            }
        }
    }

    public MathScript(File f) {
        createScript(f);
    }

    public void setPrecision(int precision) {
        Knot.setMathContext(precision);
    }

    public void run() {
        variableState = new VariableState();

        List<List<Token>> lines = getLines(tokens);

        for (int i = 0; i < lines.size(); i++) {
            try {
                runLine(lines.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void runLine(List<Token> tokens) {
        String variable = "";
        if (tokens.size() > 2 && tokens.get(1).getToken().equals("=")) {
            variable = tokens.remove(0).getToken();
            tokens.remove(0);
        }
        Knot knot = createTree(tokens);
        BigDecimal d = knot.getResult(variableState.copy());
        if (!variable.isEmpty()) {
            variableState = variableState.add(new Variable(variable).setValue(d));
        } else {
            System.out.println(d.toString());
        }
    }

    private void createScript(String s) {
        createScript(s.split("\n"));
    }

    private void createScript(File f) {
        if (!f.exists()) {
            throw new MathScriptException("Please specify a valid file");
        }
        if (!f.isFile()) {
            throw new MathScriptException("Please specify a file");
        }
        if (!f.getPath().endsWith(".mathscript")) {
            throw new MathScriptException("Please specify a '.mathscript' file");
        }
        createScript(FileUtils.fileContentAsString(f));
    }

    private void createScript(String[] strings) {
        if (strings.length == 0) {
            throw new MathScriptException("Input is empty");
        }
        for (int i = 0; i < strings.length; i++) {
            strings[i] = strings[i].trim();
        }
        for (String s : strings) {
            if (s.isBlank()) {
                continue;
            }
            String[] str = StringSplitting.splitString(s, new String[]{" ", "(", ")", "{", "}", "=", ":", ",", ";"}, true, false, false);
            for (int i = 0; i < str.length; i++) {
                if (str[i].isBlank()) {
                    continue;
                }
                String t = str[i];
                if (i != 0 && t.equals("}")) {
                    tokens.add(new Token(";"));
                }
                tokens.add(new Token(t));
                if (i != str.length - 1 && t.equals("{")) {
                    tokens.add(new Token(";"));
                }
            }
            if (!tokens.isEmpty() && !tokens.get(tokens.size() - 1).getToken().equals(";")) {
                tokens.add(new Token(";"));
            }
        }
        tokens.remove(tokens.size() - 1);
    }

    private List<List<Token>> getLines(List<Token> tokens) {
        List<List<Token>> lines = new ArrayList<>();
        List<Token> t = new ArrayList<>();
        for (Token token : tokens) {
            if (token.getToken().equals(";")) {
                lines.add(t);
                t = new ArrayList<>();
            } else {
                t.add(token);
            }
        }
        if (!t.isEmpty()) {
            lines.add(t);
        }
        return lines;
    }

    private Knot createTree(List<Token> tokens) {
        Knot knot = null;
        List<List<Token>> parameters = parameters(tokens);
        String functionOrOperator = parameters.remove(0).get(0).getToken();

        if (functionOrOperator.equals("add")) {
            knot = new OperationAdd();
        } else if (functionOrOperator.equals("sub")) {
            knot = new OperationSub();
        } else if (functionOrOperator.equals("mul")) {
            knot = new OperationMul();
        } else if (functionOrOperator.equals("div")) {
            knot = new OperationDiv();
        } else if (functionOrOperator.equals("pow")) {
            knot = new FunctionPow();
        } else if (functionOrOperator.equals("sin")) {
            knot = new FunctionSin();
        } else if (functionOrOperator.equals("cos")) {
            knot = new FunctionCos();
        } else if (functionOrOperator.equals("tan")) {
            knot = new FunctionTan();
        } else if (functionOrOperator.equals("asin")) {
            knot = new FunctionASin();
        } else if (functionOrOperator.equals("acos")) {
            knot = new FunctionACos();
        } else if (functionOrOperator.equals("atan")) {
            knot = new FunctionATan();
        } else if (functionOrOperator.equals("sinh")) {
            knot = new FunctionSinH();
        } else if (functionOrOperator.equals("cosh")) {
            knot = new FunctionCosH();
        } else if (functionOrOperator.equals("tanh")) {
            knot = new FunctionTanH();
        } else if (functionOrOperator.equals("sqrt")) {
            knot = new FunctionSqrt();
        } else if (functionOrOperator.equals("root")) {
            knot = new FunctionRoot();
        } else if (functionOrOperator.equals("round")) {
            knot = new FunctionRound();
        } else if (functionOrOperator.equals("mod")) {
            knot = new FunctionMod();
        } else if (functionOrOperator.equals("fac")) {
            knot = new FunctionFac();
        } else if (functionOrOperator.equals("gauss")) {
            knot = new FunctionGauss();
        } else if (functionOrOperator.equals("sig") || functionOrOperator.equals("sigmoid")) {
            knot = new FunctionSig();
        } else if (functionOrOperator.endsWith("signum")) {
            knot = new FunctionSignum();
        } else if (functionOrOperator.equals("sum")) {
            knot = new FunctionSum();
        } else if (functionOrOperator.equals("product")) {
            knot = new FunctionProduct();
        }
        else if (functionOrOperator.matches("-?\\d+(\\.\\d+)?")) {
            knot = new FunctionConstant(new BigDecimal(functionOrOperator));
            return knot;
        } else {
            knot = new FunctionVariable(functionOrOperator);
        }

        for (List<Token> tokenList : parameters) {
            if (tokenList.size() == 1) {
                String token = tokenList.get(0).getToken();
                if (token.matches("-?\\d+(\\.\\d+)?")) {
                    knot.addKnot(new FunctionConstant(new BigDecimal(token)));
                } else {
                    knot.addKnot(new FunctionVariable(token));
                }
            } else {
                knot.addKnot(createTree(tokenList));
            }
        }
        return knot;
    }

    private List<List<Token>> parameters(List<Token> tokens) {
        List<List<Token>> parameters = new ArrayList<>();
        if (tokens.size() == 1) {
            parameters.add(tokens);
            return parameters;
        }
        parameters.add(Collections.singletonList(tokens.remove(0)));
        tokens.remove(0);
        tokens.remove(tokens.size() - 1);

        List<Token> t = new ArrayList<>();
        int brackets = 0;
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.getToken().equals("(")) {
                brackets++;
            }
            if (token.getToken().equals(")")) {
                brackets--;
            }
            if (brackets == 0 && token.getToken().equals(",")) {
                parameters.add(t);
                t = new ArrayList<>();
            } else {
                t.add(token);
            }
        }
        if (!t.isEmpty()) {
            parameters.add(t);
        }

        return parameters;
    }

    private class Token {

        private String s;

        public Token(String token) {
            s = token;
        }

        public String getToken() {
            return s;
        }

        @Override
        public String toString() {
            if (true) {
                return s;
            }
            return "Token{" +
                    "s='" + s + '\'' +
                    '}';
        }

    }

}
