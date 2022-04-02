package gui;

import logic.Robot;

import java.io.Serializable;

public class RobotConfig implements Serializable, RobotObserver {
    private static double robotX;
    private static double robotY;
    private static double directionR;
    public final Robot robot;

    public RobotConfig(Robot robot) {
        this.robot = robot;
        robot.subscribe(this);
    }

    @Override
    public void update(double x, double y, double direction) {
        robotX = x;
        robotY = y;
        directionR = direction;
    }

    public double getX() {
        return robotX;
    }

    public double getY() {
        return robotY;
    }

    public double getDirectionR() {
        return directionR;
    }

    @Override
    public String toString()
    {
        return "X:" + robotX +"\s" + "Y:"+ robotY + "\s" + "Direction:" + directionR;
    }
}
