package com.example.android.gamesbola;

import android.graphics.Paint;

public class Bola {
    private float x;
    private float y;
    private Paint paint;
    private float radius;
    private float xVelo,yVelo;
    private boolean isInside;

    public Bola(float x, float y, Paint paint, float radius) {
        this.x = x;
        this.y = y;
        this.paint = paint;
        this.radius = radius;
        xVelo = 0;
        yVelo = 0;
        isInside = false;
    }

    public boolean isInside() {
        return isInside;
    }

    public void setInside(boolean inside) {
        isInside = inside;
    }

    public float getxVelo() {
        return xVelo;
    }

    public void setxVelo(float xVelo) {
        this.xVelo = xVelo;
    }

    public float getyVelo() {
        return yVelo;
    }

    public void setyVelo(float yVelo) {
        this.yVelo = yVelo;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }
}
