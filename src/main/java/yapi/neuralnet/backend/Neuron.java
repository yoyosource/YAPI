package yapi.neuralnet.backend;

import yapi.math.Vector;

public class Neuron {

    private static double activationFunction(double d) {
        return 1 / (1 + Math.exp(-d));
    }



    private Vector weights;
    private double value = 0;
    private Vector lastInput;

    public Neuron(Vector weights) {
        this.weights = weights;
    }

    public void update(Vector input) {
        lastInput = input;
        value = activationFunction(weights.multiplyVectorScalar(input));
    }

}
