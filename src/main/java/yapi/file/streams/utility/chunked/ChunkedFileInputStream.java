// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.file.streams.utility.chunked;

import yapi.file.streammanagement.Input;
import yapi.internal.exceptions.file.ChunkedIOException;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;

public class ChunkedFileInputStream extends Input {

    private boolean chunkEnd = false;
    private Queue<Byte> bytes = new ArrayDeque<>();

    public ChunkedFileInputStream(String name) throws FileNotFoundException {
        super(name);
    }

    public ChunkedFileInputStream(File file) throws FileNotFoundException {
        super(file);
    }

    public ChunkedFileInputStream(FileDescriptor fdObj) {
        super(fdObj);
    }

    private void readChunk() throws IOException {
        if (!bytes.isEmpty()) {
            return;
        }

        int size = super.read();
        if (size == 0) {
            chunkEnd = true;
            return;
        }
        while (size > 0) {
            bytes.add((byte)super.read());
            size--;
        }
    }

    @Override
    public int read() throws IOException {
        if (chunkEnd) {
            throw new ChunkedIOException("End of Chunk reached");
        }
        if (bytes.isEmpty()) {
            readChunk();
        }
        return bytes.poll();
    }

    public void nextChunk() throws ChunkedIOException {
        if (!chunkEnd) {
            throw new ChunkedIOException("Cannot open next Chunk, while previous is open");
        }
        chunkEnd = false;
    }

    public void skipToChunkEnd() throws IOException {
        while (!chunkEnd) {
            readChunk();
            bytes.clear();
        }
    }
}