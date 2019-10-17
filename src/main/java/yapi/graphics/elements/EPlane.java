package yapi.graphics.elements;

import yapi.graphics.Element;
import yapi.math.Vector;

import java.awt.*;

public class EPlane extends Element {

    private Vector v1;
    private Vector v2;

    public EPlane(Color color, Vector sourceVector, Vector v1, Vector v2) {
        super(color, sourceVector);
        this.v1 = v1;
        this.v2 = v2;
    }

    @Override
    public double distance(Vector vector) {
        Vector pos1 = getSourceVector().copy();
        pos1.addVector(v1);
        Vector pos2 = getSourceVector().copy();
        pos2.addVector(v2);

        System.out.println(pos1 + " " + pos2 + " " + vector);
        return super.distance(vector);
    }

    @Override
    public boolean intersect(Vector vector) {
        return super.intersect(vector);
    }
}
