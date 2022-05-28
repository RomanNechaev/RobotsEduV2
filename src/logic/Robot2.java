package logic;

import static logic.MathOperations.*;
import static logic.MathOperations.asNormalizedRadians;
import static logic.RobotConstants.*;

public class Robot2 extends Robot {
    public volatile int health = 30;
    public volatile Bullet bullet = new Bullet(this.getX(), this.getY());

    public void shoot(int targetX, int targetY) {
        double angleToTarget = angleTo(this.getX(), this.getY(), targetX, targetY, this.getDirection());
        double angularVelocity = calculateAngularVelocity(angleToTarget, this.getDirection(), maxAngularVelocity);
        double duration = 10;
        synchronized (this) {
            setDirection(asNormalizedRadians(this.getDirection() + angularVelocity * duration));
        }
        bullet.move(targetX, targetY);
    }

}
