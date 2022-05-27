package logic;

public class Target {
    private volatile int x;
    private volatile int y;

    public Target(int robotX, int robotY) {
        this.x = robotX;
        this.y = robotY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
}
