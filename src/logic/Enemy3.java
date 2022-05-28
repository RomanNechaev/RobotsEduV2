package logic;

import gui.state.RobotObserver;

import java.util.ArrayList;
import java.util.List;

import static logic.MathOperations.*;
import static logic.MathOperations.asNormalizedRadians;
import static logic.RobotConstants.maxAngularVelocity;
import static logic.RobotConstants.maxVelocity;

public class Enemy3 extends Enemy implements Entity{
    private volatile double x = 234;
    private volatile double y = 234;
    private volatile double direction = 0;
    private volatile int health = 5;
    private volatile double velocity = 0.043;
    private List<RobotObserver> observers = new ArrayList<>();

    public void move(int targetX, int targetY) {

        double distance = distance(targetX, targetY, x, y);

        if (distance < 0.5) {
            return;
        }
        double angleToTarget = angleTo(x, y, targetX, targetY, direction);
        double angularVelocity = calculateAngularVelocity(angleToTarget, direction, maxAngularVelocity);
        //double velocity = 0.1;

        if (angularVelocity == 0)
            velocity = maxVelocity;

        double duration = 10;
        synchronized (this) {
            x = recalculateX(x, velocity, angularVelocity, duration, direction);
            y = recalculateY(y, velocity, angularVelocity, duration, direction);
            direction = asNormalizedRadians(direction + angularVelocity * duration);
        }

        for (RobotObserver observer : observers) {
            observer.update(x, y, direction);
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

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public double getDirection() {
        return direction;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

}
