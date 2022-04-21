package gui;

import gui.state.RobotConfig;
import gui.state.WindowConfiguration;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayDeque;

public abstract class WindowsCommon {
    public static ArrayDeque<WindowConfiguration> windowConfigs = new ArrayDeque<>();

    public static void exitWindow(Component window, RobotConfig robotConfig) {
        if (window instanceof JInternalFrame) {
            ((JInternalFrame) window).addInternalFrameListener(new InternalFrameAdapter() {
                public void internalFrameClosing(InternalFrameEvent e) {
                    if (confirmClosing(e.getInternalFrame())) {
                        e.getInternalFrame().setVisible(false);
                        windowConfigs.add(setConfig(e));
                    }
                }
            });
        } else {
            ((JFrame) window).addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    if (confirmClosing(e.getWindow())) {
                        e.getWindow().setVisible(false);
                        windowConfigs.add(setConfig(e));
                        writeConfig(windowConfigs);
                        writeConfig(robotConfig);
                        e.getWindow().dispose();
                        System.exit(0);
                    }
                }
            });
        }
    }

    public static void writeConfig(ArrayDeque<WindowConfiguration> configs) {
        File file = new File(System.getProperty("user.home"), "data.out");
        try (BufferedWriter buffWriter = new BufferedWriter(new FileWriter(file))) {
            while(!configs.isEmpty()) {
                var config = configs.pollLast();
                buffWriter.write(config.toString() + ",");
                buffWriter.flush();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void writeConfig(RobotConfig config) {
        File file = new File(System.getProperty("user.home"), "robot.bin");
        try (BufferedWriter buffWriter = new BufferedWriter(new FileWriter(file))) {
            buffWriter.write(config.toString());
            buffWriter.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    static WindowConfiguration setConfig(InternalFrameEvent e) {
        var iconState = e.getInternalFrame().isIcon();
        var windowLocation = e.getInternalFrame().getLocation();
        var windowSize = e.getInternalFrame().getSize();
        var name = e.getInternalFrame().getName();
        return new WindowConfiguration(windowLocation.x, windowLocation.y, windowSize.width, windowSize.height, iconState, name);
    }

    static WindowConfiguration setConfig(WindowEvent e) {
        var iconState = ((JFrame) e.getWindow()).getState();
        var windowLocation = e.getWindow().getLocation();
        var windowSize = e.getWindow().getSize();
        var name = e.getWindow().getName();
        return new WindowConfiguration(windowLocation.x, windowLocation.y, windowSize.width, windowSize.height, iconState, name);
    }

    static boolean confirmClosing(Component window) {
        String[] options = {MainApplicationFrame.bundle.getString("close"), MainApplicationFrame.bundle.getString("cancel")};
        int answer = JOptionPane.showOptionDialog(window,
                MainApplicationFrame.bundle.getString("confirm"),
                "Closing",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);
        return answer == 0;
    }

    public static boolean confirmClearing() {
        String[] options = {MainApplicationFrame.bundle.getString("yes"), MainApplicationFrame.bundle.getString("no")};
        int answer = JOptionPane.showOptionDialog(null,
                MainApplicationFrame.bundle.getString("full_log"),
                "Overflow",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);
        return answer == 0;
    }
}
