package gui.state;

import gui.state.RobotObserver;
import logic.Robot;

import java.io.Serializable;

public class RobotConfig implements Serializable, RobotObserver {
    private static double X;
    private static double Y;
    private static double direction;
    public final Robot robot;

    public RobotConfig(Robot robot) {
        this.robot = robot;
        robot.subscribe(this);
    }

    @Override
    public void update(double x, double y, double angle) {
        X = x;
        Y = y;
        direction = angle;
    }

    @Override
    public String toString()
    {
        return "X:" + X +"\s" + "Y:"+ Y + "\s" + "Direction:" + direction;
    }
}
