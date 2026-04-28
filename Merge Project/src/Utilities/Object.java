package Utilities;


import java.util.LinkedList;
import java.util.Map;

import Utilities.Collision.Collision;
import Utilities.Entities.Entity;

public class Object {

    //!!IMPORTANT!! sets what layer it is rendered to; Objects can only collide normally with objects on the same layer;
    public int layer;
    //some pre-defined layer ids
    public static final int BASE = 0, HUD = -1;

    //Ingame position of the Object; relative value
    private Point position;
    //whether or not the Object is attempted to be rendered; negative-relative value
    private boolean visible = true;
    //Solid objects cannot be passed into and also have their own detection filter; positive-relative value
    private boolean solid = false;
    //The key of this object sprite (not a private datatag, but setting it to an incorrect or not existant spriteKey will crash the game)
    private String spriteIndex;
    //Note: If you resize an object that has a circle collision mask it will not distort, instead it will scale to the bigger image scale
    public double imageXScale = 1;
    public double imageYScale = 1;
    //How fast should the cycler cycle through the sprite compared to it's original speed
    public double imageSpeed = 1;
    //Which ever frame of the sprite the object is on (it isn't a private datatag, but setting it manually to an incorrect value will crash the game)
    int imageIndex = 0;
    //depth determines what objects are rendered in front of what object inside a layer; relative value
    private int depth = 0;
    //remembers which frame the cycler cycled this object
    public long previousUpdate;
    //The unique id of the instance of this object, make sure to generate one that hasn't been expanded yet
    public final String KEY;
    //copy of the sprite's mask, has to be updated everytime the spriteKey is; you can set it to something else then the sprite's mask, no point in it tho
    private Collision mask;
    public Entity entity;
    //Being a collection is like being the origin point of your own layer; Every Object that is part of this Object's collection has relative attributes to it; Being of the same collection also normally exclude things from collision detection of each other;
    private LinkedList<String> collection = new LinkedList<>();
    //The key of the Object that this Object is the part of as a collection; accepts null
    private String sourceKey = null;

