package com.feng.tankwar;

import java.awt.*;
@SuppressWarnings("all")
public enum  Direction {
    UP("U"),
    DOWN("D"),
    LEFT("L"),
    RIGHT("R"),
    LEFT_UP("LU"),
    RIGHT_UP("RU"),
    LEFT_DOWN("LD"),
    RIGHT_DOWN("RD");

    private final String abbrev;

    Direction(String abbrev) {
        this.abbrev = abbrev;
    }

    Image getImage(String prefix) {
        return utils.getImage(prefix + abbrev + ".gif");
    }
}
