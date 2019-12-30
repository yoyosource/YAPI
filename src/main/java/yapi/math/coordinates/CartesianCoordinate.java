package yapi.math.coordinates;

import yapi.datastructures.IntegerBuffer;
import yapi.manager.yapion.YAPIONVariable;
import yapi.manager.yapion.value.YAPIONArray;
import yapi.manager.yapion.value.YAPIONObject;
import yapi.manager.yapion.value.YAPIONValue;
import yapi.math.NumberRandom;
import yapi.math.Vector;

import java.util.ArrayList;
import java.util.List;

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
        } else if (value2 < 0) {
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

    public static CartesianCoordinate deserialize(YAPIONObject yapionObject) {
        CartesianCoordinate cartesianCoordinate = null;
        if (yapionObject.getKeys().contains("object-type") && ((YAPIONValue) yapionObject.getValue("object-type")).getString().equals("cartesian-coordinate")) {
            cartesianCoordinate = new CartesianCoordinate(((YAPIONValue) yapionObject.getValue("x")).getDouble(), ((YAPIONValue) yapionObject.getValue("y")).getDouble());
        }
        return cartesianCoordinate;
    }

    @Override
    public String type() {
        return "cartesian";
    }

    public YAPIONObject serialize() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(new YAPIONVariable("object-type", new YAPIONValue("cartesian-coordinate")));
        yapionObject.add(new YAPIONVariable("x", new YAPIONValue(value1 + "D")));
        yapionObject.add(new YAPIONVariable("y", new YAPIONValue(value2 + "D")));
        return yapionObject;
    }

}
