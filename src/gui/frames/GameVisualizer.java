package gui.frames;

import gui.state.RobotObserver;
import logic.Bullet;
import logic.Robot;
import logic.Robot2;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;

import static logic.MathOperations.round;

public class GameVisualizer extends JPanel implements RobotObserver {
    private final Robot robot;
    private final Robot2 robot2;

    private static Timer initTimer() {
        return new Timer("events generator", true);
    }

    private volatile int mTargetPositionX = 150;
    private volatile int mTargetPositionY = 100;

    private volatile int mTargetPositionX2 = 250;
    private volatile int mTargetPositionY2 = 200;

    public GameVisualizer(Robot robot, Robot2 robot2) {
        this.robot = robot;
        this.robot2 = robot2;
        setDoubleBuffered(true);
        robot.subscribe(this);
        robot2.subscribe(this);
        robot2.bullet.subscribe(this);
        Timer m_timer = initTimer();
        new Thread(() -> m_timer.schedule(new TimerTask() {
            public void run() {
                robot.move(mTargetPositionX, mTargetPositionY);
            }
        }, 0, 10)).start();


//        new Thread(() -> m_timer.schedule(new TimerTask() {
//            public void run() {
//                robot2.move(mTargetPositionX2, mTargetPositionY2);
//            }
//        }, 0, 10)).start();


        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setTargetPosition(e.getPoint());
                robot.move(mTargetPositionX, mTargetPositionY);
                //robot2.move(mTargetPositionX2, mTargetPositionY2);
                var t = new Thread(() ->  m_timer.schedule(new TimerTask() {
                    public void run() {
                        robot2.shoot(e.getPoint().x, e.getPoint().y);
                    }
                }, 0, 10));
                t.start();

                new Thread(()
                        -> {while (true) {if (robot2.bullet.getX()==e.getPoint().x &&
                robot2.bullet.getY() == e.getPoint().y)
                {
                    t.interrupt();
                    break;
                };   ;}});
                repaint();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_X) {
                    var point = new Point(300, 300);
                    setTargetPosition2(point);
                    robot2.shoot(mTargetPositionX2, mTargetPositionY2);
                    robot.move(500, 500);

                    repaint();
                }
            }
        });
    }

    protected void setTargetPosition(Point p) {
        mTargetPositionX = p.x;
        mTargetPositionY = p.y;
    }

    protected void setTargetPosition2(Point p) {
        mTargetPositionX2 = p.x;
        mTargetPositionY2 = p.y;
    }

    @Override
    public void update(double x, double y, double direction) {
        synchronized (robot) {
            EventQueue.invokeLater(this::repaint);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        drawRobot(g2d, round(robot.getX()), round(robot.getY()), robot.getDirection());
        drawRobot(g2d, round(robot2.getX()), round(robot2.getY()), robot2.getDirection());
        drawTarget(g2d, mTargetPositionX, mTargetPositionY);
        drawBullet(g2d, round(robot2.bullet.getX()), round(robot2.bullet.getY()));
    }

    public static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    public static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private void drawRobot(Graphics2D g, int x, int y, double direction) {
        int centerX = round(x);
        int centerY = round(y);
        AffineTransform t = AffineTransform.getRotateInstance(direction, centerX, centerY);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, centerX, centerY, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, centerX, centerY, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, centerX + 10, centerY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, centerX + 10, centerY, 5, 5);
    }

    private void drawTarget(Graphics2D g, int x, int y) {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }

    private void drawBullet(Graphics2D g, int x, int y) {

        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.RED);
        fillOval(g, x, y, 8, 8);
    }
}