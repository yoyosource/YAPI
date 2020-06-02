// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.encryption.encryption;

import yapi.math.NumberRandom;
import yapi.string.StringFormatting;

import java.util.*;

public class Block {

    private static Map<Integer, int[]> permutationMap = new HashMap<>();

    byte[] blockBytes;

    public Block(byte[] blockBytes) {
        this.blockBytes = blockBytes;
    }

    public String toString() {
        return StringFormatting.toHex(blockBytes);
    }

    void permute(int key) {
        int[] permutation;
        if (!permutationMap.containsKey(key)) {
            permutation = createPermutation(key);
            permutationMap.put(key, permutation);
        } else {
            permutation = permutationMap.get(key);
        }

        for (int i = 0; i < permutation.length; i += 2) {
            int x1 = permutation[i + 0];
            int x2 = permutation[i + 1];

            byte b = blockBytes[x1];
            blockBytes[x1] = blockBytes[x2];
            blockBytes[x2] = b;
        }
    }

    private int[] createPermutation(int key) {
        LinkedList<Integer> indices = new LinkedList<>();
        NumberRandom numberRandom = new NumberRandom(key);
        for (int i = 0; i < blockBytes.length; i++) {
            indices.add(i);
        }
        int[] permutation = new int[blockBytes.length];
        int index = 0;
        while (!indices.isEmpty()) {
            int x1 = numberRandom.getInt(indices.size());
            x1 = indices.remove(x1);
            int x2 = numberRandom.getInt(indices.size());
            x2 = indices.remove(x2);

            permutation[index++] = x1;
            permutation[index++] = x2;
        }

        return permutation;
    }

    void encrypt(byte[] bytes) {
        if (bytes.length != blockBytes.length) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < bytes.length; i++) {
            blockBytes[i] ^= bytes[i];
        }
    }

}