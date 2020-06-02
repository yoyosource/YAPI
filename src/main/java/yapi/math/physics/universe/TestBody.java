// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.physics.universe;

import yapi.math.vector.BigVector;
import yapi.runtime.ThreadUtils;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class TestBody {

    public static void main(String[] args) {
        Body body1 = new Body(BigDecimal.valueOf(10), 20, 500, 300, new BigVector(new BigDecimal(150), new BigDecimal(0)));
        body1.setTrail(new Trail(0.0001));
        Body body3 = new Body(BigDecimal.valueOf(30), 20, 500, 100, new BigVector(new BigDecimal(-180), new BigDecimal(0)));
        body3.setTrail(new Trail(0.0001));
        Body body2 = new Body(BigDecimal.valueOf(100), 20, 500, 500);
        body2.setFixPosition(true);

        int timeStep = 30;

        JFrame jFrame = new JFrame();
        jFrame.setSize(1000, 1000);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
        jFrame.setContentPane(new JComponent() {
            @Override
            protected void paintChildren(Graphics g) {
                body1.update(body2);
                body1.update(body3);

                body2.update(body1);
                body2.update(body3);

                body3.update(body1);
                body3.update(body2);

                body1.update();
                body2.update();
                body3.update();

                body1.updatePosition(timeStep);
                body2.updatePosition(timeStep);
                body3.updatePosition(timeStep);

                Graphics2D graphics2D = (Graphics2D)g;
                body1.draw(graphics2D);
                body2.draw(graphics2D);
                body3.draw(graphics2D);
            }
        });
        jFrame.validate();

        Runnable runnable = () -> {
            while (true) {
                jFrame.repaint();
                ThreadUtils.sleep(1000/timeStep);
            }
        };
        Thread t = new Thread(runnable);
        t.setName("Test");
        t.start();
    }

}