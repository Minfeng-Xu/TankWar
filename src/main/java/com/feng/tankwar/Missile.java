package com.feng.tankwar;

import java.awt.*;

@SuppressWarnings("all")
class Missile {

    public static final int SPEED = 10;
    private int x;

    private int y;

    private final  Direction direction;

    private final boolean enemy;

    private boolean live = true;

    public boolean isLive() {
        return live;
    }

    private void setLive(boolean live) {
        this.live = live;
    }

    Missile(int x, int y, Direction direction, boolean enemy) {
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
        if(x < 0 || x > GameClient.WIDTH || y < 0 || y > GameClient.HEIGHT) {
            this.setLive(false);
            return;
        }
        Rectangle rectangle = this.getRectangle();
        for(Wall wall : GameClient.getInstance().getWalls()) {
            if(rectangle.intersects(wall.getRectangle())){
                this.setLive(false);
                return;
            }
        }

        if (enemy) {
            Tank playerTank = GameClient.getInstance().getPlayerTank();
            if(rectangle.intersects(playerTank.getRectangeleForHitDetection())) {
                addExplosion();
                playerTank.setHp(playerTank.getHp() - 20);
                if(playerTank.getHp() <= 0) {
                    playerTank.setLive(false);
                }
                this.setLive(false);
            }
        }else {
            for(Tank tank: GameClient.getInstance().getEnemyTanks()) {
                if(rectangle.intersects(tank.getRectangeleForHitDetection())) {
                    addExplosion();
                    tank.setLive(false);
                    this.setLive(false);
                    break;
                }
            }
        }


        g.drawImage(getImage(), x, y, null);
    }

    private void addExplosion() {
        GameClient.getInstance().addExplosion(new Explosion(x, y));
        utils.playAudioFile("explode.wav");
    }


    private Rectangle getRectangle() {
        return new Rectangle(x, y, getImage().getWidth(null), getImage().getHeight(null));
    }
}
