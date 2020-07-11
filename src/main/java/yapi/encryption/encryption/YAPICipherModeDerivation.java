// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.encryption.encryption;

import yapi.array.LinkedListUtils;
import yapi.internal.annotations.yapi.WorkInProgress;
import yapi.internal.annotations.yapi.WorkInProgressType;
import yapi.internal.exceptions.CipherException;
import yapi.math.NumberRandom;
import yapi.string.HashType;
import yapi.string.StringCrypting;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;

@WorkInProgress(context = WorkInProgressType.TESTING)
class YAPICipherModeDerivation implements YAPICipherImpl {

    @Override
    public List<Block> partitionSource(byte[] bytes, NumberRandom numberRandom) {
        return null;
    }

    @Override
    public Block getPaddingBlock(NumberRandom numberRandom) {
        return null;
    }

    @Override
    public Block getLengthBlock(byte length, NumberRandom numberRandom) {
        return null;
    }

    @Override
    public void permuteBlocks(List<Block> blocks, NumberRandom numberFalseRandom) {

    }

    @Override
    public void permuteBlocksInternal(List<Block> blocks, boolean nextKeyAfterPermute, NumberRandom numberFalseRandom) {

    }

    @Override
    public void xor(List<Block> blocks, NumberRandom numberTrueRandom) {

    }

    @Override
    public byte[] assembleOutput(List<Block> blocks) throws CipherException {
        return new byte[0];
    }

    @Override
    public byte[] crypt(byte[] key, byte[] bytes) throws CipherException {
        throw new CipherException("Unsupported Operation, use 'derive()' instead");
    }

    @Override
    public byte[] derive(byte[] salt, byte[] key, int size) throws CipherException {
        if (key.length < 64) return derive(salt, StringCrypting.hash(key, HashType.SHA512), size);
        if (salt.length != 64) return derive(StringCrypting.hash(salt, HashType.SHA512), key, size);

        LinkedList<Byte> linkedList = LinkedListUtils.asList(key);

        while (linkedList.size() < size) {
            byte b = linkedList.removeFirst();
            byte c = linkedList.getFirst();
            linkedList.add(b);
            linkedList.add((byte) ((b ^ c) + ((b & 0xFF) % linkedList.size()) + linkedList.size()));
        }

        LinkedList<Byte> bytes = new LinkedList<>();
        int index = 0;
        while (!linkedList.isEmpty()) {
            bytes.add((byte)(linkedList.removeFirst() ^ salt[index]));
            index++;
            if (index >= salt.length) {
                index = 0;
                salt = StringCrypting.hash(salt, HashType.SHA512);
            }
        }

        // System.out.println(StringFormatting.toHex(LinkedListUtils.toByteArray(bytes), true));
        // System.out.println(bytes.size());

        return LinkedListUtils.toByteArray(bytes);
    }

    @Override
    public void cryptParallel(byte[] key, FileInputStream source, FileOutputStream destination, int threads) throws CipherException {
        throw new CipherException("Unsupported Operation, use 'crypt()' instead");
    }
}