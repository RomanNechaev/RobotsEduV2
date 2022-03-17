package logic;
import gui.RobotObserver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static logic.MathOperations.*;
import static logic.Const.*;

public class Robot implements Serializable {
    //десериализация
    //начальные значения
    private double x = 100;
    private double y = 100;
    private transient double direction = 0;

    private List<RobotObserver> observers = new ArrayList<>();

    public void move(int targetX, int targetY) {
        double distance = distance(targetX, targetY, x, y);
        if (distance < 0.5) {
            return;
        }
        double angleToTarget = angleTo(x, y, targetX, targetY, direction);
        double angularVelocity = calculateAngularVelocity(angleToTarget, direction, maxAngularVelocity);
        double velocity = 0;

        if(angularVelocity == 0)
            velocity = maxVelocity;

        double duration = 10;
        x = calculateNewX(x, velocity, angularVelocity, duration, direction);
        y = calculateNewY(y, velocity, angularVelocity, duration, direction);
        direction = asNormalizedRadians(direction + angularVelocity * duration);

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

    public double getDirection() {
        return direction;
    }
}
