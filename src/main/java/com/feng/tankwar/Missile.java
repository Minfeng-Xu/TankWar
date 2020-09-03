package com.feng.tankwar;

import java.awt.*;

@SuppressWarnings("all")
class Missile {

    public static final int SPEED = 10;
    private int x;

    private int y;

    private final  Direction direction;

    private final boolean enemy;

    public Missile(int x, int y, Direction direction, boolean enemy) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.enemy = enemy;
    }

    private Image getImage() {
        return direction.getImage("missile");
    }

    void move() {
        x += direction.xFactor * SPEED;
        y += direction.yFactor * SPEED;
    }

    void draw(Graphics g) {
        move();
        if(x < 0 || x > 800 || y < 0 || y > 800) {
            return;
        }
        g.drawImage(getImage(), x, y, null);
    }
}
