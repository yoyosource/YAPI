package yapi.neuralnet.backend.utils;

public class NeuralNetLayerInformation {

    private int size = -1;

    NeuralNetLayerInformation(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("size needs to be at least 1");
        }
        this.size = size;
    }

    public int getSize() {
        return size;
    }

}
