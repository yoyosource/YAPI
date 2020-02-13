// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.neuralnet.frontend;

import yapi.neuralnet.backend.utils.NeuralNetLayer;
import yapi.neuralnet.backend.utils.NeuralNetLayerInformation;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetBuilder {

    private NeuralNetLayer neuralNetLayer = new NeuralNetLayer(this);
    private List<NeuralNetLayerInformation> information = new ArrayList<>();

    public NeuralNetBuilder() {

    }

    public NeuralNetLayer edit() {
        return neuralNetLayer;
    }

    public void addLayer(NeuralNetLayerInformation neuralNetLayerInformation) {
        information.add(neuralNetLayerInformation);
    }

    public NeuralNet build() {
        return new NeuralNet(information);
    }

}