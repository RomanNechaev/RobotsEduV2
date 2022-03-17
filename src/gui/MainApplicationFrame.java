package gui;

import java.awt.*;
import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import javax.swing.*;

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
        setJMenuBar(new gui.MenuGenerator());
        setName("MainWindow");


        LogWindow logWindow = createLogWindow();
        GameWindow gameWindow = createGameWindow(robot);

      if (Files.exists(Path.of(System.getProperty("user.home"), "data.bin")))
        restoreConfiguration(logWindow, gameWindow);


        addWindow(logWindow);
        addWindow(gameWindow);


        exitWindow(gameWindow, cnf);
        exitWindow(logWindow, cnf);
        exitWindow(MainApplicationFrame.this, cnf);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    private void restoreConfiguration(LogWindow logWindow, GameWindow gameWindow) throws IOException, ClassNotFoundException {
        var configs = getConfig();
        while (!configs.isEmpty()) {
            WindowConfiguration config = configs.pollLast();
            switch (config.getName()) {
                case "GameWindow" -> {
                    try {
                        gameWindow.setIcon(config.getIconState());
                    } catch (PropertyVetoException e) {
                        e.printStackTrace();
                    }
                    gameWindow.setLocation(config.getLocationX(), config.getLocationY());
                    gameWindow.setSize(config.getWindowWidth(), config.getWindowHeight());
                }
                case "LogWindow" -> {
                    try {
                        logWindow.setIcon(config.getIconState());
                    } catch (PropertyVetoException e) {
                        e.printStackTrace();
                    }
                    logWindow.setLocation(config.getLocationX(), config.getLocationY());
                    logWindow.setSize(config.getWindowWidth(), config.getWindowHeight());
                }
                case "MainWindow" -> {
                    MainApplicationFrame.this.setState(config.getIconNumber());
                    MainApplicationFrame.this.setLocation(config.getLocationX(), config.getLocationY());
                    MainApplicationFrame.this.desktopPane.setSize(config.getWindowWidth(), config.getWindowHeight());
                }
            }
        }
    }

    public static ArrayDeque<WindowConfiguration> getConfig() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(
                    new FileInputStream(System.getProperty("user.home") + "/" + "data.bin"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        var configs = (ArrayDeque<WindowConfiguration>) objectInputStream.readObject();
        objectInputStream.close();
        return configs;
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