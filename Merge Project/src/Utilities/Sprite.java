package Utilities;

import java.awt.Point;
import java.awt.image.BufferedImage;

public class Sprite {
    BufferedImage image[];
    Collision collision;
    public Point origin;

    public Sprite(BufferedImage[] image, Collision collision, Point origin) {
        this.image = new BufferedImage[image.length];
        for (int i = 0; i < image.length; i ++) {this.image[i] = image[i];}
        this.collision = collision;
        this.origin = origin;
    }

    public BufferedImage getImage(int n) {
        if (n >= image.length) return null;
        return image[n];
    }
}