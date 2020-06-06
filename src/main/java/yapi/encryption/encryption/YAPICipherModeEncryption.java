// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.encryption.encryption;

import yapi.internal.exceptions.CipherException;
import yapi.math.NumberRandom;
import yapi.string.HashType;
import yapi.string.StringCrypting;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class YAPICipherModeEncryption implements YAPICipherImpl {

    private static final int BLOCK_SIZE = 16;

    @Override
    public List<Block> partitionSource(byte[] bytes, NumberRandom numberRandom) {
        List<Block> blocks = new ArrayList<>(bytes.length / BLOCK_SIZE + 1);
        byte[] block = getPaddingBlock(numberRandom).blockBytes;
        for (int i = 0; i < bytes.length; i++) {
            if (i % BLOCK_SIZE == 0 && i != 0) {
                blocks.add(new Block(block));
                if (i < bytes.length - BLOCK_SIZE) {
                    block = getPaddingBlock(numberRandom).blockBytes;
                } else {
                    block = new byte[BLOCK_SIZE];
                }
            }
            block[i % BLOCK_SIZE] = bytes[i];
        }
        blocks.add(new Block(block));
        return blocks;
    }

    @Override
    public Block getPaddingBlock(NumberRandom numberRandom) {
        byte[] bytes = new byte[BLOCK_SIZE];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = numberRandom.getByte();
        }
        return new Block(bytes);
    }

    @Override
    public Block getLengthBlock(byte length, NumberRandom numberRandom) {
        byte[] bytes = new byte[BLOCK_SIZE];
        bytes[0] = length;
        for (int i = 0; i < bytes.length - 1; i++) {
            bytes[i + 1] = numberRandom.getByte();
        }
        return new Block(bytes);
    }

    @Override
    public void permuteBlocks(List<Block> blocks, NumberRandom numberFalseRandom) {
        LinkedList<Integer> indices = new LinkedList<>();
        for (int i = 0; i < blocks.size(); i++) {
            indices.add(i);
        }

        while (!indices.isEmpty()) {
            int x1 = numberFalseRandom.getInt(indices.size());
            x1 = indices.remove(x1);
            int x2 = numberFalseRandom.getInt(indices.size());
            x2 = indices.remove(x2);
            Block b = blocks.get(x1);
            blocks.set(x1, blocks.get(x2));
            blocks.set(x2, b);
        }
    }

    @Override
    public void permuteBlocksInternal(List<Block> blocks, boolean nextKeyAfterPermute, NumberRandom numberFalseRandom) {
        byte b = numberFalseRandom.getByte();
        for (int i = 0; i < blocks.size(); i++) {
            byte c = blocks.get(i).blockBytes[BLOCK_SIZE - 1];
            blocks.get(i).permute(getInt(b));
            if (!nextKeyAfterPermute) {
                b = c;
            } else {
                b = blocks.get(i).blockBytes[BLOCK_SIZE - 1];
            }
        }
    }

    @Override
    public void xor(List<Block> blocks, NumberRandom numberTrueRandom) {
        for (int i = 0; i < blocks.size(); i++) {
            byte[] k = new byte[BLOCK_SIZE];
            for (int l = 0; l < k.length; l++) {
                k[l] = numberTrueRandom.getByte();
            }
            blocks.get(i).encrypt(k);
        }
    }

    @Override
    public byte[] assembleOutput(List<Block> blocks) throws CipherException {
        byte[] bytes = new byte[blocks.size() * BLOCK_SIZE];
        for (int i = 0; i < blocks.size(); i++) {
            for (int j = 0; j < BLOCK_SIZE; j++) {
                bytes[i * BLOCK_SIZE + j] = blocks.get(i).blockBytes[j];
            }
        }
        return bytes;
    }

    @Override
    public byte[] crypt(byte[] key, byte[] bytes) throws CipherException {
        key = StringCrypting.hash(key, HashType.SHA256);

        NumberRandom randomNumberRandom = new NumberRandom(getLong(key, 0));

        List<Block> blocks = partitionSource(bytes, randomNumberRandom);
        int bytesAdded = (BLOCK_SIZE - (bytes.length % BLOCK_SIZE)) % BLOCK_SIZE;
        // We need even block size but with the added header we need to ensure that it is still even:
        if (blocks.size() % 2 == 0) {
            bytesAdded += BLOCK_SIZE;
            blocks.add(getPaddingBlock(randomNumberRandom));
        }
        // Produces signed byte but read accounts for that:
        blocks.add(0, getLengthBlock((byte) bytesAdded, randomNumberRandom));
        permuteBlocks(blocks, new NumberRandom(getLong(key, 1)));
        permuteBlocksInternal(blocks,false, new NumberRandom(getLong(key, 2)));
        xor(blocks, new NumberRandom(getLong(key, 3)));

        return assembleOutput(blocks);
    }

    @Override
    public byte[] derive(byte[] salt, byte[] key, int size) throws CipherException {
        throw new CipherException("Unsupported Operation, use 'crypt()' instead");
    }

    @Override
    public void cryptParallel(byte[] key, FileInputStream source, FileOutputStream destination, int threads) throws CipherException {
        throw new CipherException("Unsupported Operation, use 'crypt()' instead");
    }
}