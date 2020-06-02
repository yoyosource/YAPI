// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.calculator.scientific;

import ch.obermuhlner.math.big.BigDecimalMath;
import yapi.internal.annotations.yapi.WorkInProgress;
import yapi.internal.annotations.yapi.WorkInProgressType;
import yapi.math.calculator.scientific.function.Function;
import yapi.math.calculator.scientific.operator.Operator;
import yapi.math.calculator.scientific.operator.UnaryOperator;
import yapi.math.calculator.scientific.token.Token;
import yapi.math.calculator.scientific.token.TokenType;
import yapi.math.calculator.scientific.token.Tokenizer;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WorkInProgress(context = WorkInProgressType.ALPHA)
public class CalculatorScientific {

    public static void main(String[] args) {
        getInstance("1+1")
                .editMathContext()
                    .setInfiniteMathContext()
                    .finish()
                .eval();
        getInstance("a+b");
        getInstance("a x+b");
        getInstance("2x^2+7x+x+2");
        getInstance("c+x+b");
        getInstance("6*6");
        getInstance("6*6+6");
        getInstance("6/7");
        getInstance("6/(7*4)");
        getInstance("6/sin{7*4}");
        getInstance("6/sum{7*4,4}");
        getInstance("5!+1");
        getInstance("5(1)6");
    }

    public static CalculatorScientific getInstance(String mathExpression) {
        return new CalculatorScientific(mathExpression);
    }

    private List<Token> tokens = new ArrayList<>();

    private FunctionList functionList = new FunctionList();
    private OperatorList operatorList = new OperatorList();
    private UnaryOperatorList unaryOperatorList = new UnaryOperatorList();

    private Context context = new Context();

    private int precision = 7;
    private RoundingMode roundingMode = RoundingMode.HALF_EVEN;

    public CalculatorScientific(String mathExpression) {
        Tokenizer tokenizer = new Tokenizer(mathExpression);
        while (tokenizer.hasNext()) {
            Token t = tokenizer.next();
            if (t.getTokenType() == TokenType.Function_Open && !tokens.isEmpty() && tokens.get(tokens.size() - 1).getTokenType() != TokenType.Function) {
                throw new IllegalStateException();
            }
            multiplicationCorrecter(t);
            tokens.add(t);
        }
        System.out.println(getMathExpression());
        System.out.println(getMathExpressionToken());
        System.out.println();
    }

    private void multiplicationCorrecter(Token token) {
        if (tokens.isEmpty()) {
            return;
        }
        Token lastToken = tokens.get(tokens.size() - 1);

        if ((lastToken.getTokenType() == TokenType.Variable || lastToken.getTokenType() == TokenType.Number) && (token.getTokenType() == TokenType.Variable || token.getTokenType() == TokenType.Number)) {
            tokens.add(new Token().setTokenType(TokenType.Operator).append("*").setHidden(true).finish());
        }
        if ((lastToken.getTokenType() == TokenType.Variable || lastToken.getTokenType() == TokenType.Number) && (token.getTokenType() == TokenType.Bracket_Open || token.getTokenType() == TokenType.Function)) {
            tokens.add(new Token().setTokenType(TokenType.Operator).append("*").setHidden(true).finish());
        }
        if ((lastToken.getTokenType() == TokenType.Bracket_Close || lastToken.getTokenType() == TokenType.Function_Close) && (token.getTokenType() == TokenType.Variable || token.getTokenType() == TokenType.Number)) {
            tokens.add(new Token().setTokenType(TokenType.Operator).append("*").setHidden(true).finish());
        }
    }

    private MathContext getMathContext() {
        return new MathContext(precision, roundingMode);
    }

    public String getMathExpressionToken() {
        return tokens.stream().map(Token::toString).collect(Collectors.joining(" "));
    }

    public String getMathExpression() {
        StringBuilder st = new StringBuilder();
        for (Token token : tokens) {
            if (token.isHidden()) {
                if (token.getValue().equals("*")) {
                    st.append(token.getValue());
                }
                continue;
            }
            String value = token.getValue();
            if (value.isEmpty()) {
                switch (token.getTokenType()) {
                    case Function_Open:
                        value = "{";
                        break;
                    case Function_Close:
                        value = "}";
                        break;
                    case Bracket_Open:
                        value = "(";
                        break;
                    case Bracket_Close:
                        value = ")";
                        break;
                    case Function_Separator:
                        value = ",";
                        break;
                }
            }
            st.append(value);
        }
        return st.toString();
    }


    public CalculatorScientific setFunctionList(FunctionList functionList) {
        this.functionList = functionList;
        return this;
    }

