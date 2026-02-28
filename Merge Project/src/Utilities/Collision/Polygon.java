package Utilities.Collision;

import java.awt.geom.Area;
import java.security.InvalidParameterException;
import java.util.Arrays;

import Utilities.Point;

public class Polygon extends Collision {

    private Point[] points;

    public Polygon(Point[] points) {
        
        this.points = points;

    }

    public Polygon(int[] xs, int[] ys) {

        if (xs.length != ys.length) {throw new InvalidParameterException("The provided lists do not have the same length");}
        this.points = new Point[xs.length];

        for (int i = 0; i < xs.length; i ++) {
            this.points[i] = new Point(xs[i], ys[i]);
        }

    }

    public Polygon resize(double x, double y) {
        if (x == 0 || y == 0) {return null;}

        Point points[] = new Point[this.points.length];

        for (int i = 0; i < points.length; i ++) {
            points[i] = new Point(this.points[i].x*x, this.points[i].y*y);
        }

        return new Polygon(points);
    }

    public boolean collidesAt(Point thisOrigin, Collision other, Point otherOrigin) {
        if (other instanceof Polygon) {

            // Check if circle center is inside polygon
            Polygon otherPolygonMask = (Polygon)other;
            Point otherPoints[] = otherPolygonMask.getPoints();
            int[] otherXPoints = new int[otherPoints.length];
            int[] otherYPoints = new int[otherPoints.length];
            for (int i = 0; i < points.length; i++) {
                otherXPoints[i] = (int) Math.round(otherPoints[i].x + otherOrigin.x);
                otherYPoints[i] = (int) Math.round(otherPoints[i].y + otherOrigin.y);
            }
            java.awt.Polygon otherPolygon = new java.awt.Polygon(otherXPoints, otherYPoints, otherPoints.length);
            if (otherPolygon.contains(thisOrigin.x, thisOrigin.y)) return true;

            Polygon thisPolygonMask = (Polygon)this;
            Point thisPoints[] = thisPolygonMask.getPoints();
            int[] thisXPoints = new int[thisPoints.length];
            int[] thisYPoints = new int[thisPoints.length];
            for (int i = 0; i < points.length; i++) {
                thisXPoints[i] = (int) Math.round(thisPoints[i].x + thisOrigin.x);
                thisYPoints[i] = (int) Math.round(thisPoints[i].y + thisOrigin.y);
            }
            java.awt.Polygon thisPolygon = new java.awt.Polygon(thisXPoints, thisYPoints, points.length);
            if (thisPolygon.contains(otherOrigin.x, otherOrigin.y)) return true;

            Area thisArea = new Area(thisPolygon);
            Area otherArea = new Area(otherPolygon);
            thisArea.intersect(otherArea);

            return !thisArea.isEmpty();

        }
        else if (other instanceof Circle) {other.collidesAt(otherOrigin, this, thisOrigin);}
        return false;
    }

    public Point[] getPoints() {
        return points;
    }

    public String toString() {
        return Arrays.toString(points);
    }

}
