package yapi.graphics;

import yapi.math.Vector;

import java.awt.*;

public class Element {

    private Color color;
    private Vector sourceVector;

    public Element(Color color, Vector sourceVector) {
        this.color = color;
        this.sourceVector = sourceVector;
    }

    public final Color getColor() {
        return color;
    }

    public final void setColor(Color color) {
        this.color = color;
    }

    public final Vector getSourceVector() {
        return sourceVector;
    }

    public final void setSourceVector(Vector sourceVector) {
        this.sourceVector = sourceVector;
    }

    public double distance(Vector vector) {
        return vector.lenght();
    }

    public boolean intersect(Vector vector) {
        return false;
    }

}
