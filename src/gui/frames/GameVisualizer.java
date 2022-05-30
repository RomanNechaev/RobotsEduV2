package gui.frames;

import gui.state.RobotObserver;
import logic.*;
import logic.Robot;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.*;

import static logic.MathOperations.round;

public class GameVisualizer extends JPanel implements RobotObserver {
    private final Robot robot;
    private final Robot2 robot2;
    private final Enemy enemy1;
    private final Enemy2 enemy2;
    private final Enemy3 enemy3;
    private final Obstracle obstracle1;
    private final Bonus bonus1;
    private final Bonus bonus2;
    private final Bonus bonus3;
    private final Target target1;
    private final Target target2;


    private static Timer initTimer() {
        return new Timer("events generator", true);
    }

    private volatile int mTargetPositionX = 150;
    private volatile int mTargetPositionY = 100;

    private volatile int mTargetPositionX2 = 250;
    private volatile int mTargetPositionY2 = 200;

    private volatile boolean robot1IsLive = true;
    private volatile boolean robot2IsLive = true;
    private volatile boolean enemy1IsLive = true;
    private volatile boolean enemy2IsLive = true;
    private volatile boolean enemy3IsLive = true;
    private volatile boolean bulletIsPaint = false;
    private volatile boolean bonus1IsPaint = true;
    private volatile boolean bonus2IsPaint = true;
    private volatile boolean bonus3IsPaint = true;
    private volatile Integer robot1SpeedDrawing = 1;
    private volatile Integer robot2SpeedDrawing = 3;
    private final int width = 400;
    private final int height = 400;

    private final Timer enemyTimer;
    private final Timer enemy2Timer;
    private final Timer enemy3Timer;
    private final Timer target1Timer;
    private final Timer target2Timer;


