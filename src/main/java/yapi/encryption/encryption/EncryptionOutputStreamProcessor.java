// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.encryption.encryption;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class EncryptionOutputStreamProcessor extends EncryptionStreamProcessor {

    private OutputStream stream;

    public EncryptionOutputStreamProcessor(FileOutputStream fileOutputStream) {
        this.stream = fileOutputStream;
    }

    public EncryptionOutputStreamProcessor(OutputStream outputStream) {
        this.stream = outputStream;
    }

    public EncryptionOutputStreamProcessor(ByteArrayOutputStream byteArrayOutputStream) {
        this.stream = byteArrayOutputStream;
    }

    @Override
    public void processData(byte[] bytes) {
        try {
            stream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}