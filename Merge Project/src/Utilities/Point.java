package Utilities;

public class Point {
    public double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Casts and returns this <code>Point</code> object's abcissa casted to an <code>int</code>
     * @return  <code>int</code>    the abcissa of the point casted to an integer
     */
    public int getX() {return (int)x;}

    /**
     * Casts and returns this <code>Point</code> object's ordinate casted to an <code>int</code>
     * @return  <code>int</code>    the ordinate of the point casted to an integer
     */
    public int getY() {return (int)y;}

    /**
     * Calculates the distance beetwen this <code>Point</code> and the given
     * @param point     the point to find the distance to
     * @return  <code>double</code>      the distance of the two points
     */
    public double distanceTo(Point point) {
        return Math.sqrt(Math.pow(this.x-point.x, 2)+Math.pow(this.y-point.y, 2));
    }

    public String toString() {
        return "["+x+", "+y+"]";
    }
}