    public GameVisualizer(Robot robot, Robot2 robot2) {
        this.robot = robot;
        this.robot2 = robot2;
        setFocusable(true);
        setDoubleBuffered(true);
        robot.subscribe(this);
        robot2.subscribe(this);
        robot2.bullet.subscribe(this);
        enemy1 = new Enemy();
        enemy2 = new Enemy2();
        enemy3 = new Enemy3();
        bonus1 = new Bonus(179, 372);
        bonus2 = new Bonus(123, 189);
        bonus3 = new Bonus(423, 309);
        target1 = new Target(getRandomNumber(width), getRandomNumber(height));
        target2 = new Target(getRandomNumber(width), getRandomNumber(height));
        enemy1.subscribe(this);
        enemy2.subscribe(this);
        enemy3.subscribe(this);
        Timer m_timer = initTimer();
        AtomicReference<Timer> robot2Timer = new AtomicReference<>(initTimer());
        enemyTimer = initTimer();
        enemy2Timer = initTimer();
        enemy3Timer = initTimer();
        target1Timer = initTimer();
        target2Timer = initTimer();
        obstracle1 = new Obstracle(100, 100, 50, 60);
        m_timer.schedule(new TimerTask() {
            public void run() {
                robot.move(round(target1.getX()), round(target1.getY()));
                repaint();
            }
        }, 0, robot1SpeedDrawing);

        target1Timer.schedule(new TimerTask() {
            public void run() {
                target1.setX(getRandomNumber(width));
                target1.setY(getRandomNumber(height));
                repaint();
            }
        }, 0, 5000);

        target2Timer.schedule(new TimerTask() {
            public void run() {
                target2.setX(getRandomNumber(width));
                target2.setY(getRandomNumber(height));
                repaint();
            }
        }, 0, 5000);

        enemyTimer.schedule(new TimerTask() {
            public void run() {
                enemy1.move(round(robot2.getX()), round(robot2.getY()));
                repaint();
            }
        }, 0, 10);

        enemy2Timer.schedule(new TimerTask() {
            public void run() {
                enemy2.move(round(robot.getX()), round(robot.getY()));
                repaint();
            }
        }, 0, 7);

        enemy3Timer.schedule(new TimerTask() {
            public void run() {
                enemy3.move(round(robot.getX()), round(robot.getY()));
                repaint();
            }
        }, 0, 5);


        var taskForRobot = new TimerTask() {
            public void run() {
                robot2.move(round(target2.getX()), round(target2.getY()));
                repaint();
            }
        };

        robot2Timer.get().schedule(taskForRobot, 0, robot2SpeedDrawing);


        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setTargetPosition(e.getPoint());
                robot.move(mTargetPositionX, mTargetPositionY);
                robot2.move(mTargetPositionX2, mTargetPositionY2);
                repaint();
            }
        });


        new Thread(()
                -> {
            while (true) {
                synchronized (robot) {
                    if (!robot2IsLive && !robot1IsLive) {
                        JOptionPane.showMessageDialog(null,
                                "Игра закончена");
                        m_timer.cancel();
                        robot2Timer.get().cancel();
                        enemyTimer.cancel();
                        enemy2Timer.cancel();
                        enemy3Timer.cancel();
                        break;
                    }
                }
            }
        }).start();

        new Thread(()
                -> {
            while (true) {
                synchronized (robot2) {
                    if (robot2.getHealth() == 0) {
                        robot2IsLive = false;
                        robot2Timer.get().cancel();
                        break;
                    }
                    if (checkIntersection(robot2, target2, 25)) {
                        robot2.setTargetPoints(robot2.getTargetPoints() + 1);
                        target2.setX(getRandomNumber(width));
                        target2.setY(getRandomNumber(height));

                    }
                    repaint();
                }
            }
        }).start();

        new Thread(()
                -> {
            while (true) {
                synchronized (robot) {
                    if (robot.getHealth() == 0) {
                        robot1IsLive = false;
                        m_timer.cancel();
                        break;
                    }
                    if (checkIntersection(robot, target1, 25)) {
                        robot.setTargetPoints(robot.getTargetPoints() + 1);
                        target1.setX(getRandomNumber(width));
                        target1.setY(getRandomNumber(height));

                    }
                    repaint();
                }
            }
        }).start();

        new Thread(()
                -> {
            while (true) {
                synchronized (robot2) {
                    if (checkIntersection(robot2, bonus1, 20)) {
                        robot2.setHealth(robot2.getHealth() + 1);
                        System.out.println(robot2.getHealth());
                        bonus1IsPaint = false;
                        break;
                    }
                }
                repaint();
            }
        }).start();

        new Thread(()
                -> {
            while (true) {
                synchronized (robot) {
                    if (checkIntersection(robot, bonus1, 20)) {
                        robot.setHealth(robot.getHealth() + 1);
                        System.out.println(robot.getHealth());
                        bonus1IsPaint = false;
                        break;
                    }
                }
                repaint();
            }
        }).start();

        new Thread(()
                -> {
            while (true) {
                synchronized (bonus2) {
                    if (checkIntersection(robot2, bonus2, 15)) {
                        if (robot2.getTargetPoints() != 0)
                            robot2.setTargetPoints(robot2.getTargetPoints() * 2);
                        bonus2IsPaint = false;
                        break;
                    }
                }
                repaint();
            }
        }).start();

        new Thread(()
                -> {
            while (true) {

                synchronized (bonus2) {
                    if (checkIntersection(robot, bonus2, 15)) {
                        if (robot.getTargetPoints() != 0)
                            robot.setTargetPoints(robot.getTargetPoints() * 2);
                        bonus2IsPaint = false;
                        break;
                    }
                }
                repaint();
            }
        }).start();

        new Thread(()
                -> {
            while (true) {
                synchronized (bonus3) {
                    if (checkIntersection(robot2, bonus3, 32)) {
                        robot2SpeedDrawing -= 1;
                        bonus3IsPaint = false;
                        break;
                    }
                }
                repaint();
            }
        }).start();

        new Thread(()
                -> {
            while (true) {

                synchronized (bonus3) {
                    if (checkIntersection(robot, bonus3, 32)) {
                        if (robot.getTargetPoints() != 0)
                            robot.setTargetPoints(robot.getTargetPoints() * 2);
                        robot1SpeedDrawing -= 1;
                        bonus3IsPaint = false;
                        break;
                    }
                }
                repaint();
            }
        }).start();


