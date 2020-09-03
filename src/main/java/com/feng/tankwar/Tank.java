package com.feng.tankwar;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Random;

@SuppressWarnings("all")
class Tank {

    Save.Position getPosition() {
        return new Save.Position(x, y, direction);
    }

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

    private static final int MAX_HP = 100;

    private int hp = MAX_HP;

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }


    private Direction direction;


    Tank(Save.Position position, boolean enemy) {
        this(position.getX(), position.getY(), enemy, position.getDirection());
    }

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
        if(!this.enemy) {
            this.determineDirection();
        }

        this.move();
        if(x < 0) {x = 0;}
        else if (x>GameClient.WIDTH - getImage().getWidth(null)) x=GameClient.WIDTH - getImage().getWidth(null);
        if(y < 0) {y = 0;}
        else if (y> GameClient.HEIGHT - getImage().getHeight(null)) y = GameClient.HEIGHT - getImage().getHeight(null);
        Rectangle rec = this.getRectangle();
        for(Wall wall : GameClient.getInstance().getWalls()) {
            if(rec.intersects(wall.getRectangle())){
                x = oldX;
                y = oldY;
                break;
            }
        }
        for (Tank enemy: GameClient.getInstance().getEnemyTanks()){
            if(enemy != this && rec.intersects(enemy.getRectangle())){
                x = oldX;
                y = oldY;
                break;
            }
        }

        if( this.enemy && rec.intersects(
                GameClient.getInstance().getPlayerTank().getRectangle())) {
            x = oldX;
            y = oldY;
        }

        if(!this.enemy) {
            Blood blood = GameClient.getInstance().getBlood();
            if(blood.isLive() && rec.intersects(blood.getRectangle())) {
                this.hp = MAX_HP;
                utils.playAudioFile("revive.wav");
                blood.setLive(false);
            }
            g.setColor(Color.WHITE);
            g.fillRect(x, y - 10, this.getImage().getWidth(null), 10);
            g.setColor(Color.RED);
            int width = hp * this.getImage().getWidth(null) / MAX_HP;
            g.fillRect(x, y -10, width, 10);

            Image petImage = utils.getImage("pet-camel.gif");
            g.drawImage(petImage, this.x - petImage.getWidth(null) -DISTANCE_TO_PET, this.y, null);
        }

        g.drawImage(this.getImage(), this.x, this.y, null );
    }

    private static final int DISTANCE_TO_PET = 4;

    Rectangle getRectangle() {
        if(enemy) {
            return new Rectangle(x,y, getImage().getWidth(null), getImage().getHeight(null));
        } else {
            Image petImage = utils.getImage("pet-camel.gif");
            int delta = petImage.getWidth(null) + DISTANCE_TO_PET;
            return new Rectangle(x - delta, y,
                    getImage().getWidth(null) + delta, getImage().getHeight(null));
        }

    }

    Rectangle getRectangeleForHitDetection() {
        return new Rectangle(x,y, getImage().getWidth(null), getImage().getHeight(null));
    }


    private boolean up, down, left, right;



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



    private int code;



    void determineDirection() {
        Direction newDirection = Direction.get(code);
        if(newDirection == null) {
            this.stopped = true;
        } else {
            this.direction = newDirection;
            this.stopped = false;
        }

    }

    void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP: code |= Direction.UP.code; break;
            case KeyEvent.VK_DOWN: code |= Direction.DOWN.code; break;
            case KeyEvent.VK_LEFT: code |= Direction.LEFT.code; break;
            case KeyEvent.VK_RIGHT: code |= Direction.RIGHT.code; break;
            case KeyEvent.VK_F: fire(); break;
            case KeyEvent.VK_A: superFire(); break;
            case KeyEvent.VK_R: GameClient.getInstance().restart(); break;
        }
        this.determineDirection();

    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP: code ^= Direction.UP.code; break;
            case KeyEvent.VK_DOWN: code ^= Direction.DOWN.code; break;
            case KeyEvent.VK_LEFT: code ^= Direction.LEFT.code; break;
            case KeyEvent.VK_RIGHT: code ^= Direction.RIGHT.code; break;
        }
        this.determineDirection();
    }

    private final Random random = new Random();

    private int step = random.nextInt(12) + 3;

    public void actRandomly() {
        Direction[] dirs = Direction.values();
        if (step == 0) {
            step = random.nextInt(12) + 3;
            this.direction = dirs[random.nextInt(dirs.length)];
            if(random.nextBoolean()) {
                this.fire();
            }
        }
        step--;
    }

    boolean isDying() {
        return this.hp <= MAX_HP *0.2;
    }
}
