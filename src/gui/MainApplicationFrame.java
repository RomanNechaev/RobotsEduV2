package gui;

import java.awt.*;
import javax.swing.*;
import log.Logger;
import static gui.WindowsCommon.*;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */
public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();
    public MainApplicationFrame() {

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
        addWindow(logWindow);
        addWindow(gameWindow);

        exitWindow(gameWindow);
        exitWindow(logWindow);
        exitWindow(MainApplicationFrame.this);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    protected GameWindow createGameWindow()
    {
        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);
        return gameWindow;
    }

    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
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
