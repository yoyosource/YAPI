// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.neuralnet.backend;

import yapi.math.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NeuronLayer {

    private static Random random = new Random();

    private static Vector createRandomVector(int size) {
        Vector vector = new Vector(size);
        for (int i = 0; i < size; i++) {
            vector.setVector(i, random.nextDouble());
        }
        return vector;
    }



    private List<Neuron> neurons = new ArrayList<>();

    public NeuronLayer(int lastSize, int size) {
        for (int i = 0; i < size; i++) {
            neurons.add(new Neuron(createRandomVector(lastSize)));
        }
    }

}