// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.neuralnet.backend.utils;

import yapi.neuralnet.frontend.NeuralNetBuilder;

public class NeuralNetLayer {

    private NeuralNetBuilder neuralNetBuilder;

    public NeuralNetLayer(NeuralNetBuilder neuralNetBuilder) {
        this.neuralNetBuilder = neuralNetBuilder;
    }

    public NeuralNetLayer add(int size) {
        neuralNetBuilder.addLayer(new NeuralNetLayerInformation(size));
        return this;
    }

}