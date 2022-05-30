package gui.frames;

import gui.state.RobotObserver;
import logic.Robot2;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class CoordinatesWindow extends JInternalFrame implements RobotObserver {

    private final TextArea area;
    private final TextArea area2;
    private Timer timer;

    public CoordinatesWindow(String title, Robot2 robot2) {
        super(title, true, true, true, true);
        area = new TextArea("");
        area.setSize(500, 300);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(area, BorderLayout.NORTH);

        area2 = new TextArea("");
        area2.setSize(500, 300);
        panel.add(area2, BorderLayout.SOUTH);

        getContentPane().add(panel);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        Timer timer  = initTimer();
        timer.schedule(new TimerTask() {
            public void run() {
                area2.setText("ROBOT2 \n" +"X: " + String.format("%.2f", robot2.getY()) + "\nY: " + String.format("%.2f", robot2.getY()) + "\nHeatlh: " + robot2.getHealth()
                        + "\nTargets: " + robot2.getTargetPoints());
            }
        }, 0, 100);
    }

    @Override
    public void update(double x, double y, double direction, int health , int targets) {
        area.setText("ROBOT1 \n" +"X: " + String.format("%.2f", x) + "\nY: " + String.format("%.2f", y) + "\nHeatlh: " + health
        + "\nTargets: " + targets);
    }

    private static java.util.Timer initTimer() {
        return new java.util.Timer("events generator", true);
    }
}