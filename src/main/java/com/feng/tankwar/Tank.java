package com.feng.tankwar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

@SuppressWarnings("all")
public class Tank {

    private int x;
    private int y;

    private boolean enemy;

    private Direction direction;

    public Tank(int x, int y, Direction direction) {
        this(x, y, false, direction);
    }

    public Tank(int x, int y, boolean enemy, Direction direction) {
        this.x = x;
        this.y = y;
        this.enemy = enemy;
        this.direction = direction;
    }

    Image getImage() {
        String prefix = enemy? "e":"";
        switch (direction) {
            case UP: return new ImageIcon("assets/images/"+prefix+"tankU.gif").getImage();
            case UPLEFT: return new ImageIcon("assets/images/"+prefix+"tankLU.gif").getImage();
            case UPRIGHT: return new ImageIcon("assets/images/"+prefix+"tankRU.gif").getImage();
            case DOWN: return new ImageIcon("assets/images/"+prefix+"tankD.gif").getImage();
            case DOWNLEFT: return new ImageIcon("assets/images/"+prefix+"tankLD.gif").getImage();
            case DOWNRIGHT: return new ImageIcon("assets/images/"+prefix+"tankRD.gif").getImage();
            case LEFT: return new ImageIcon("assets/images/"+prefix+"tankL.gif").getImage();
            case RIGHT: return new ImageIcon("assets/images/"+prefix+"tankR.gif").getImage();
        }
        return null;
    }

    void move() {
        if(this.stopped) return;
        switch (direction) {
            case UP: y-=5; break;
            case UPLEFT: y-=5; x-=5; break;
            case UPRIGHT: y-=5; x+=5; break;
            case DOWN: y+=5; break;
            case DOWNLEFT: y+=5; x-=5; break;
            case DOWNRIGHT: y+=5; x+=5; break;
            case LEFT: x-=5; break;
            case RIGHT: x+=5; break;
        }
    }

    void draw(Graphics g) {
        this.determineDirection();
        this.move();
        if(x < 0) x = 0;
        else if (x>800 - getImage().getWidth(null)) x=800 - getImage().getWidth(null);
        if(y < 0) y = 0;
        else if (y> 600 - getImage().getHeight(null)) y = 600 - getImage().getHeight(null);
        g.drawImage(this.getImage(), this.x, this.y, null );
    }

    private boolean up, down, left, right;

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP: up = true; break;
            case KeyEvent.VK_DOWN: down = true; break;
            case KeyEvent.VK_LEFT: left = true; break;
            case KeyEvent.VK_RIGHT: right = true; break;
        }

    }

    private boolean stopped;

    private void determineDirection() {
        if(!up && !left && !down && !right) this.stopped = true;
        else {
            this.stopped = false;
            if (up && left && !down && !right) this.direction = Direction.UPLEFT;
            else if (up && !left && !down && right) this.direction = Direction.UPRIGHT;
            else if (up && !left && !down && !right) this.direction = Direction.UP;
            else if (!up && left && down && !right) this.direction = Direction.DOWNLEFT;
            else if (!up && !left && down && right) this.direction = Direction.DOWNRIGHT;
            else if (!up && !left && down && !right) this.direction = Direction.DOWN;
            else if (!up && left && !down && !right) this.direction = Direction.LEFT;
            else if (!up && !left && !down && right) this.direction = Direction.RIGHT;
        }

    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP: up = false; break;
            case KeyEvent.VK_DOWN: down = false; break;
            case KeyEvent.VK_LEFT: left = false; break;
            case KeyEvent.VK_RIGHT: right = false; break;
        }
        this.determineDirection();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
