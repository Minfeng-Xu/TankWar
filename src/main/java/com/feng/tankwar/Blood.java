package com.feng.tankwar;

import java.awt.*;

public class Blood {

    private int x, y;

    private final Image image ;

    private boolean live = true;

    void draw(Graphics g) {
        g.drawImage(image, x, y, null);
    }

    public Blood(int x, int y) {
        this.x = x;
        this.y = y;
        this.image = utils.getImage("blood.png");
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    Rectangle getRectangle() {
        return new Rectangle(x, y, image.getWidth(null), image.getHeight(null));
    }

}
