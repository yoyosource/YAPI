// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.neuralnet.frontend;

import yapi.exceptions.NeuralNetException;
import yapi.neuralnet.backend.InputLayer;
import yapi.neuralnet.backend.NeuronLayer;
import yapi.neuralnet.backend.utils.NeuralNetLayerInformation;

import java.util.ArrayList;
import java.util.List;

public class NeuralNet {

    private InputLayer inputLayer;
    private List<NeuronLayer> layers = new ArrayList<>();

    NeuralNet(List<NeuralNetLayerInformation> information) {
        if (information.isEmpty()) {
            throw new NeuralNetException("At least one layer is needed");
        }
        int lastSize = 0;
        for (NeuralNetLayerInformation neuralNetLayerInformation : information) {
            if (inputLayer == null) {
                inputLayer = new InputLayer(neuralNetLayerInformation.getSize());
                lastSize = neuralNetLayerInformation.getSize();
            } else {
                layers.add(new NeuronLayer(lastSize, neuralNetLayerInformation.getSize()));
                lastSize = neuralNetLayerInformation.getSize();
            }
        }
    }

}