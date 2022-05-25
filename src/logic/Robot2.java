package logic;

import static logic.MathOperations.*;
import static logic.MathOperations.asNormalizedRadians;
import static logic.RobotConstants.*;
import static logic.RobotConstants.startY;

public class Robot2 extends Robot {

    public volatile Bullet bullet = new Bullet(this.getX(), this.getY());

    public void shoot(int targetX, int targetY) {
        bullet.move(targetX, targetY);
        double angleToTarget = angleTo(this.getX(), this.getY(), targetX, targetY, this.getDirection());
        double angularVelocity = calculateAngularVelocity(angleToTarget, this.getDirection(), maxAngularVelocity);
        double duration = 10;
        synchronized (this) {
            setDirection(asNormalizedRadians(this.getDirection() + angularVelocity * duration));
        }
    }

}
