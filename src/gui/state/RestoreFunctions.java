package gui.state;

import gui.frames.FramesNameConst;
import gui.frames.GameWindow;
import gui.frames.LogWindow;
import logic.Robot;

import javax.swing.*;
import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;

public final class RestoreFunctions {
    public static void restoreConfiguration(JFrame frame, JDesktopPane pane, LogWindow logWindow, GameWindow gameWindow) {
        var configs = restoreConfig();
        while (!configs.isEmpty()) {
            WindowConfiguration config = configs.pollLast();
            switch (config.getName()) {
                case FramesNameConst.gameWindow -> {
                    try {
                        gameWindow.setIcon(config.getIconState());
                    } catch (PropertyVetoException e) {
                        e.printStackTrace();
                    }
                    gameWindow.setLocation(config.getWindowLocationX(), config.getWindowLocationY());
                    gameWindow.setSize(config.getWindowWidth(), config.getWindowHeight());
                }
                case FramesNameConst.logWindow -> {
                    try {
                        logWindow.setIcon(config.getIconState());
                    } catch (PropertyVetoException e) {
                        e.printStackTrace();
                    }
                    logWindow.setLocation(config.getWindowLocationX(), config.getWindowLocationY());
                    logWindow.setSize(config.getWindowWidth(), config.getWindowHeight());
                }
                case FramesNameConst.mainWindow -> {
                    frame.setState(config.getIconNumber());
                    frame.setLocation(config.getWindowLocationX(), config.getWindowLocationY());
                    pane.setSize(config.getWindowWidth(), config.getWindowHeight());
                    frame.setSize(config.getWindowWidth(), config.getWindowHeight());
                }
            }
        }
    }

    public static void restoreConfig(Robot r) {
        try (BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.home") + "/" + "robot.bin"))) {
            var config = reader.readLine().split("\s");
            r.setX(Double.parseDouble(config[0].split(":")[1]));
            r.setY(Double.parseDouble(config[1].split(":")[1]));
            r.setDirection(Double.parseDouble(config[2].split(":")[1]));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static ArrayDeque<WindowConfiguration> restoreConfig() {
        var configs = new ArrayDeque<WindowConfiguration>();
        try (BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.home") + "/" + "data.out"))) {
            var line = reader.readLine().split(",");
            for (String s : line) {
                var tas = s.split("\s");
                configs.add(new WindowConfiguration(Integer.parseInt(tas[0].split(":")[1]),
                        Integer.parseInt(tas[1].split(":")[1]),
                        Integer.parseInt(tas[2].split(":")[1]),
                        Integer.parseInt(tas[3].split(":")[1]),
                        Boolean.parseBoolean(tas[4].split(":")[1])
                        , tas[5].split(":")[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return configs;
    }
}