    public CalculatorScientific setOperatorList(OperatorList operatorList) {
        this.operatorList = operatorList;
        return this;
    }

    public CalculatorScientific setOperatorList(UnaryOperatorList unaryOperatorList) {
        this.unaryOperatorList = unaryOperatorList;
        return this;
    }

    public CalculatorScientific setContext(Context context) {
        this.context = context;
        return this;
    }


    public class MathContextEditor {

        private CalculatorScientific calculatorScientific;

        public MathContextEditor(CalculatorScientific calculatorScientific) {
            this.calculatorScientific = calculatorScientific;
        }

        public MathContextEditor setPrecision(int precision) {
            calculatorScientific.precision = precision;
            return this;
        }

        public MathContextEditor setRoundMode(RoundingMode roundMode) {
            calculatorScientific.roundingMode = roundMode;
            return this;
        }


        public MathContextEditor setMathContext(MathContext mathContext) {
            precision = mathContext.getPrecision();
            roundingMode = mathContext.getRoundingMode();
            return this;
        }

        public MathContextEditor setDecimal32MathContext() {
            setMathContext(MathContext.DECIMAL32);
            return this;
        }

        public MathContextEditor setDecimal64MathContext() {
            return setExtendedMathContext();
        }

        public MathContextEditor setDecimal128MathContext() {
            return setExtremeMathContext();
        }

        public MathContextEditor setDecimalUnlimitedMathContext() {
            return setInfiniteMathContext();
        }

        public MathContextEditor setDefaultMathContext() {
            setMathContext(MathContext.DECIMAL32);
            return this;
        }

        public MathContextEditor setExtendedMathContext() {
            setMathContext(MathContext.DECIMAL64);
            return this;
        }

        public MathContextEditor setExtremeMathContext() {
            setMathContext(MathContext.DECIMAL128);
            return this;
        }

        public MathContextEditor setInfiniteMathContext() {
            setMathContext(MathContext.UNLIMITED);
            return this;
        }

        public CalculatorScientific finish() {
            return calculatorScientific;
        }

    }

    public MathContextEditor editMathContext() {
        return new MathContextEditor(this);
    }


    public class FunctionEditor {

        private CalculatorScientific calculatorScientific;

        FunctionEditor(CalculatorScientific calculatorScientific) {
            this.calculatorScientific = calculatorScientific;
        }

        public FunctionEditor addFunction(Function function) {
            functionList.addFunction(function);
            return this;
        }

        public CalculatorScientific finish() {
            return calculatorScientific;
        }

    }

    public class OperatorEditor {

        private CalculatorScientific calculatorScientific;

        OperatorEditor(CalculatorScientific calculatorScientific) {
            this.calculatorScientific = calculatorScientific;
        }

        public OperatorEditor addOperator(Operator operator) {
            operatorList.addOperator(operator);
            return this;
        }

        public OperatorEditor addUnaryOperator(UnaryOperator unaryOperator) {
            unaryOperatorList.addOperator(unaryOperator);
            return this;
        }

        public CalculatorScientific finish() {
            return calculatorScientific;
        }

    }

    public FunctionEditor editFunctions() {
        return new FunctionEditor(this);
    }

    public OperatorEditor editOperator() {
        return new OperatorEditor(this);
    }


    public class ContextEditor {

        private CalculatorScientific calculatorScientific;

        ContextEditor(CalculatorScientific calculatorScientific) {
            this.calculatorScientific = calculatorScientific;
        }

        public ContextEditor addVariable(String variable, BigDecimal value) {
            context.addVariable(variable, value);
            return this;
        }

        public ContextEditor addVariable(String variable, String value) {
            return addVariable(variable, new BigDecimal(value));
        }

        public ContextEditor addVariable(String variable, long value) {
            return addVariable(variable, BigDecimal.valueOf(value));
        }

        public ContextEditor addVariable(String variable, double value) {
            return addVariable(variable, BigDecimal.valueOf(value));
        }

        public CalculatorScientific finish() {
            return calculatorScientific;
        }

    }

    public ContextEditor editContext() {
        return new ContextEditor(this);
    }


    private void createContextConstants() {
        context.addVariable("pi", BigDecimalMath.pi(getMathContext()));
        context.addVariable("e", BigDecimalMath.e(getMathContext()));
    }

    public BigDecimal eval() {
        createContextConstants();
        return null;
    }

    public BigDecimal evalDerivative(String variable) {
        createContextConstants();
        return null;
    }

    public BigDecimal evalInverseDerivative(String variable) {
        createContextConstants();
        return null;
    }

}