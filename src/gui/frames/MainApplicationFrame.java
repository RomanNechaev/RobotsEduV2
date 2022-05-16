package gui.frames;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import javax.swing.*;

import gui.state.NameUpdateMethods;
import gui.state.RestoreFunctions;
import gui.state.RobotConfig;
import logic.Robot;
import log.Logger;

import static gui.WindowsCommon.*;
import static gui.WindowsConst.*;
import static gui.frames.FramesStore.*;
import static gui.state.NameUpdateMethods.*;

public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();
    public static ResourceBundle bundle = ResourceBundle.getBundle("resources/text_ru");
    private static JMenuBar bar;
    private static JInternalFrame[] windows;

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
        GameWindow gameWindow = createGameWindow(robot, bundle.getString("gameWindow"));
        CoordinatesWindow coordinatesWindow = createCoordinatesWindow(MainApplicationFrame.this.getWidth());
        if (Files.exists(Path.of(System.getProperty("user.home"), "data.out"))) {
            RestoreFunctions.restoreConfiguration(this, this.desktopPane, logWindow, gameWindow);
        }
        if (Files.exists(Path.of(System.getProperty("user.home"), "robot.bin"))) {
            RestoreFunctions.restoreConfig(robot);
        }

        robot.subscribe(coordinatesWindow);

        addWindow(logWindow);
        addWindow(gameWindow);
        addWindow(coordinatesWindow);
        bar = this.getJMenuBar();
        windows = this.desktopPane.getAllFrames();

        exitWindow(coordinatesWindow, cnf);
        exitWindow(gameWindow, cnf);
        exitWindow(logWindow, cnf);
        exitWindow(MainApplicationFrame.this, cnf);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    public static void updateLanguage(String bundleName, Locale locale) {
        bundle = ResourceBundle.getBundle(bundleName, locale);
        NameUpdateMethods.updateMenus(bundleName, bar);
        NameUpdateMethods.updateTitles(bundleName, windows);
    }
}