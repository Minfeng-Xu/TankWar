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

    Image getImage() {
        switch (direction) {
            case UP: return utils.getImage("missileU.gif");
            case UPLEFT: return utils.getImage("missileLU.gif");
            case UPRIGHT: return utils.getImage("missileRU.gif");
            case DOWN: return utils.getImage("missileD.gif");
            case DOWNLEFT: return utils.getImage("missileLD.gif");
            case DOWNRIGHT:  return utils.getImage("missileRD.gif");
            case LEFT: return utils.getImage("missileL.gif");
            case RIGHT: return utils.getImage("missileR.gif");
        }
        return null;
    }

    void move() {
        switch (direction) {
            case UP: y-= Speed; break;
            case UPLEFT: y-= Speed; x-= Speed; break;
            case UPRIGHT: y-= Speed; x+= Speed; break;
            case DOWN: y+= Speed; break;
            case DOWNLEFT: y+= Speed; x-= Speed; break;
            case DOWNRIGHT: y+= Speed; x+= Speed; break;
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
