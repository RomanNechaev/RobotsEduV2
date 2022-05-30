package logic;

import gui.state.RobotObserver;

import java.util.ArrayList;
import java.util.List;

import static logic.MathOperations.*;
import static logic.MathOperations.asNormalizedRadians;
import static logic.RobotConstants.*;
import static logic.RobotConstants.maxVelocity;

public class Bullet implements Entity {
    private volatile double x;
    private volatile double y;
    private volatile double direction = 0;

    public Bullet(double robotX, double robotY) {
        this.x = robotX;
        this.y = robotY;
    }

    private List<RobotObserver> observers = new ArrayList<>();

    public void move(int targetX, int targetY) {
        double distance = distance(targetX, targetY, x, y);
        if (distance < 0.5) {
            return;
        }
        double angleToTarget = angleTo(x, y, targetX, targetY, direction);
        double angularVelocity = calculateAngularVelocity(angleToTarget, direction, maxAngularVelocity);
        double velocity = 0;

        if (angularVelocity == 0)
            velocity = maxVelocity;

        double duration = 10;
        synchronized (this) {
            x = recalculateX(x, velocity, angularVelocity, duration, direction);
            y = recalculateY(y, velocity, angularVelocity, duration, direction);
            direction = asNormalizedRadians(direction + angularVelocity * duration);
        }

        for (RobotObserver observer : observers) {
            observer.update(x, y, direction, 0, 0);
        }
    }

    public void subscribe(RobotObserver observer) {
        observers.add(observer);
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
