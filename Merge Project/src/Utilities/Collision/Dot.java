package Utilities.Collision;

import Utilities.Point;

public class Dot extends Collision {

    private double longestDiagonal;

    public Dot() {
        longestDiagonal = 0;
    }
    
    public boolean collidesAt(Point thisOrigin, Collision other, Point otherOrigin) {
        if (other instanceof Dot) {
            return thisOrigin.equals(otherOrigin);
        }
        else {
            return other.collidesAt(otherOrigin, this, thisOrigin);
        }
    }

    public boolean containsPoint(Point thisOrigin, Point otherOrigin) {
        return (thisOrigin.equals(otherOrigin));
    }

    public String toString() {
        return null;
    }

    public Collision resize(double x, double y) {
        return this;
    }

    public Collision copy() {
        return this;
    }

    public double getLongestDiagonal() {
        return longestDiagonal;
    }

}
