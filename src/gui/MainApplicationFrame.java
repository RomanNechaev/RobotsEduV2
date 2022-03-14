package gui;

import java.awt.*;
import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import javax.swing.*;
import log.Logger;
import static gui.WindowsCommon.*;
import static gui.WindowsConst.*;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */
public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();
    public MainApplicationFrame() throws IOException, ClassNotFoundException {

        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);

        setContentPane(desktopPane);
        setJMenuBar(new gui.MenuGenerator());


        LogWindow logWindow = createLogWindow();
        GameWindow gameWindow = createGameWindow();
//        try {
//            var config = getConfig();
//            gameWindow.setIcon(config.getIconState());
//            gameWindow.setLocation(config.getLocationX(),config.getLocationY());
//            gameWindow.setSize(config.getWindowWidth(),config.getWindowHeight());
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (PropertyVetoException e) {
//            e.printStackTrace();
//        }

        var t = 0;

        while(t!=2) {
            switch (getConfig().getName()) {
                case "GameWindow":
                    try {
                        gameWindow.setIcon(getConfig().getIconState());
                    } catch (PropertyVetoException e) {
                        e.printStackTrace();
                    }
                    gameWindow.setLocation(getConfig().getLocationX(), getConfig().getLocationY());
                    gameWindow.setSize(getConfig().getWindowWidth(), getConfig().getWindowHeight());
                case "LogWindow":
                    try {
                        logWindow.setIcon(getConfig().getIconState());
                    } catch (PropertyVetoException e) {
                        e.printStackTrace();
                    }
                    logWindow.setLocation(getConfig().getLocationX(), getConfig().getLocationY());
                    logWindow.setSize(getConfig().getWindowWidth(), getConfig().getWindowHeight());
            }
            t+=1;
        }


        addWindow(logWindow);
        addWindow(gameWindow);

        exitWindow(gameWindow);
        exitWindow(logWindow);
        exitWindow(MainApplicationFrame.this);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    public static GameWindowConfiguration getConfig() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(
                    new FileInputStream(System.getProperty("user.home")+"\\"+"data.bin"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        GameWindowConfiguration config = (GameWindowConfiguration) objectInputStream.readObject();
        objectInputStream.close();
        return config;
    }

    protected GameWindow createGameWindow()
    {
        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(gameWindowWidth, gameWindowHeight);
        return gameWindow;
    }

    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        logWindow.setSize(logWindowWidth, logWindowHeight);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }
}