    public Object(int layer, Point relativePosition, String spriteIndex, Collision mask, String Key, long currentFrame) {
        this.layer = layer;
        this.position = relativePosition;
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

    public int getRelativeDepth() {return this.depth;}

    public int getAbsoluteDepth() {
        if (sourceKey == null) return this.depth;
        return Game.instance.get(sourceKey).getAbsoluteDepth()+this.depth;
    }

    public void setRelativeDepth(int depth) {
        this.depth = depth;
    }

    public void setAbsoluteDepth(int depth) {
        if (sourceKey == null) {
            this.depth = depth;
            return;
        }
        int currentDepth = Game.instance.get(sourceKey).getAbsoluteDepth();
        this.depth = this.depth-currentDepth;
    }

    public Collision getMask() {
        return mask.resize(imageXScale, imageYScale);
    }

    public boolean collidesWithAt(Point thisPosition, Object other, Point otherPosition) {
        Collision otherMask = other.getMask();
        
        if (this.mask == null || otherMask == null || this.layer != other.layer) return false;
        return getMask().collidesAt(thisPosition, otherMask, otherPosition);
    }

    public boolean collidesAt(Point thisPosition, Map<String, Object> others) {
        if (this.mask == null) return false;
        Collision trueMask = getMask();
        for (Object other : others.values()) {
            if (other != this && other.layer == this.layer) {
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
            if (other != this && other.layer == this.layer) {
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
        if (!other.solid) return false;
        return collidesWithAt(thisPosition, other, otherPosition);
    }

    public boolean solidCollidesAt(Point thisPosition, Map<String, Object> others) {
        if (this.mask == null) return false;
        Collision trueMask = getMask();
        for (Object other : others.values()) {
            if (other != this && other.layer == this.layer) {
                Collision otherMask = other.getMask();

                if (otherMask != null && other.getAbsoluteSolidity() && trueMask.collidesAt(thisPosition, otherMask, other.position)) return true;
            }
        }
        return false;
    }

    public boolean solidCollides(Map<String, Object> others) {
        return solidCollidesAt(this.position, others);
    }

    public LinkedList<Object> solidCollidesAtList(Point thisPosition, Map<String, Object> others) {
        if (this.getMask() == null) return new LinkedList<>();
        Collision trueMask = getMask();
        LinkedList<Object> objects = new LinkedList<>();
        for (Object other : others.values()) {
            if (other != this && other.layer == this.layer) {
                Collision otherMask = other.getMask();

                if (otherMask != null && other.getAbsoluteSolidity() && trueMask.collidesAt(thisPosition, otherMask, other.position)) objects.add(other);
            }
        }
        return objects;
    }

    public LinkedList<Object> solidCollidesList(Map<String, Object> others) {
        return solidCollidesAtList(this.position, others);
    }

    public void setRelativePosition(Point position) {
        this.position = position;
    }

    public void setAbsolutePosition(Point position) {
        if (sourceKey == null) {
            this.setRelativePosition(position);
            return;
        }
        Point currentPosition = Game.instance.get(sourceKey).getAbsolutePosition();
        this.setRelativePosition(new Point(position.x-currentPosition.x, position.y-currentPosition.y));
    }

    public Point getRelativePosition() {
        return position;
    }
    
    public Point getAbsolutePosition() {
        if (sourceKey == null) return position;
        return Game.instance.get(sourceKey).getAbsolutePosition().applyVector(new Vector(this.position.x, this.position.y));
    }

    public boolean getAbsoluteVisibility() {
        if (sourceKey == null) return visible;
        String key = sourceKey;
        while (key != null) {
            Object ancestor = Game.instance.get(key);
            if (!ancestor.visible) return false;
            key = ancestor.sourceKey;
        }
        return visible;
    }

    public boolean getRelativeVisibility() {
        return visible;
    }

    public boolean getAbsoluteSolidity() {
        if (sourceKey == null) return solid;
        String key = sourceKey;
        while (key != null) {
            Object ancestor = Game.instance.get(key);
            if (ancestor.solid) return true;
            key = ancestor.sourceKey;
        }
        return solid;
    }

    public boolean getRelativeSolidity() {
        return solid;
    }

    public void setRelativeVisibility(boolean visible) {
        this.visible = visible;
    }

    public void setRelativeSolidity(boolean solid) {
        this.solid = solid;
    }

    public void setAbsoluteVisibility(boolean visible) {
        this.visible = visible;
        String key = sourceKey;
        while (key != null) {
            Object ancestor = Game.instance.get(key);
            ancestor.setRelativeVisibility(visible);
            key = ancestor.sourceKey;
        }
    }

    public void setAbsoluteSolidity(boolean solid) {
        this.solid = solid;
        String key = sourceKey;
        while (key != null) {
            Object ancestor = Game.instance.get(key);
            ancestor.solid = solid;
            key = ancestor.sourceKey;
        }
    }

    public boolean containsKey(String searchKey) {
        if (collection.contains(searchKey)) return true;
        String key = Game.instance.get(searchKey).sourceKey;
        while (key != null) {
            Object ancestor = Game.instance.get(key);
            if (ancestor.KEY.equals(KEY)) return true;
            if (ancestor.sourceKey == null) return false;
            key = ancestor.sourceKey;
        }
        return false;
    }

    public void setSource(String sourceKey) {
        if (this.KEY.equals(sourceKey) || this.containsKey(sourceKey) || Game.instance.get(sourceKey).containsKey(KEY)) throw new IllegalArgumentException("An Object's ancestor cannot be itself or one of it's collectives nor can it be it's current ancestor's ancestor and so on");
        this.sourceKey = sourceKey;
        Game.instance.get(sourceKey).collection.add(KEY);
    }

    public void addToCollection(String Key) {
        if (collection.contains(Key) || this.KEY.equals(sourceKey) || this.containsKey(Key) || Game.instance.get(Key).containsKey(KEY)) throw new IllegalArgumentException("An Object cannot be it's own collective, nor can it's ancestor be it's collective, nor can one of it's already existing collective");
        this.collection.add(Key);
        Game.instance.get(Key).sourceKey = KEY;
    }

    public void removeFromCollection(String Key) {
        if (!collection.contains(Key)) throw new IllegalArgumentException("The provided Key does not belong to an Object that is part of this objects immideate collection");
        collection.remove(Key);
        Game.instance.get(Key).sourceKey = null;
    }
}
