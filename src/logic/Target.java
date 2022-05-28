package logic;

public class Target implements Entity {
    private volatile int x;
    private volatile int y;

    public Target(int robotX, int robotY) {
        this.x = robotX;
        this.y = robotY;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
}
