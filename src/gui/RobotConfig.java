package gui;

import logic.Robot;

import java.io.Serializable;

public class RobotConfig implements Serializable, RobotObserver {
    private static double robotX;
    private static double robotY;

    public RobotConfig(Robot robot){
        robot.subscribe(this);
    }
    @Override
    public void update(double x, double y, double direction) {
        robotX = x;
        robotY = y;
    }

    public double getRobotX() {
        return robotX;
    }
    public static double getRobotY() {
        return robotY;
    }
}
