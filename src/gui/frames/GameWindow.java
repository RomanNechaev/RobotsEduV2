package gui.frames;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import logic.Robot;

public class GameWindow extends JInternalFrame
{
    private final GameVisualizer mVisualizer;
    public GameWindow(Robot robot, String title)
    {
        super(title, true, true, true, true);
        mVisualizer = new GameVisualizer(robot);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(mVisualizer, BorderLayout.CENTER);
        setName("GameWindow");
        getContentPane().add(panel);
        pack();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }
}
