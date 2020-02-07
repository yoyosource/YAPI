package yapi.ui.yapiframe.handler;

import yapi.ui.yapiframe.YAPIComponent;

import java.awt.event.*;

public class YAPIMouseHandler implements MouseListener, MouseWheelListener, MouseMotionListener {

    private YAPIComponent component;

    public YAPIMouseHandler(YAPIComponent component) {
        if (component == null) {
            throw new IllegalArgumentException("component cannot be null");
        }
        this.component = component;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        component.addHandlerPacket(new HandlerPacket(HandlerPacket.MOUSE_CLICKED, e));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        component.addHandlerPacket(new HandlerPacket(HandlerPacket.MOUSE_PRESSED, e));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        component.addHandlerPacket(new HandlerPacket(HandlerPacket.MOUSE_RELEASED, e));
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        component.addHandlerPacket(new HandlerPacket(HandlerPacket.MOUSE_ENTERED, e));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        component.addHandlerPacket(new HandlerPacket(HandlerPacket.MOUSE_EXITED, e));
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        component.addHandlerPacket(new HandlerPacket(HandlerPacket.WHEEL_MOVED, e));
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        component.addHandlerPacket(new HandlerPacket(HandlerPacket.MOUSE_DRAGGED, e));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        component.addHandlerPacket(new HandlerPacket(HandlerPacket.MOUSE_MOVED, e));
    }

}
