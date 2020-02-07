package yapi.neuralnet.frontend;

import yapi.neuralnet.backend.utils.NeuralNetLayer;
import yapi.neuralnet.backend.utils.NeuralNetLayerInformation;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetBuilder {

    private NeuralNetLayer neuralNetLayer = new NeuralNetLayer(this);
    private List<NeuralNetLayerInformation> informations = new ArrayList<>();

    public NeuralNetBuilder() {

    }

    public NeuralNetLayer edit() {
        return neuralNetLayer;
    }

    public void addLayer(NeuralNetLayerInformation neuralNetLayerInformation) {
        informations.add(neuralNetLayerInformation);
    }

    public void build() {

    }

}
