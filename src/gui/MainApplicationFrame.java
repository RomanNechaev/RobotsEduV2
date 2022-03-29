package gui;

import java.awt.*;
import java.beans.PropertyVetoException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import logic.Robot;
import log.Logger;

import static gui.WindowsCommon.*;
import static gui.WindowsConst.*;

public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();

    public MainApplicationFrame(Robot robot) throws IOException, ClassNotFoundException {
        RobotConfig cnf = new RobotConfig(robot);
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);
        setJMenuBar(new gui.MenuGenerator(MainApplicationFrame.this));
        setName("MainWindow");


        LogWindow logWindow = createLogWindow();
        GameWindow gameWindow = createGameWindow(robot);
        if (Files.exists(Path.of(System.getProperty("user.home"), "data.bin"))) {
            restoreConfiguration(logWindow, gameWindow);
        }
        if (Files.exists(Path.of(System.getProperty("user.home"), "robot.bin"))) {
            restoreConfig(robot);
        }

        addWindow(logWindow);
        addWindow(gameWindow);


        exitWindow(gameWindow, cnf);
        exitWindow(logWindow, cnf);
        exitWindow(MainApplicationFrame.this, cnf);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    private void restoreConfiguration(LogWindow logWindow, GameWindow gameWindow) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.home") + "/" + "data.bin"))) {
            ObjectMapper mapper = new ObjectMapper();
            WindowConfiguration[] configs = mapper.readValue(reader, WindowConfiguration[].class);
            for (WindowConfiguration config : configs) {
                switch (config.getName()) {
                    case "GameWindow" -> {
                        try {
                            gameWindow.setIcon(config.getIconState());
                        } catch (PropertyVetoException e) {
                            e.printStackTrace();
                        }
                        gameWindow.setLocation(config.getWindowLocationX(), config.getWindowLocationY());
                        gameWindow.setSize(config.getWindowWidth(), config.getWindowHeight());
                    }
                    case "LogWindow" -> {
                        try {
                            logWindow.setIcon(config.getIconState());
                        } catch (PropertyVetoException e) {
                            e.printStackTrace();
                        }
                        logWindow.setLocation(config.getWindowLocationX(), config.getWindowLocationY());
                        logWindow.setSize(config.getWindowWidth(), config.getWindowHeight());
                    }
                    case "MainWindow" -> {
                        MainApplicationFrame.this.setLocation(config.getWindowLocationX(), config.getWindowLocationY());
                        MainApplicationFrame.this.setState(config.getIconNumber());
                        MainApplicationFrame.this.desktopPane.setSize(config.getWindowWidth(), config.getWindowHeight());
                        MainApplicationFrame.this.setSize(config.getWindowWidth(), config.getWindowHeight());
                    }
                }
            }

        }
    }

    public void restoreConfig(Robot r) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.home") + "/" + "robot.bin"))) {
            ObjectMapper mapper = new ObjectMapper();
            var config = mapper.readTree(reader);
            r.setX(config.get("x").asDouble());
            r.setY(config.get("y").asDouble());
            r.setDirection(config.get("directionR").asDouble());
        }
    }

    protected GameWindow createGameWindow(Robot robot) {
        GameWindow gameWindow = new GameWindow(robot);
        gameWindow.setSize(gameWindowWidth, gameWindowHeight);
        return gameWindow;
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(logWindowWidth, logWindowHeight);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }
}