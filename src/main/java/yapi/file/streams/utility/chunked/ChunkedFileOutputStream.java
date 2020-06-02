// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.file.streams.utility.chunked;

import yapi.file.streammanagement.OutputAutoClose;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;

public class ChunkedFileOutputStream extends OutputAutoClose {

    private Queue<Byte> bytes = new ArrayDeque<>();
    private boolean openChunk = false;

    public ChunkedFileOutputStream(String name) throws FileNotFoundException {
        super(name);
    }

    public ChunkedFileOutputStream(String name, boolean append) throws FileNotFoundException {
        super(name, append);
    }

    public ChunkedFileOutputStream(File file) throws FileNotFoundException {
        super(file);
    }

    public ChunkedFileOutputStream(File file, boolean append) throws FileNotFoundException {
        super(file, append);
    }

    public ChunkedFileOutputStream(FileDescriptor fdObj) {
        super(fdObj);
    }

    private void writeChunk() throws IOException {
        if (bytes.isEmpty()) return;

        int size = Math.min(bytes.size(), 255);
        byte[] bytes = new byte[size];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = this.bytes.poll();
        }

        super.write((byte) size);
        super.write(bytes);
    }

    @Override
    public void write(int b) throws IOException {
        openChunk = true;
        bytes.add((byte)b);
        if (bytes.size() >= 255) {
            writeChunk();
        }
    }

    @Override
    public void write(byte[] b) throws IOException {
        if (b.length == 0) {
            return;
        }
        openChunk = true;
        for (byte t : b) {
            write(t);
        }
        while (bytes.size() >= 255) {
            writeChunk();
        }
    }

    public void finishChunk() throws IOException {
        endChunk();
    }

    public void endChunk() throws IOException {
        if (!openChunk) {
            return;
        }
        writeChunk();
        this.write(0);
        openChunk = false;
    }

    @Override
    public void closeStream() throws IOException {
        endChunk();
    }
}