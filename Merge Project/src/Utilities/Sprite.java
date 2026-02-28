package Utilities;

import java.awt.image.BufferedImage;

import Utilities.Collision.Collision;

public class Sprite {
    private BufferedImage image[];
    final Collision collision;
    public Point origin;
    public double imageTime = 1;

    public Sprite(BufferedImage[] image, Collision collision, Point origin) {
        this.image = new BufferedImage[image.length];
        for (int i = 0; i < image.length; i ++) {this.image[i] = image[i];}
        this.collision = collision;
        this.origin = origin;
    }

    public Sprite(BufferedImage[] image, Collision collision, Point origin, double span) {
        this.image = new BufferedImage[image.length];
        for (int i = 0; i < image.length; i ++) {this.image[i] = image[i];}
        this.collision = collision;
        this.origin = origin;
        this.imageTime = span;
    }

    public BufferedImage getImage(int n) {
        if (n >= image.length) return null;
        return image[n];
    }

    public int size() {
        return image.length;
    }

    public Collision getMask() {
        return this.collision.copy();
    }

}