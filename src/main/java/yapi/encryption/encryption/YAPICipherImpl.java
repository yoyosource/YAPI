// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.encryption.encryption;

import yapi.internal.exceptions.CipherException;
import yapi.math.NumberRandom;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public interface YAPICipherImpl {

    default long getLong(byte[] bytes, int index) {
        long l = 1;
        long r = 0;
        for (int i = 0; i < 8; i++) {
            r += getInt(bytes[i + 8 * index]) * l;
            l *= 256;
        }
        return r;
    }

    default int getInt(byte b) {
        return ((int)b) & 0xFF;
    }

    default List<Block> partitionSource(byte[] bytes, NumberRandom numberRandom) {
        return new ArrayList<>();
    }

    default Block getPaddingBlock(NumberRandom numberRandom) {
        return null;
    }

    default Block getLengthBlock(byte length, NumberRandom numberRandom) {
        return null;
    }

    default void permuteBlocks(List<Block> blocks, NumberRandom numberFalseRandom) {

    }

    default void permuteBlocksInternal(List<Block> blocks, boolean nextKeyAfterPermute, NumberRandom numberFalseRandom) {

    }

    default void xor(List<Block> blocks, NumberRandom numberTrueRandom) {

    }

    byte[] assembleOutput(List<Block> blocks) throws CipherException;

    byte[] crypt(byte[] key, byte[] bytes) throws CipherException;

    byte[] derive(byte[] salt, byte[] key, int size) throws CipherException;

    void cryptParallel(byte[] key, File source, File destination, int threads) throws CipherException;

}