// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.physics.universe;

import ch.obermuhlner.math.big.BigDecimalMath;
import yapi.math.vector.BigVector;

import java.awt.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Random;

public class Body {

    private static MathContext context = new MathContext(100, RoundingMode.HALF_UP);

    private BigDecimal mass;
    private int radius;

    private BigDecimal x;
    private BigDecimal y;
    private boolean fixPosition = false;

    private BigVector force = new BigVector(BigDecimal.ZERO, BigDecimal.ZERO);
    {
        force.setMathContext(context);
    }
    private BigVector tempForce = null;

    private Trail trail = null;

    public Body(BigDecimal mass, int radius, BigDecimal x, BigDecimal y) {
        this.mass = mass;
        this.radius = radius;
        this.x = x;
        this.y = y;
    }

    public Body(BigDecimal mass, int radius, BigDecimal x, BigDecimal y, BigVector initialForce) {
        this.mass = mass;
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.force = initialForce;
    }


    public Body(BigDecimal mass, int radius, double x, double y) {
        this.mass = mass;
        this.radius = radius;
        this.x = BigDecimal.valueOf(x);
        this.y = BigDecimal.valueOf(y);
    }

    public Body(BigDecimal mass, int radius, double x, double y, BigVector initialForce) {
        this.mass = mass;
        this.radius = radius;
        this.x = BigDecimal.valueOf(x);
        this.y = BigDecimal.valueOf(y);
        this.force = initialForce;
    }

    public void update(Body body) {
        if (fixPosition) {
            return;
        }
        if (tempForce == null) {
            tempForce = force;
        }

        BigDecimal ms = body.mass.multiply(mass, context);
        BigDecimal dx = body.x.subtract(x, context).multiply(body.x.subtract(x, context), context);
        BigDecimal dy = body.y.subtract(y, context).multiply(body.y.subtract(y, context), context);
        BigDecimal r = BigDecimalMath.sqrt(dx.add(dy, context), context);

        BigVector vector = new BigVector(body.x.subtract(x, context), body.y.subtract(y, context));
        vector.setMathContext(context);
        if (r.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }
        vector.multiplyVector(ms.divide(r.multiply(r, context), context));

        tempForce.addVector(vector);
    }

    public void update() {
        if (fixPosition) {
            return;
        }
        if (tempForce == null) {
            return;
        }
        force = tempForce;
        tempForce = null;
    }

    public void updatePosition() {
        x = x.add(force.get(0), context);
        y = y.add(force.get(1), context);
    }

    public void updatePosition(int timeStep) {
        if (trail != null) {
            trail.update(x.add(BigDecimal.ZERO, context), y.add(BigDecimal.ZERO, context), radius);
        }

        x = x.add(force.get(0).divide(BigDecimal.valueOf(timeStep), context), context);
        y = y.add(force.get(1).divide(BigDecimal.valueOf(timeStep), context), context);
    }

    public void setFixPosition(boolean fixPosition) {
        this.fixPosition = fixPosition;
    }

    public void setTrail(Trail trail) {
        this.trail = trail;
    }

    public void draw(Graphics2D g) {
        g.setColor(new Color(0, 0, 0));
        g.fillOval(x.intValue() - radius / 2, y.intValue() - radius / 2, radius, radius);

        if (trail != null) {
            trail.paint(g);
        }
    }

    public static BigVector createInitialForce(int bits) {
        return new BigVector(new BigDecimal(new BigInteger(bits, new Random())), new BigDecimal(new BigInteger(bits, new Random())));
    }

    @Override
    public String toString() {
        return "Body{" +
                "mass=" + mass +
                ", radius=" + radius +
                ", x=" + x +
                ", y=" + y +
                ", force=" + force +
                '}';
    }
}