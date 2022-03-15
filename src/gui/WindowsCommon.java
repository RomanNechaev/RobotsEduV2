package gui;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayDeque;

public abstract class WindowsCommon {
    public static ArrayDeque<WindowConfiguration> configs = new ArrayDeque<>();

    static void exitWindow(Component window) {
        if (window instanceof JInternalFrame) {
            ((JInternalFrame) window).addInternalFrameListener(new InternalFrameAdapter() {
                public void internalFrameClosing(InternalFrameEvent e) {
                    if (confirmClosing(e.getInternalFrame())) {
                        e.getInternalFrame().getDesktopPane().getDesktopManager().closeFrame(e.getInternalFrame());
                        configs.add(setConfig(e));
                    }
                }
            });
        } else {
            ((JFrame) window).addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    if (confirmClosing(e.getWindow())) {
                        e.getWindow().setVisible(false);
                        configs.add(setConfig(e));
                        writeConfig(configs);
                        System.exit(0);
                    }
                }
            });
        }
    }

    public static void writeConfig(ArrayDeque<WindowConfiguration> configs) {
        File file = new File(System.getProperty("user.home"), "data.bin");
        try {
            OutputStream os = new FileOutputStream(file);
            try {
                ObjectOutputStream oos =
                        new ObjectOutputStream(new BufferedOutputStream(os));
                try {
                    oos.writeObject(configs);
                    oos.flush();
                } finally {
                    oos.close();
                }
            } finally {
                os.close();
            }
            InputStream is = new FileInputStream(file);
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
        String[] options = {"закрыть", "отмена"};
        int answer = JOptionPane.showOptionDialog(window,
                "Вы уверены?",
                "Closing",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);
        return answer == 0;
    }

    public static boolean confirmClearing() {
        String[] options = {"да", "нет"};
        int answer = JOptionPane.showOptionDialog(null,
                "Лог заполнен. Добавить сообщение?",
                "Overflow",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);
        return answer == 0;
    }
}
