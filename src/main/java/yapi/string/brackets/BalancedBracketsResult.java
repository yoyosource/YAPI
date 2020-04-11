// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.string.brackets;

import java.util.ArrayList;
import java.util.List;

public class BalancedBracketsResult {

    private boolean isBalanced;
    private List<BalancedBracketsEntry> entries = new ArrayList<>();

    BalancedBracketsResult(boolean isBalanced) {
        this.isBalanced = isBalanced;
    }

    BalancedBracketsResult setEntries(List<BalancedBracketsEntry> entries) {
        this.entries = entries;
        return this;
    }

    public boolean isBalanced() {
        return isBalanced;
    }

    public List<BalancedBracketsEntry> getEntries() {
        return entries;
    }

    @Override
    public String toString() {
        return "BalancedBracketsResult{" +
                "isBalanced=" + isBalanced +
                ", entries=" + entries +
                '}';
    }

}