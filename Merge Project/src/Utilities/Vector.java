package Utilities;
public class Vector {
    public double x, y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double length() {
        return Math.sqrt(x*x+y*y);
    }

    public Vector thisVectorWithLength(double targetLength) {
        double currentLength = this.length();
        double ratio = targetLength/currentLength;
        return new Vector(x*ratio, y*ratio);
    }

    public Vector multiplyBySKalar(double skalar) {
        return new Vector(this.x * skalar, this.y * skalar);
    }

    public Vector addVector(Vector vector) {
        return new Vector(this.x + vector.x, this.y + vector.y);
    }

    public Vector opposite() {
        return new Vector(-this.x, -this.y);
    }

    public String toString() {
        return "["+x+", "+y+"]";
    }

}
