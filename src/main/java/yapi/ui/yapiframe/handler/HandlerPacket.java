// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.yapiframe.handler;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class HandlerPacket {

    public static final String KEY_TYPED = "key:typed";
    public static final String KEY_PRESSED = "key:pressed";
    public static final String KEY_RELEASED = "key:released";

    public static final String MOUSE_CLICKED = "mouse:clicked";
    public static final String MOUSE_PRESSED = "mouse:pressed";
    public static final String MOUSE_RELEASED = "mouse:released";
    public static final String MOUSE_ENTERED = "mouse:entered";
    public static final String MOUSE_EXITED = "mouse:exited";
    public static final String MOUSE_DRAGGED = "mouse:dragged";
    public static final String MOUSE_MOVED = "mouse:moved";

    public static final String WHEEL_MOVED = "wheel:moved";

    private static String[] types = new String[]{KEY_TYPED, KEY_PRESSED, KEY_RELEASED, MOUSE_CLICKED, MOUSE_PRESSED, MOUSE_RELEASED, MOUSE_ENTERED, MOUSE_EXITED, MOUSE_DRAGGED, MOUSE_MOVED, WHEEL_MOVED};

    public static int getTypeID(String type) {
        for (int i = 0; i < types.length; i++) {
            if (types[i].equals(type)) {
                return i;
            }
        }
        return -1;
    }

    public static String getTypeString(int id) {
        if (id >= 0) {
            return types[id];
        }
        return null;
    }

    public static int getType(String type) {
        return getTypeID(type);
    }

    public static String getType(int id) {
        return getTypeString(id);
    }

    private int id;
    private InputEvent inputEvent;

    private MouseEvent mouseEvent = null;
    private MouseWheelEvent mouseWheelEvent = null;
    private KeyEvent keyEvent = null;

    private long time = System.currentTimeMillis();
    private boolean consumed = false;

    HandlerPacket(String type, InputEvent inputEvent) {
        this.id = getTypeID(type);
        this.inputEvent = inputEvent;
        init(inputEvent);
    }

    HandlerPacket(int id, InputEvent inputEvent) {
        this.id = id;
        this.inputEvent = inputEvent;
        init(inputEvent);
    }

    private void init(InputEvent inputEvent) {
        if (inputEvent instanceof MouseEvent) {
            mouseEvent = (MouseEvent)inputEvent;
        }
        if (inputEvent instanceof MouseWheelEvent) {
            mouseWheelEvent = (MouseWheelEvent)inputEvent;
        }
        if (inputEvent instanceof KeyEvent) {
            keyEvent = (KeyEvent)inputEvent;
        }
    }

    public String getType() {
        return getTypeString(id);
    }

    public int getID() {
        return id;
    }

    public InputEvent getInputEvent() {
        return inputEvent;
    }

    public MouseEvent getMouseEvent() {
        return mouseEvent;
    }

    public MouseWheelEvent getMouseWheelEvent() {
        return mouseWheelEvent;
    }

    public KeyEvent getKeyEvent() {
        return keyEvent;
    }

    public long getTime() {
        return time;
    }

    public boolean isConsumed() {
        return consumed;
    }

    public void consume() {
        consumed = true;
    }

}