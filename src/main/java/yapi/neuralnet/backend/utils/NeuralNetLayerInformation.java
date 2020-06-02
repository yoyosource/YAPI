// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.neuralnet.backend.utils;

import yapi.internal.runtimeexceptions.NeuralNetException;

public class NeuralNetLayerInformation {

    private int size = -1;

    NeuralNetLayerInformation(int size) {
        if (size < 1) {
            throw new NeuralNetException("size needs to be at least 1");
        }
        this.size = size;
    }

    public int getSize() {
        return size;
    }

}