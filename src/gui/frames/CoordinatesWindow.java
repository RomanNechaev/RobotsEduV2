package gui.frames;

import gui.state.RobotObserver;

import javax.swing.*;
import java.awt.*;

public class CoordinatesWindow extends JInternalFrame implements RobotObserver {

    private final TextArea area;

    public CoordinatesWindow(String title) {
        super(title, true, true, true, true);
        area = new TextArea("");
        area.setSize(500, 500);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(area, BorderLayout.CENTER);
        getContentPane().add(panel);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    @Override
    public void update(double x, double y, double direction) {
        area.setText("X: " + String.format("%.2f", x) + "\nY: " + String.format("%.2f", y));
    }
}