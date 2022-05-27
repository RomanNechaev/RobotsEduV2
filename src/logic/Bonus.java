package logic;

public class Bonus {
    private volatile double x;
    private volatile double y;

    public Bonus(double robotX, double robotY) {
        this.x = robotX;
        this.y = robotY;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
}
