package gui.frames;

import java.awt.*;
import java.beans.PropertyVetoException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import javax.swing.*;

import com.sun.tools.javac.Main;
import gui.state.RobotConfig;
import gui.state.WindowConfiguration;
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
        setJMenuBar(new gui.menu.MenuGenerator(MainApplicationFrame.this));
        setName("MainWindow");


        LogWindow logWindow = createLogWindow();
        GameWindow gameWindow = createGameWindow(robot);
        CoordinatesWindow coordinatesWindow = createCoordinatesWindow();
        if (Files.exists(Path.of(System.getProperty("user.home"), "data.out"))) {
            restoreConfiguration(logWindow, gameWindow);
        }
        if (Files.exists(Path.of(System.getProperty("user.home"), "robot.bin"))) {
            restoreConfig(robot);
        }

        robot.subscribe(coordinatesWindow);

        addWindow(logWindow);
        addWindow(gameWindow);
        addWindow(coordinatesWindow);


        exitWindow(coordinatesWindow, cnf);
        exitWindow(gameWindow, cnf);
        exitWindow(logWindow, cnf);
        exitWindow(MainApplicationFrame.this, cnf);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    private void restoreConfiguration(LogWindow logWindow, GameWindow gameWindow) {
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
                    this.setState(config.getIconNumber());
                    this.setLocation(config.getWindowLocationX(), config.getWindowLocationY());
                    this.desktopPane.setSize(config.getWindowWidth(), config.getWindowHeight());
                    this.setSize(config.getWindowWidth(), config.getWindowHeight());
                }
            }
        }
    }

    public void restoreConfig(Robot r) {
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

    protected CoordinatesWindow createCoordinatesWindow() {
        CoordinatesWindow coordinatesWindow = new CoordinatesWindow("Координаты");
        coordinatesWindow.setSize(coordinatesWidth, coordinatesHeight);
        coordinatesWindow.setLocation(MainApplicationFrame.this.getWidth()  - coordinatesWidth / 2, 0);
        coordinatesWindow.isBackgroundSet();
        return coordinatesWindow;
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