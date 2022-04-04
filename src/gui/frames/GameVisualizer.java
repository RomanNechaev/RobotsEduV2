package gui.frames;

import gui.state.RobotObserver;
import logic.Robot;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;

import static logic.MathOperations.round;

public class GameVisualizer extends JPanel implements RobotObserver {
    private final Robot robot;
    private static Timer initTimer()
    {
        return new Timer("events generator", true);
    }
    private volatile int mTargetPositionX = 150;
    private volatile int mTargetPositionY = 100;

    public GameVisualizer(Robot robot)
    {
        this.robot = robot;
        setDoubleBuffered(true);
        robot.subscribe(this);
        Timer m_timer = initTimer();
        new Thread(() -> m_timer.schedule(new TimerTask() {
            public void run() {
                robot.move(mTargetPositionX, mTargetPositionY);
            }
        }, 0, 10)).start();

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    setTargetPosition(e.getPoint());
                    robot.move(mTargetPositionX, mTargetPositionY);
                    repaint();
                }
            });
    }

    protected void setTargetPosition(Point p)
    {
        mTargetPositionX = p.x;
        mTargetPositionY = p.y;
    }

    @Override
    public void update(double x, double y, double direction) {
        synchronized(robot) {
            EventQueue.invokeLater(this::repaint);
        }
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        drawRobot(g2d, round(robot.getX()), round(robot.getY()), robot.getDirection());
        drawTarget(g2d, mTargetPositionX, mTargetPositionY);
    }

    public static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    public static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private void drawRobot(Graphics2D g, int x, int y, double direction)
    {
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

    private void drawTarget(Graphics2D g, int x, int y)
    {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }
}