// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.calculator.scientific.token;

public class Token {

    private TokenType tokenType = TokenType.Unknown;

    private boolean hidden = false;

    private StringBuilder st = new StringBuilder();
    private String value;

    public Token() {

    }

    public Token setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    public Token setHidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    public Token append(String s) {
        st.append(s);
        return this;
    }

    public Token append(char c) {
        st.append(c);
        return this;
    }

    String getTempValue() {
        return st.toString();
    }

    public boolean isHidden() {
        return hidden;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getValue() {
        return value;
    }

    public Token finish() {
        value = st.toString();
        return this;
    }

    @Override
    public String toString() {
        if (value.length() == 0) {
            return "<" + tokenType.toString() + ">";
        }
        return "<" + tokenType.toString() + ":" + value + ">";
    }
}