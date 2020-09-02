package com.feng.tankwar;

import java.awt.*;

class Wall {

    private int x;
    private int y;

    private boolean horizontal;

    private int bricks;
    private final Image brickImage;

    Wall(int x, int y, boolean horizontal, int bricks) {
        this.brickImage = utils.getImage("brick.png");
        this.x = x;
        this.y = y;
        this.horizontal = horizontal;
        this.bricks = bricks;
    }

    Rectangle getRectangle() {
        return horizontal?
                new Rectangle(x, y, bricks * brickImage.getWidth(null), brickImage.getHeight(null)):
                new Rectangle(x, y, brickImage.getWidth(null), bricks*brickImage.getHeight(null));
    }


    void draw(Graphics g) {
        if(horizontal){
            for (int i = 0; i < bricks; i++){
                g.drawImage(brickImage, x+i*brickImage.getWidth(null), y, null);
            }
        }else{
            for (int i = 0; i < bricks; i++){
                g.drawImage(brickImage, x, y+i*brickImage.getHeight(null), null);
            }
        }
    }
}
