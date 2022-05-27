package gui.frames;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import logic.Robot;
import logic.Robot2;

public class GameWindow extends JInternalFrame {
    private final GameVisualizer mVisualizer;

    public GameWindow(Robot robot, Robot2 robot2, String title) {
        super(title, true, true, true, true);
        mVisualizer = new GameVisualizer(robot, robot2);
        mVisualizer.setFocusable(true);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(mVisualizer, BorderLayout.CENTER);
        setName("GameWindow");
        getContentPane().add(panel);
        pack();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }
}
