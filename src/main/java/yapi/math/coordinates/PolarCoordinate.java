// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.coordinates;

import yapi.manager.yapion.YAPIONVariable;
import yapi.manager.yapion.value.YAPIONObject;
import yapi.manager.yapion.value.YAPIONValue;

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

    public static PolarCoordinate deserialize(YAPIONObject yapionObject) {
        PolarCoordinate polarCoordinate = null;
        if (yapionObject.getKeys().contains("object-type") && yapionObject.getValue("object-type").getString().equals("polar-coordinate")) {
            polarCoordinate = new PolarCoordinate(yapionObject.getValue("r").getDouble(), yapionObject.getValue("theta").getDouble());
        }
        return polarCoordinate;
    }

    public YAPIONObject serialize() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(new YAPIONVariable("object-type", new YAPIONValue("polar-coordinate")));
        yapionObject.add(new YAPIONVariable("r", new YAPIONValue(value1 + "D")));
        yapionObject.add(new YAPIONVariable("theta", new YAPIONValue(value2 + "D")));
        return yapionObject;
    }

}