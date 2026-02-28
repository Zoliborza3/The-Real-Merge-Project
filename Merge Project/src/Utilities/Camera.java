package Utilities;

public class Camera {
    public Point position;
    public double width;
    public double height;
    public String anchorKey = null;

    public Camera(Point position, double width, double height, String anchorKey) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.anchorKey = anchorKey;
    }

    public double rightBound() {
        return position.x + width;
    }

    public double leftBound() {
        return position.x;
    }

    public double bottomBound() {
        return position.y + height;
    }

    public double topBound() {
        return position.y;
    }

}
