// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.string.brackets;

public class BalancedBracketsEntry {

    private char openBracket;
    private char closeBracket;

    private int openBracketIndex = -1;
    private int closeBracketIndex = -1;

    public BalancedBracketsEntry(char openBracket, char closeBracket) {
        this.openBracket = openBracket;
        this.closeBracket = closeBracket;
    }

    public BalancedBracketsEntry(char openBracket, char closeBracket, int openBracketIndex, int closeBracketIndex) {
        this.openBracket = openBracket;
        this.closeBracket = closeBracket;
        this.openBracketIndex = openBracketIndex;
        this.closeBracketIndex = closeBracketIndex;
    }

    @Override
    public String toString() {
        if (openBracketIndex == -1 && closeBracketIndex == -1) {
            return openBracket + "" + closeBracket + ":" + "?>?";
        }
        if (openBracketIndex == -1) {
            return openBracket + "" + closeBracket + ":" + "?>" + closeBracketIndex;
        }
        if (closeBracketIndex == -1)  {
            return openBracket + "" + closeBracket + ":" + openBracketIndex + ">?";
        }
        return openBracket + "" + closeBracket + ":" + openBracketIndex + ">" + closeBracketIndex;
    }

}