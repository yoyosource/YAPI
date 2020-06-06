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

        LinkedList<Byte> linkedList = new LinkedList<>();
        for (byte b : key) {
            linkedList.add(b);
        }

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

    public static void main(String[] args) throws Exception {
        YAPICipher yapiCipher = YAPICipher.getInstance(YAPICipher.DERIVATION);
        yapiCipher.derive(new byte[]{0}, new byte[]{0, 0}, 256);
        // 7B 6E 8D 38 54 FE E9 39 B6 76 A8 E7 68 44 81 40 36 07 07 CD C1 0B C2 76 FB 69 3A 87 DD 50 6C F0 13 6B 0C 0E D2 08 BF F5 44 C7 71 24 1D 91 DA 58 9E F9 9B 21 14 01 B9 6E 34 35 35 8D DC 11 71 65 62 A8 90 51 CA ED 51 CB 67 53 3B 00 7F 84 3B A7 B6 92 4C 49 8F 66 53 41 5F E1 89 88 E6 77 B8 A7 6A CA 4D 58 46 D9 29 71 4B AE D5 BD AA 4B 81 EB DE 51 65 3D CD 91 5A 7F BD 8D 8D 00 00 00 00 00 57 4A B1 52 6F 52 C9 36 8D 4B 07 A3 6B DD 46 DB 5A CD C3 1E C3 FD BB 8A BC 83 C8 73 C8 41 CB 7B 08 B6 00 00 44 27 51 BF B7 DB B0 1C D5 00 17 CE 0B 0D B6 46 1C 6B 07 D7 C6 3A 3A 9B 60 59 3C E9 34 74 7F 8E 44 E4 11 17 BB 62 6A 51 E7 C0 0F A3 CF 09 CF F5 CF B5 8F 8F 8F CC CC FA C0 0B C5 C1 AA 32 41 3F 1E 7A 7D B2 E3 23 AC C6 C6 35 6C 70 36 A8 F0 75 D9 2B E4 49 E3 F9 53 AB 39 2A 1E 17
        // B8 E1 D4 68 24 28 15 D3 4D B8 C8 C1 02 71 5F B5 89 ED 2E 58 81 C1 C3 D8 D6 DC 37 9D 93 A0 65 21 AF 49 A5 46 7B 73 2C 35 45 EC C5 88 6A F2 13 C8 F8 F7 9E 39 EF 0A 80 37 A4 C0 EE 09 CA 9E C6 A7 D6 F8 13 27 3D 07 4D A2 28 81 DE 64 2E 94 3D 47 19 89 C3 F1 FF D1 30 28 14 B1 D8 BD 94 61 46 DC 2C EE EC C1 24 4C 7E F0 6E 5C 1A 33 50 1E CD 8E D9 A2 8D 97 35 F2 31 B4 1D E8 C1 B6 22 E1 54 F1 70 9E 69 C2 4A E5 94 94 80 09 0E F4 2A 68 E2 52 71 19 F7 F4 C3 B4 56 FD 58 2C 5F F9 0B 7E 57 56 63 C7 28 19 70 DA FD A5 DE E3 A9 BD 4C 74 32 23 EB A4 0D 15 29 0B 2F FD 3C DE 02 78 32 9A 77 62 4A 3D B5 79 84 D1 21 0B 23 13 5E 09 34 A1 3C 5E 25 E7 B8 7F 57 98 8B 60 D4 94 A9 18 E5 A8 FF 22 C3 A3 8A A6 84 8E 3A 2B 38 7C 8E BA F0 4C 67 3F E3 D2 37 9E 69 08 2C 55 10 FA 8B E7 EE 37 7B 0E

        // 03 03 83 44 C3 AC A4 6E E3 3D 35 00 73 3E 36 02 73 D1 A5 E6 1D 3C 88 46 BB E4 26 EB 1E C0 0A CA 3B 19 75 42 49 FE 6A 78 11 C6 30 39 44 48 2A 36 5F 9E F8 FB 92 79 DF 47 12 94 F4 83 06 8C EE 7E 1F CB 13 DC 3B 04 1C F6 2B 92 E8 B5 C0 80 DA DE 87 AF 07 DC 2B 12 16 EC 3D 0A 1C F1 34 06 10 E0 31 DA C8 BF 8E FB 35 5D 40 23 6F 9E 15 83 AD 69 A8 5F 57 3C 7B 2A 8E 0C 4D 1A 5C 39 7C EA CC 4C 0D C3 FB B4 03 B4 EC 96 0B 3E 64 41 7C 6C 56 FA 8B 05 39 7A 41 80 34 7A 4F 28 42 0F 42 84 36 7E 4F 17 B7 00 CF AC C4 9E C3 F0 9A 23 2E 9E B4 8C B5 FB DB 08 8F 3E 5A 40 51 AE D0 BD C0 F6 98 28 29 4E 6C 63 7A 6B 75 5D 70 F1 ED E8 FB DD 07 D3 E2 30 84 35 4C 89 1F 97 92 73 67 7E 4D 69 63 83 52 EA FA 03 E2 F1 FF 2B C6 1F A3 1E 25 9F A1 9D A4 0A CA 07 8E 67 51 65 48 89 0D 8C 8F 1F 1D 39
        // 05 33 AB 74 E3 C4 BC 86 FB 3D 45 00 73 4E 26 52 83 79 ED 3E 25 24 50 EE 43 FC 6E 43 26 28 52 F2 43 E1 4D 2A 01 F6 62 F0 99 3E 88 51 BC 70 A2 3E A7 F9 1D C6 0D B0 04 C2 27 FC 1E CB 0E B4 06 C6 27 6F 8F 60 BF 68 B8 8A 97 8E C4 71 CC EC B6 CA DB 0B 53 28 7F 36 72 30 61 4E 20 75 88 F2 34 4C 5D BB F3 B4 E3 98 E0 BA C7 BC 86 DF 22 82 88 60 A9 33 7B 58 5F 3E 7A 50 61 9E E0 BD C0 2E 70 90 11 30 4E 11 4C 1D 43 1F 5A BF FB DE E5 B3 ED E1 C0 80 38 6D 54 59 6F 47 62 4B 7F 76 55 49 73 53 62 14 80 25 54 77 05 71 9C B5 D9 C4 CF 8D 27 83 A2 64 48 71 18 19 3F 37 22 67 4B 7A 19 51 43 83 22 8A AA 9B BA F5 E3 27 82 99 B1 A4 BF 39 2B FF C6 32 42 37 4E 3F 49 35 48 41 45 3C 4F 5B 51 55 44 02 8A FB 0A 89 87 83 8E AB A7 D2 91 BB A5 C9 88 12 1A 17 1E 4F 29 5D 10 19 1D 1C 1F 3F 3D 79
    }
}