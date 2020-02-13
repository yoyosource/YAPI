// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.yapiframe;

import yapi.ui.yapiframe.handler.HandlerPacket;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class YAPIComponent extends JComponent {

    private YAPIFrame frame;
    private YAPIView view = null;

    private List<HandlerPacket> handlerPackets = new ArrayList<>();

    YAPIComponent(YAPIFrame frame) {
        this.frame = frame;
    }

    public void addHandlerPacket(HandlerPacket packet) {
        handlerPackets.add(packet);
    }

    public void setView(YAPIView view) {
        this.view = view;
    }

    @Override
    protected void paintComponent(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;

        for (int i = handlerPackets.size() - 1; i >= 0; i--) {
            if (handlerPackets.get(i).isConsumed()) {
                handlerPackets.remove(i);
                continue;
            }
            if (System.currentTimeMillis() - handlerPackets.get(i).getTime() > 10) {
                handlerPackets.remove(i);
            }
        }

        if (view != null) {
            view.paint(g);
        }
    }
}