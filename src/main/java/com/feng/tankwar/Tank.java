package com.feng.tankwar;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;

@SuppressWarnings("all")
class Tank {

    private int x;
    private int y;

    private boolean enemy;

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

    private void move() {
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
        int oldX = x;
        int oldY = y;
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
        }

    }

    private void fire() {
        Missile missile = new Missile(x + getImage().getWidth(null)/2 -6,
                        y + getImage().getHeight(null)/2 -6, direction, enemy);
        GameClient.getInstance().getMissiles().add(missile);

        Media sound = new Media(new File("assets/audios/shoot.wav").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    private boolean stopped;

    void determineDirection() {
        if(!up && !left && !down && !right) {
            this.stopped = true;
        } else {
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


}
