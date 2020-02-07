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
