package com.feng.tankwar;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Random;

@SuppressWarnings("all")
class Tank {

    public static final int MOVE_SPEED = 5;
    private int x;
    private int y;

    private boolean live = true;

    boolean isLive() {
        return live;
    }

    void setLive(boolean live) {
        this.live = live;
    }

    private boolean enemy;

    boolean isEnemy() {
        return enemy;
    }

    private int hp = 100;

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }


    private Direction direction;

    Tank(int x, int y, Direction direction) {
        this(x, y, false, direction);
    }

    Tank(int x, int y, boolean enemy, Direction direction) {
        this.x = x;
        this.y = y;
        this.enemy = enemy;
        this.direction = direction;
    }

    Image getImage() {
        String prefix = enemy? "e":"";
        return direction.getImage(prefix + "tank");
    }

    private void move() {
        if(this.stopped) return;
        x += direction.xFactor * MOVE_SPEED;
        y += direction.yFactor * MOVE_SPEED;
    }

    void draw(Graphics g) {

        int oldX = x; int oldY = y;
        this.determineDirection();
        this.move();
        if(x < 0) x = 0;
        else if (x>800 - getImage().getWidth(null)) x=800 - getImage().getWidth(null);
        if(y < 0) y = 0;
        else if (y> 600 - getImage().getHeight(null)) y = 600 - getImage().getHeight(null);
        Rectangle rec = this.getRectangle();
        for(Wall wall : GameClient.getInstance().getWalls()) {
            if(rec.intersects(wall.getRectangle())){
                x = oldX;
                y = oldY;
                break;
            }
        }
        for (Tank enemy: GameClient.getInstance().getEnemyTanks()){
            if(rec.intersects(enemy.getRectangle())){
                x = oldX;
                y = oldY;
                break;
            }
        }

        g.drawImage(this.getImage(), this.x, this.y, null );
    }

    Rectangle getRectangle() {
        return new Rectangle(x,y, getImage().getWidth(null), getImage().getHeight(null));
    }


    private boolean up, down, left, right;

    void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP: up = true; break;
            case KeyEvent.VK_DOWN: down = true; break;
            case KeyEvent.VK_LEFT: left = true; break;
            case KeyEvent.VK_RIGHT: right = true; break;
            case KeyEvent.VK_F: fire(); break;
            case KeyEvent.VK_A: superFire(); break;
        }

    }

    private void fire() {
        Missile missile = new Missile(x + getImage().getWidth(null)/2 -6,
                        y + getImage().getHeight(null)/2 -6, direction, enemy);
        GameClient.getInstance().getMissiles().add(missile);

        utils.playAudioFile("shoot.wav");
    }

    private void superFire() {
        for (Direction direction : Direction.values()) {
            Missile missile = new Missile(x + getImage().getWidth(null)/2 -6,
                    y + getImage().getHeight(null)/2 -6, direction, enemy);
            GameClient.getInstance().getMissiles().add(missile);
        }

        String audioFile = new Random().nextBoolean()? "supershoot.aiff" : "supershoot.wav";
        utils.playAudioFile(audioFile);
    }



    private boolean stopped;

    void determineDirection() {
        if(!up && !left && !down && !right) {
            this.stopped = true;
        } else {
            this.stopped = false;
            if (up && left && !down && !right) this.direction = Direction.LEFT_UP;
            else if (up && !left && !down && right) this.direction = Direction.RIGHT_UP;
            else if (up && !left && !down && !right) this.direction = Direction.UP;
            else if (!up && left && down && !right) this.direction = Direction.LEFT_DOWN;
            else if (!up && !left && down && right) this.direction = Direction.RIGHT_DOWN;
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


}
