package logic;

import java.awt.geom.Point2D;

import static logic.MathOperations.*;
import static logic.MathOperations.angleTo;
import static logic.Const.*;

public class MoveOperations {
    public static void onModelUpdateEvent() {
        double distance = distance(Const.m_targetPositionX, Const.m_targetPositionY,
                Const.m_robotPositionX, Const.m_robotPositionY);
        if (distance < 0.5) {
            return;
        }
        double angleToTarget = angleTo(Const.m_robotPositionX, Const.m_robotPositionY,
                Const.m_targetPositionX, Const.m_targetPositionY);
        double angularVelocity = 0;
        if (angleToTarget > Const.m_robotDirection) {
            angularVelocity = Const.maxAngularVelocity;
        }
        if (angleToTarget < Const.m_robotDirection) {
            angularVelocity = -Const.maxAngularVelocity;
        }
        moveRobot(Const.maxVelocity, angularVelocity, 10);
    }

    private static void moveRobot(double velocity, double angularVelocity, double duration) {
        velocity = applyLimits(velocity, 0, Const.maxVelocity);
        angularVelocity = applyLimits(angularVelocity,
                -Const.maxAngularVelocity, Const.maxAngularVelocity);

        Point2D.Double newPosition = getNewRobotPosition(velocity, angularVelocity, duration);
        double newDirection = asNormalizedRadians(m_robotDirection + angularVelocity * duration);
        Const.m_robotDirection = newDirection;
        Const.m_robotPositionX = newPosition.x;
        Const.m_robotPositionY = newPosition.y;
    }

    private static Point2D.Double getNewRobotPosition(double velocity,
                                                      double angularVelocity,
                                                      double duration) {
        double x = Const.m_robotPositionX + velocity / angularVelocity *
                (Math.sin(Const.m_robotDirection + angularVelocity * duration) -
                        Math.sin(Const.m_robotDirection));
        if (!Double.isFinite(x)) {
            x = Const.m_robotPositionX + velocity * duration * Math.cos(Const.m_robotDirection);
        }
        double y = Const.m_robotPositionY - velocity / angularVelocity *
                (Math.cos(Const.m_robotDirection + angularVelocity * duration) -
                        Math.cos(Const.m_robotDirection));
        if (!Double.isFinite(y)) {
            y = Const.m_robotPositionY + velocity * duration * Math.sin(Const.m_robotDirection);
        }
        return new Point2D.Double(x, y);
    }
}
