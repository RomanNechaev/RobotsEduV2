package logic;

import gui.state.RobotObserver;

import java.io.Serializable;
import java.util.*;

import static logic.MathOperations.*;
import static logic.RobotConstants.*;

public class Robot implements Serializable {
    private volatile double x = startX;
    private volatile double y = startY;
    private volatile double direction = 0;
    private double speed;
    private volatile int health = 20;
    private volatile int targetPoints = 0;

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

    public void setTargetPoints(int targetPoints) {
        this.targetPoints = targetPoints;
    }

    public int getTargetPoints() {
        return targetPoints;
    }


    @Override
    public String toString() {
        return "Robot{" + "x:" + x
                + "y:" + y
                + "direction:" + direction + "}";
    }



    public double getDistanceTo(Enemy en) {
        return Math.sqrt(this.x * en.getX() + this.y * en.getY());
    }
}
