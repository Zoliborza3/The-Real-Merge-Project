package Utilities;

import java.awt.Point;

public class Object {
    public Point position;
    public boolean visible = true;
    public boolean solid = false;
    public String spriteIndex;
    public double imageXScale = 1;
    public double imageYScale = 1;
    public double imageSpeed = 1;
    public int imageIndex = 0;
    public int depth = 0;
    public long previousUpdate;
    public final String KEY;

    public Object(Point position, String spriteIndex, String Key, long currentFrame) {
        this.position = position;
        this.spriteIndex = spriteIndex;
        this.KEY = Key;
        this.previousUpdate = currentFrame;
    }

}
