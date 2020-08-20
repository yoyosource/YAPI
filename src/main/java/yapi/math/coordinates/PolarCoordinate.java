// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.coordinates;

public class PolarCoordinate extends Coordinate {

    public PolarCoordinate() {

    }

    public PolarCoordinate(double r, double theta) {
        value1 = r;
        value2 = theta;
    }

    public CartesianCoordinate toCartesianCoordinate() {
        return new CartesianCoordinate(value1 * Math.cos(value2), value1 * Math.sin(value2));
    }

    public double getR() {
        return value1;
    }

    public double getTheta() {
        return value2;
    }

    @Override
    public String toString() {
        return "PolarCoordinate{" +
                "r=" + value1 +
                ", theta=" + value2 +
                '}';
    }

    @Override
    public String type() {
        return "polar";
    }

}