//        new Thread(()
//                -> {
//            while (true) {
//
//                synchronized (obstracle1) {
//                    if (robot.getX()
//                }
//            }
//        }).start();

        //логика снятие хп робота врагом
        new Thread(()
                -> {
            while (true) {

                synchronized (robot) {
                    if (robot.getHealth() == 0) {
                        robot1IsLive = false;
                        m_timer.cancel();
                        target1Timer.cancel();
                        break;

                    }
                    if (MathOperations.distance(robot.getX(), robot.getY(), enemy1.getX(), enemy1.getY()) <
                            10
                    ) {
                        robot.setHealth(robot.getHealth() - 1);
                        try {
                            Thread.currentThread().sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(robot.getHealth());
                    }
                }
                repaint();
            }
        }).start();

        new Thread(()
                -> {
            while (true) {

                synchronized (robot) {
                    if (robot.getHealth() == 0) {
                        robot1IsLive = false;
                        m_timer.cancel();
                        target1Timer.cancel();
                        break;
                    }
                    if (MathOperations.distance(robot.getX(), robot.getY(), enemy2.getX(), enemy2.getY()) <
                            10
                    ) {
                        robot.setHealth(robot.getHealth() - 1);
                        try {
                            Thread.currentThread().sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(robot.getHealth());
                    }
                }
                repaint();
            }
        }).start();

        new Thread(()
                -> {
            while (true) {

                synchronized (robot) {
                    if (robot.getHealth() == 0) {
                        robot1IsLive = false;
                        m_timer.cancel();
                        target1Timer.cancel();

                        break;
                    }
                    if (MathOperations.distance(robot.getX(), robot.getY(), enemy3.getX(), enemy3.getY()) <
                            10
                    ) {
                        robot.setHealth(robot.getHealth() - 1);
                        try {
                            Thread.currentThread().sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(robot.getHealth());
                    }
                }
                repaint();
            }
        }).start();

        new Thread(()
                -> {
            while (true) {

                synchronized (robot2) {
                    if (robot2.getHealth() == 0) {
                        robot2IsLive = false;
                        robot2Timer.get().cancel();
                        target2Timer.cancel();
                        break;
                    }
                    if (MathOperations.distance(robot2.getX(), robot2.getY(), enemy1.getX(), enemy1.getY()) <
                            10
                    ) {
                        robot2.setHealth(robot2.getHealth() - 1);
                        try {
                            Thread.currentThread().sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(robot2.getHealth());
                    }
                }
                repaint();
            }
        }).start();

        new Thread(()
                -> {
            while (true) {

                synchronized (robot2) {
                    if (robot2.getHealth() == 0) {
                        robot2IsLive = false;
                        robot2Timer.get().cancel();
                        target2Timer.cancel();
                        break;
                    }
                    if (MathOperations.distance(robot2.getX(), robot2.getY(), enemy2.getX(), enemy2.getY()) <
                            10
                    ) {
                        robot2.setHealth(robot2.getHealth() - 1);
                        try {
                            Thread.currentThread().sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(robot2.getHealth());
                    }
                }
                repaint();
            }
        }).start();

        new Thread(()
                -> {
            while (true) {

                synchronized (robot2) {
                    if (robot2.getHealth() == 0) {
                        robot2IsLive = false;
                        robot2Timer.get().cancel();
                        target2Timer.cancel();
                        break;
                    }
                    if (MathOperations.distance(robot2.getX(), robot2.getY(), enemy3.getX(), enemy3.getY()) <
                            10
                    ) {
                        robot2.setHealth(robot2.getHealth() - 1);
                        try {
                            Thread.currentThread().sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(robot2.getHealth());
                    }
                }
                repaint();
            }
        }).start();


        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_X && (enemy1IsLive || enemy2IsLive || enemy3IsLive) && robot2IsLive) {
                    robot2Timer.get().cancel();
                    robot2.bullet.setX(robot2.getX());
                    robot2.bullet.setY(robot2.getY());
                    bulletIsPaint = true;
                    Timer bulletTimer = initTimer();
                    var enemy = getNearestEnemy(robot2, enemy1, enemy2, enemy3);
                    var enemyX = enemy.getX();
                    var enemyY = enemy.getY();
                    bulletTimer.schedule(new TimerTask() {
                        public void run() {
                            robot2.shoot(round(enemyX), round(enemyY));
                        }
                    }, 0, 1);

                    new Thread(()
                            -> {
                        while (true) {
                            //if robot.health > 0
                            if (round(robot2.bullet.getX()) == round(enemyX) &&
                                    round(robot2.bullet.getY()) == round(enemyY)) {
                                bulletTimer.cancel();
                                robot2Timer.get().cancel();
                                robot2Timer.set(initTimer());
                                robot2Timer.get().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        robot2.move(round(robot.getX()), round(robot.getY()));
                                    }
                                }, 50, 9);
                                robot2.bullet.setX(robot2.getX());
                                robot2.bullet.setY(robot2.getY());
                                bulletIsPaint = false;
                                break;
                            }
                            repaint();
                        }

                    }).start();

                    if (enemy1IsLive && enemy.getNamE().equals("enemy")) {
                        new Thread(()
                                -> {
                            while (true) {
                                synchronized (enemy1) {
                                    if (checkIntersection(robot2.bullet, enemy1, 30, 10)) {
                                        enemy1.setHealth(enemy1.getHealth() - 1);
                                        if (enemy1.getHealth() == 0) {
                                            enemy1IsLive = false;
                                            bulletIsPaint = false;
                                        }
                                        bulletIsPaint = false;
                                        break;
                                    }
                                }
                            }
                            repaint();
                        }).start();
                    }

                    if (enemy2IsLive && enemy.getNamE().equals("enemy2")) {
                        new Thread(()
                                -> {
                            while (true) {
                                synchronized (enemy2) {
                                    if (checkIntersection(robot2.bullet, enemy2, 30, 10)) {
                                        enemy2.setHealth(enemy2.getHealth() - 1);
                                        if (enemy2.getHealth() == 0) {
                                            enemy2IsLive = false;
                                            bulletIsPaint = false;
                                        }
                                        bulletIsPaint = false;
                                        break;
                                    }
                                }
                            }
                            repaint();
                        }).start();
                    }
                    if (enemy3IsLive && enemy.getNamE().equals("enemy3")) {
                        new Thread(()
                                -> {
                            while (true) {
                                synchronized (enemy3) {
                                    if (checkIntersection(robot2.bullet, enemy3, 30, 10)) {
                                        enemy3.setHealth(enemy3.getHealth() - 1);
                                        if (enemy3.getHealth() == 0) {
                                            enemy3IsLive = false;
                                            bulletIsPaint = false;
                                        }
                                        bulletIsPaint = false;
                                        break;
                                    }
                                }
                            }
                            repaint();
                        }).start();
                    }
                }
                repaint();
            }
        });
    }

    private Enemy getNearestEnemy(Robot robot, Enemy... enemies) {
        double min = Double.MAX_VALUE;
        Enemy enemy = null;
        for (var e : enemies) {
            if (robot.getDistanceTo(e) < min && e.getHealth() > 0) {
                min = robot.getDistanceTo(e);
                enemy = e;
            }
        }
        return enemy;
    }

    private int getRandomNumber(int From) {
        // int rand = ThreadLocalRandom.current().nextInt(1,From);
        return 1 + (int) (Math.random() * From);
    }

    private <T extends Entity> boolean checkIntersection(T firs_entity, T second_entity, int delta) {
        return round(firs_entity.getX()) <= round(second_entity.getX() + delta) &&
                round(firs_entity.getX()) >= round(second_entity.getX() - delta) &&
                round(firs_entity.getY()) <= round(second_entity.getY() + delta)
                && round(firs_entity.getY()) >= round(second_entity.getY() - delta);
    }

    private <T extends Entity> boolean checkIntersection(T firs_entity, T second_entity, int delta, int delta2) {
        return round(firs_entity.getX()) <= round(second_entity.getX() + delta) &&
                round(firs_entity.getX()) >= round(second_entity.getX() - delta) &&
                round(firs_entity.getY()) <= round(second_entity.getY() + delta2)
                && round(firs_entity.getY()) >= round(second_entity.getY() - delta2);
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
    public void update(double x, double y, double direction, int health, int targets) {
        EventQueue.invokeLater(this::repaint);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        if (robot1IsLive && robot2IsLive) {


            if (bulletIsPaint) {
                drawBullet(g2d, round(robot2.bullet.getX()), round(robot2.bullet.getY()));
            }
            if (bonus1IsPaint) {
                drawBonus(g2d, round(bonus1.getX()), round(bonus1.getY()));
            }
            if (bonus2IsPaint) {
                drawBonus2(g2d, round(bonus2.getX()), round(bonus2.getY()));
            }
            if (bonus3IsPaint) {
                drawBonus3(g2d, round(bonus3.getX()), round(bonus3.getY()));
            }
            if (robot1IsLive) {
                drawRobot(g2d, round(robot.getX()), round(robot.getY()), robot.getDirection());
            }
            if (robot2IsLive) {
                drawRobot(g2d, round(robot2.getX()), round(robot2.getY()), robot2.getDirection());
            }
            drawTarget(g2d, mTargetPositionX, mTargetPositionY);
            drawTarget(g2d, round(target1.getX()), round(target1.getY()), Color.pink);
            drawTarget(g2d, round(target2.getX()), round(target2.getY()), Color.darkGray);
            if (enemy1IsLive && enemy2IsLive && enemy3IsLive) {
                drawEnemy(g2d, round(enemy1.getX()), round(enemy1.getY()), round(enemy1.getDirection()));
                drawEnemy(g2d, round(enemy2.getX()), round(enemy2.getY()), round(enemy2.getDirection()));
                drawEnemy(g2d, round(enemy3.getX()), round(enemy3.getY()), round(enemy3.getDirection()));
            } else {
                if (enemy1IsLive) {
                    drawEnemy(g2d, round(enemy1.getX()), round(enemy1.getY()), round(enemy1.getDirection()));
                }
                if (enemy2IsLive) {
                    drawEnemy(g2d, round(enemy2.getX()), round(enemy2.getY()), round(enemy2.getDirection()));
                }
                if (enemy3IsLive) {
                    drawEnemy(g2d, round(enemy3.getX()), round(enemy3.getY()), round(enemy3.getDirection()));
                }
            }
        }
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

    private void drawEnemy(Graphics2D g, int x, int y, double direction) {
        int centerX = round(x);
        int centerY = round(y);
        AffineTransform t = AffineTransform.getRotateInstance(direction, centerX, centerY);
        g.setTransform(t);
        g.setColor(Color.BLUE);
        fillOval(g, centerX, centerY, 40, 30);
        g.setColor(Color.ORANGE);
        drawOval(g, centerX, centerY, 40, 30);
        g.setColor(Color.WHITE);
        fillOval(g, centerX - 5, centerY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, centerX - 5, centerY, 5, 5);
        g.setColor(Color.WHITE);
        fillOval(g, centerX + 5, centerY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, centerX + 5, centerY, 5, 5);
        g.setColor(Color.GREEN);
        g.drawLine(centerX - 4, centerY + 5, centerX + 5, centerY + 2);
    }

    private void drawObstacle(Graphics2D g, double x, double y, int width, int height) {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        int centerX = round(x);
        int centerY = round(y);
        g.setColor(Color.orange);
        g.fillRect(centerX, centerY, width, height);
        g.drawRect(centerX, centerY, width, height);
    }

    private void drawBonus(Graphics2D g, double x, double y) {
        int centerX = round(x);
        int centerY = round(y);
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.ORANGE);
        fillOval(g, centerX, centerY, 20, 20);
        g.setColor(Color.BLACK);
        drawOval(g, centerX, centerY, 20, 20);
        g.setColor(Color.RED);
        fillOval(g, centerX, centerY, 8, 8);
        g.setColor(Color.BLACK);
        drawOval(g, centerX, centerY, 8, 8);
    }

    private void drawBonus2(Graphics2D g, double x, double y) {
        int centerX = round(x);
        int centerY = round(y);
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, centerX, centerY, 15, 15);
        g.setColor(Color.BLACK);
        drawOval(g, centerX, centerY, 15, 15);
        g.setColor(Color.ORANGE);
        fillOval(g, centerX, centerY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, centerX, centerY, 5, 5);
    }

    private void drawBonus3(Graphics2D g, double x, double y) {
        int centerX = round(x);
        int centerY = round(y);
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.yellow);
        fillOval(g, centerX, centerY, 32, 32);
        g.setColor(Color.BLACK);
        drawOval(g, centerX, centerY, 32, 32);
        g.setColor(Color.GREEN);
        fillOval(g, centerX, centerY, 14, 14);
        g.setColor(Color.BLACK);
        drawOval(g, centerX, centerY, 14, 14);
    }

    private void drawTarget(Graphics2D g, int x, int y, Color cl) {
        int centerX = round(x);
        int centerY = round(y);
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(cl);
        fillOval(g, centerX, centerY, 25, 25);
        g.setColor(Color.BLACK);
        drawOval(g, centerX, centerY, 25, 25);
    }
}