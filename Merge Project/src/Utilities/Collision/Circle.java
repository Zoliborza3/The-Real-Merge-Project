package Utilities.Collision;

import Utilities.Point;

public class Circle extends Collision {
    private double radius;

    public Circle(double radius) {
        this.radius = radius;       
    }

    public String toString() {
        return "radius: "+radius;
    }

    public Collision resize(double x, double y) {
        if (x == 0 || y == 0) return null;

        return new Circle(radius*(x > y ? x : y));
    }

    public boolean collidesAt(Point thisOrigin, Collision other, Point otherOrigin) {
        if (other instanceof Circle && thisOrigin.distanceTo(otherOrigin) < this.radius+((Circle)(other)).radius) {return true;}
        else if (other instanceof Polygon) {
            // Check if circle center is inside polygon
            Polygon polygonMask = (Polygon)other;
            Point points[] = polygonMask.getPoints();
            int[] xPoints = new int[points.length];
            int[] yPoints = new int[points.length];
            for (int i = 0; i < points.length; i++) {
                xPoints[i] = (int) Math.round(points[i].x + otherOrigin.x);
                yPoints[i] = (int) Math.round(points[i].y + otherOrigin.y);
            }
            java.awt.Polygon polygon = new java.awt.Polygon(xPoints, yPoints, points.length);
            if (polygon.contains(thisOrigin.x, thisOrigin.y)) return true;

            // Check if any polygon edge is close enough to the circle
            double r = this.getLongestDiagonal();
            Point C = thisOrigin;
            int n = points.length;
            for (int i = 0; i < n; i++) {
                Point A = new Point(points[i].x + otherOrigin.x, points[i].y + otherOrigin.y);
                Point B = new Point(points[(i + 1) % n].x + otherOrigin.x, points[(i + 1) % n].y + otherOrigin.y);

                // Project C onto AB, clamp to segment
                double dx = B.x - A.x;
                double dy = B.y - A.y;
                double length2 = dx * dx + dy * dy;
                double t = ((C.x - A.x) * dx + (C.y - A.y) * dy) / (length2 == 0 ? 1 : length2);
                t = Math.max(0, Math.min(1, t));
                double closestX = A.x + t * dx;
                double closestY = A.y + t * dy;
                double dist = Math.hypot(C.x - closestX, C.y - closestY);
                if (dist <= r) return true;
            }
            
            return false;
        }
        return false;
    }

}
