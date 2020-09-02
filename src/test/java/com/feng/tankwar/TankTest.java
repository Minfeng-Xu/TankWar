package com.feng.tankwar;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TankTest {

    @Test
    void getImage() {
        for(Direction direction : Direction.values()) {
            Tank tank = new Tank(0, 0, false, direction);
            assertNotNull(tank.getImage());

            Tank enemytank = new Tank(0,0,true, direction);
            assertNotNull(enemytank.getImage());
        }
    }
}