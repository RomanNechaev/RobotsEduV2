package gui.frames;

import log.Logger;
import logic.Robot;
import logic.Robot2;

import static gui.WindowsConst.*;
import static gui.WindowsConst.logWindowHeight;

public class FramesStore {
    public static CoordinatesWindow createCoordinatesWindow(int width) {
        CoordinatesWindow coordinatesWindow = new CoordinatesWindow("Координаты");
        coordinatesWindow.setSize(coordinatesWidth, coordinatesHeight);
        coordinatesWindow.setLocation(width - coordinatesWidth / 2, 0);
        coordinatesWindow.isBackgroundSet();
        return coordinatesWindow;
    }

    public static GameWindow createGameWindow(Robot robot, Robot2 robot2, String title) {
        GameWindow gameWindow = new GameWindow(robot, robot2, title);
        gameWindow.setSize(gameWindowWidth, gameWindowHeight);
        return gameWindow;
    }

    public static LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(logWindowWidth, logWindowHeight);
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }
}
