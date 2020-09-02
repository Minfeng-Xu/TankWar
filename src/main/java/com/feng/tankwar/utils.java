package com.feng.tankwar;

import javax.swing.*;
import java.awt.*;

public class utils {

    public static Image getImage(String imageName) {
        return new ImageIcon("assets/images/" + imageName).getImage();
    }
}
