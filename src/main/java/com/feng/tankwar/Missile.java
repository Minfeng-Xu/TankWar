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

    public void setLive(boolean live) {
        this.live = live;
    }

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
            if(rectangle.intersects(playerTank.getRectangle())) {
                addExplosion();
                playerTank.setHp(playerTank.getHp() - 20);
                if(playerTank.getHp() <= 0) {
                    playerTank.setLive(false);
                }
                this.setLive(false);
            }
        }else {
            for(Tank tank: GameClient.getInstance().getEnemyTanks()) {
                if(rectangle.intersects(tank.getRectangle())) {
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


    Rectangle getRectangle() {
        return new Rectangle(x, y, getImage().getWidth(null), getImage().getHeight(null));
    }
}
