// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.string.brackets;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class BalancedBrackets {

    public static boolean isPaired(char leftBracket, char rightBracket) {
        char[][] pairedBrackets = {
                {'(', ')'},
                {'[', ']'},
                {'{', '}'},
                {'<', '>'}
        };
        for (char[] pairedBracket : pairedBrackets) {
            if (pairedBracket[0] == leftBracket && pairedBracket[1] == rightBracket) {
                return true;
            }
        }
        return false;
    }

    public static BalancedBracketsResult isBalanced(String brackets) {
        if (brackets == null) {
            throw new IllegalArgumentException("brackets is null");
        }
        Deque<Character> bracketsStack = new ArrayDeque<>();
        List<BalancedBracketsEntry> entries = new ArrayList<>();
        boolean balanced = true;

        for (int i = 0; i < brackets.length(); i++) {
            char bracket = brackets.charAt(i);
            switch (bracket) {
                case '(':
                case '[':
                case '{':
                case '<':
                    bracketsStack.push(bracket);
                    break;
                case ')':
                case ']':
                case '}':
                case '>':
                    char c = '?';
                    if (bracketsStack.isEmpty() || !isPaired(c = bracketsStack.pop(), bracket)) {
                        entries.add(new BalancedBracketsEntry(c, bracket));
                        balanced = false;
                    }
                    break;
            }
        }
        for (Character c : bracketsStack) {
            entries.add(new BalancedBracketsEntry(c, '?'));
        }
        return new BalancedBracketsResult(bracketsStack.isEmpty() && balanced).setEntries(entries);
    }

}