package yapi.ui.yapiframe.handler;

import yapi.ui.yapiframe.YAPIComponent;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class YAPIKeyHandler implements KeyListener {

    private YAPIComponent component;

    public YAPIKeyHandler(YAPIComponent component) {
        if (component == null) {
            throw new IllegalArgumentException("component cannot be null");
        }
        this.component = component;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        component.addHandlerPacket(new HandlerPacket(HandlerPacket.KEY_TYPED, e));
    }

    @Override
    public void keyPressed(KeyEvent e) {
        component.addHandlerPacket(new HandlerPacket(HandlerPacket.KEY_PRESSED, e));
    }

    @Override
    public void keyReleased(KeyEvent e) {
        component.addHandlerPacket(new HandlerPacket(HandlerPacket.KEY_RELEASED, e));
    }

}
