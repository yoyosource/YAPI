// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.coordinates;

import yapi.math.vector.Vector;

public class CartesianCoordinate extends Coordinate {

    public CartesianCoordinate() {

    }

    public CartesianCoordinate(double x, double y) {
        value1 = x;
        value2 = y;
    }

    public PolarCoordinate toPolarCoordinate() {
        double r = Math.sqrt(value1 * value1 + value2 * value2);
        return new PolarCoordinate(r, arcCosine(r));
    }

    public Vector toVector() {
        return new Vector(value1, value2);
    }

    private double arcCosine(double r) {
        if (value2 >= 0 && r != 0) {
            return value1 / r;
        } else if (value2 < 0 && r != 0) {
            return -(value1 / r);
        } else {
            // this case should return undefined but I will return 0 instead.
            return 0;
        }
    }

    public double getX() {
        return value1;
    }

    public double getY() {
        return value2;
    }

    @Override
    public String toString() {
        return "PolarCoordinate{" +
                "x=" + value1 +
                ", y=" + value2 +
                '}';
    }

    @Override
    public String type() {
        return "cartesian";
    }

}