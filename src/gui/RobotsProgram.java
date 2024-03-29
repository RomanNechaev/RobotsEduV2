package gui;

import gui.frames.MainApplicationFrame;
import logic.Robot;

import java.awt.Frame;
import java.io.IOException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class RobotsProgram {
    public static void main(String[] args) {
        Robot robot = new Robot();
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            MainApplicationFrame frame = null;
            try {
                frame = new MainApplicationFrame(robot);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            frame.pack();
            frame.setVisible(true);
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        });
    }
}
