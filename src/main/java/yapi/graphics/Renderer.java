package yapi.graphics;

import yapi.math.Vector;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Renderer extends JComponent {

    private Vector viewOffset =    new Vector(100, 100, 0);
    private Vector viewDirection = new Vector(0, 0, 0);

    private List<Element> elementList = new ArrayList<>();

    public Renderer() {

    }

    public void setViewOffset(Vector viewOffset) {
        this.viewOffset = viewOffset;
    }

    public void setViewDirection(Vector viewDirection) {
        this.viewDirection = viewDirection;
    }

    public void paint(Graphics g) {
        paintGraphics((Graphics2D)g);
    }

    public void paint(Graphics2D g){
        paintGraphics(g);
    }

    private void paintGraphics(Graphics2D g) {
        Rectangle rectangle = g.getClipBounds();

        for (Element element : elementList) {
            element.distance(viewOffset);
        }

        g.setColor(new Color(0, 0, 0, 255));
        g.drawLine(0, 0, (int)viewOffset.get(0), (int)viewOffset.get(1));
    }

    public void addElement(Element element) {
        elementList.add(element);
    }

    @Override
    protected void paintComponent(Graphics g) {
        paintGraphics((Graphics2D)g);
    }

}
