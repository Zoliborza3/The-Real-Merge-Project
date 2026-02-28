package Utilities;

import java.util.LinkedList;
import java.util.Map;

import Utilities.Collision.Collision;

public class Object {
    public Point position;
    public boolean visible = true;
    //Solid objects cannot be passed into and also have their own detection filter
    public boolean solid = false;
    //The key of this object sprite (not a private datatag, but setting it to an incorrect or not existant spriteKey will crash the game)
    private String spriteIndex;
    //Note: If you resize an object that has a circle collision mask it will not distort, instead it will scale to the bigger image scale
    public double imageXScale = 1;
    public double imageYScale = 1;
    //How fast should the cycler cycle through the sprite compared to it's original speed
    public double imageSpeed = 1;
    //Which ever frame of the sprite the object is on (it isn't a private datatag, but setting it manually to an incorrect value will crash the game)
    int imageIndex = 0;
    //depth determines what objects are rendered in front of what object, only supports values beetwen -99 and 99 at it WILL crash the game if you set one outside of this range
    private int depth = 0;
    //remembers which frame the cycler cycled this object
    public long previousUpdate;
    //The unique id of the instance of this object, make sure to generate one that hasn't been expanded yet
    public final String KEY;
    //copy of the sprite's mask, has to be updated everytime the spriteKey is; you can set it to something else then the sprite's mask, no point in it tho
    private Collision mask;
    public Entity entity;

    public Object(Point position, String spriteIndex, Collision mask, String Key, long currentFrame) {
        this.position = position;
        this.spriteIndex = spriteIndex;
        this.mask = mask;
        this.KEY = Key;
        this.previousUpdate = currentFrame;
    }

    public String getSpriteIndex() {
        return spriteIndex;
    }

    public void setSpriteIndex(String spriteIndex, Collision mask) {
        this.spriteIndex = spriteIndex;
        this.mask = mask;
    }

    public int getDepth() {return this.depth;}

    public void setDepth(int depth) {
        if (depth < -99) {this.depth = -99;}
        else if (depth > 99) {this.depth = 99;}
        else {this.depth = depth;}
    }

    public Collision getMask() {
        return mask.resize(imageXScale, imageYScale);
    }

    public boolean collidesWithAt(Point thisPosition, Object other, Point otherPosition) {
        Collision otherMask = other.getMask();
        
        if (this.mask == null || otherMask == null) return false;
        return getMask().collidesAt(thisPosition, otherMask, otherPosition);
    }

    public boolean collidesAt(Point thisPosition, Map<String, Object> others) {
        if (this.mask == null) return false;
        Collision trueMask = getMask();
        for (Object other : others.values()) {
            if (other != this) {
                Collision otherMask = other.getMask();
            
                if (otherMask != null && trueMask.collidesAt(thisPosition, otherMask, other.position)) return true;
            }
        }
        return false;
    }

    public boolean collides(Map<String, Object> others) {
        return collidesAt(this.position, others);
    }

    public LinkedList<Object> collidesAtList(Point thisPosition, Map<String, Object> others) {
        if (this.mask == null) return new LinkedList<>();
        Collision trueMask = getMask();
        LinkedList<Object> objects = new LinkedList<>();
        for (Object other : others.values()) {
            if (other != this) {
                Collision otherMask = other.getMask();

                if (otherMask != null && trueMask.collidesAt(thisPosition, otherMask, other.position)) objects.add(other);
            }
        }
        return objects;
    }

    public LinkedList<Object> collidesList(Map<String, Object> others) {
        return collidesAtList(this.position, others);
    }

    public boolean solidCollidesWithAt(Point thisPosition, Object other, Point otherPosition) {
        if (!this.solid || !other.solid) return false;
        return collidesWithAt(thisPosition, other, otherPosition);
    }

    public boolean solidCollidesAt(Point thisPosition, Map<String, Object> others) {
        if (this.mask == null || !this.solid) return false;
        Collision trueMask = getMask();
        for (Object other : others.values()) {
            if (other != this) {
                Collision otherMask = other.getMask();

                if (otherMask != null && other.solid && trueMask.collidesAt(thisPosition, otherMask, other.position)) return true;
            }
        }
        return false;
    }

    public boolean solidCollides(Map<String, Object> others) {
        return solidCollidesAt(this.position, others);
    }

    public LinkedList<Object> solidCollidesAtList(Point thisPosition, Map<String, Object> others) {
        if (this.mask == null || !this.solid) return new LinkedList<>();
        Collision trueMask = getMask();
        LinkedList<Object> objects = new LinkedList<>();
        for (Object other : others.values()) {
            if (other != this) {
                Collision otherMask = other.getMask();

                if (otherMask != null && other.solid && trueMask.collidesAt(thisPosition, otherMask, other.position)) objects.add(other);
            }
        }
        return objects;
    }

    public LinkedList<Object> solidCollidesList(Map<String, Object> others) {
        return solidCollidesAtList(this.position, others);
    }

}
