// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.physics.universe;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Trail {

    private List<TrailValue> values = new ArrayList<>();
    private double radiusDecrease = 0.1;

    public Trail() {

    }

    public Trail(double radiusDecrease) {
        this.radiusDecrease = radiusDecrease;
    }

    public void paint(Graphics2D g) {
        values.forEach((TrailValue t) -> {
            g.setColor(new Color(0, 0, 0, 50));
            g.fillOval((int)(t.x.intValue() - t.radius / 2), (int)(t.y.intValue() - t.radius / 2), (int)t.radius, (int)t.radius);
        });
    }

    public void update(BigDecimal x, BigDecimal y, int radius) {
        values.add(new TrailValue(x, y, radius));
        values.forEach((TrailValue t) -> {
            t.radius = t.radius - (t.radius * radiusDecrease);
        });
        values = values.stream().filter((TrailValue t) -> t.radius > 1).collect(Collectors.toList());
    }

    private class TrailValue {

        public TrailValue(BigDecimal x, BigDecimal y, double radius) {
            this.x = x;
            this.y = y;
            this.radius = radius;
        }

        private BigDecimal x;
        private BigDecimal y;
        private double radius;

    }

}