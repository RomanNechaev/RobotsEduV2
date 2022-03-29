package logic;

import java.util.function.Function;

public class MathOperations {

    /**
     * normalizes, but in Polar coordinate system
     */
    public static double angleTo(double fromX, double fromY, double toX, double toY, double direction)
    {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        double angle = Math.atan2(diffY, diffX);

        if(direction > angle) {
            while (Math.abs(direction - angle) >= Math.PI) {
                angle += 2 * Math.PI;
            }
        } else if (direction < angle) {
            while (Math.abs(direction - angle) >= Math.PI) {
                angle -= 2 * Math.PI;
            }
        }
        return angle;
    }

    public static double asNormalizedRadians(double angle)
    {
        while (angle < 0)
        {
            angle += 2*Math.PI;
        }
        while (angle >= 2*Math.PI)
        {
            angle -= 2*Math.PI;
        }
        return angle;
    }

    public static double distance(double x1, double y1, double x2, double y2)
    {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    public static double calculateAngularVelocity(double angleToTarget, double direction, double maxAngularVelocity) {
        double velocity = 0;
        if (angleToTarget > direction) {
            velocity = Math.min((angleToTarget - direction) / 10, maxAngularVelocity);
        }
        if (angleToTarget < direction) {
            velocity = Math.max((angleToTarget - direction) / 10, -maxAngularVelocity);
        }
        return velocity;
    }

    public static double recalculateX(double x, double velocity, double angularVelocity, double duration, double direction) {
        double newX = x + velocity / angularVelocity *
                (Math.sin(direction + angularVelocity * duration) - Math.sin(direction));
        if (!Double.isFinite(newX)) {
            newX = x + velocity * duration * Math.cos(direction);
        }
        return newX;
    }

    public static double recalculateY(double y, double velocity, double angularVelocity, double duration, double direction) {
        double newY = y - velocity / angularVelocity *
                (Math.cos(direction + angularVelocity * duration) - Math.cos(direction));
        if (!Double.isFinite(newY)) {
            newY = y + velocity * duration * Math.sin(direction);
        }
        return newY;
    }

    public static int round(double value) {return (int) (value + 0.5);}
}