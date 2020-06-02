// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.calculator.scientific.token;

import yapi.string.brackets.BalancedBrackets;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

public class Tokenizer {

    private String s;
    private int index = 0;

    private Deque<Character> bracketsStack = new ArrayDeque<>();

    public Tokenizer(String s) {
        this.s = s;
    }

    private char get() {
        return s.charAt(index);
    }

    private char peek() {
        if (s.length() < index + 1) {
            throw new IllegalStateException();
        }
        return s.charAt(index + 1);
    }

    private char consume() {
        return s.charAt(index++);
    }

    private boolean hasMore() {
        return index < s.length();
    }

    private int available() {
        return s.length() - index;
    }

    public boolean hasNext() {
        return hasMore();
    }

    public Token next() {
        Token token = new Token();

        while (get() == ' ') {
            consume();
        }

        if (get() == '(') {
            bracketsStack.add(consume());
            return token.setTokenType(TokenType.Bracket_Open).finish();
        }
        if (get() == ')') {
            consumeBracket();
            return token.setTokenType(TokenType.Bracket_Close).finish();
        }
        if (get() == '{') {
            bracketsStack.add(consume());
            return token.setTokenType(TokenType.Function_Open).finish();
        }
        if (get() == '}') {
            consumeBracket();
            return token.setTokenType(TokenType.Function_Close).finish();
        }
        if (get() == ',') {
            consume();
            return token.setTokenType(TokenType.Function_Separator).finish();
        }
        if ((get() >= '!' && get() <= '\'') || (get() >= '*' && get() <= '/') || (get() >= ':' && get() <= '@') || (get() == '\\') || (get() >= '^' && get() <= '`') || (get() == '|') || (get() == '~')) {
            return token.setTokenType(TokenType.Operator_Generic).append(consume()).finish();
        }

        if (Character.isDigit(get())) {
            consumeNumber(token);
        } else if (Character.isAlphabetic(get())) {
            consumeVariable(token);
        } else {
            throw new IllegalStateException();
        }

        return token;
    }

    private void consumeBracket() {
        try {
            if (!BalancedBrackets.isPaired(bracketsStack.pop(), consume())) {
                throw new IllegalStateException();
            }
        } catch (NoSuchElementException e) {
            throw new IllegalStateException();
        }
    }

    private void consumeNumber(Token token) {
        token.append(consume());
        while (hasMore() && Character.isDigit(get())) {
            token.append(consume());
        }
        token.setTokenType(TokenType.Number).finish();
    }

    private void consumeVariable(Token token) {
        token.append(consume());
        while (hasMore() && Character.isAlphabetic(get())) {
            token.append(consume());
        }
        if (hasMore() && get() == '{') {
            token.setTokenType(TokenType.Function).finish();
        } else {
            token.setTokenType(TokenType.Variable).finish();
        }
    }

}