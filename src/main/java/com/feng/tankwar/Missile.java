package com.feng.tankwar;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("all")
class Missile {

    public static final int Speed = 10;
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
        switch (direction) {
            case UP: y-= Speed; break;
            case LEFT_UP: y-= Speed; x-= Speed; break;
            case RIGHT_UP: y-= Speed; x+= Speed; break;
            case DOWN: y+= Speed; break;
            case LEFT_DOWN: y+= Speed; x-= Speed; break;
            case RIGHT_DOWN: y+= Speed; x+= Speed; break;
            case LEFT: x-= Speed; break;
            case RIGHT: x+= Speed; break;
        }
    }

    void draw(Graphics g) {
        move();
        if(x < 0 || x > 800 || y < 0 || y > 800) {
            return;
        }
        g.drawImage(getImage(), x, y, null);
    }
}
