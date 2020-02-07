package yapi.neuralnet.backend.utils;

public class NeuralNetLayerInformation {

    private int size = -1;

    NeuralNetLayerInformation(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

}
