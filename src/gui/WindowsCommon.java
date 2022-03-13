package gui;

import log.LogEntry;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.io.*;
import java.util.ArrayList;

public abstract class WindowsCommon {
    static void exitWindow(Component window) {
        if (window instanceof JInternalFrame) {
            ((JInternalFrame) window).addInternalFrameListener(new InternalFrameAdapter() {
                public void internalFrameClosing(InternalFrameEvent e) {
                    if (confirmClosing(e.getInternalFrame())) {
                        e.getInternalFrame().getDesktopPane().getDesktopManager().closeFrame(e.getInternalFrame());
                        var windowLocation = e.getInternalFrame().getLocation();
                        var windowLocationConfig = new Configuration(windowLocation.x, windowLocation.y);
                        writeConfig(windowLocationConfig);
                    }
                }
            });
        } else {
            ((JFrame) window).addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    if (confirmClosing(e.getWindow())) {
                        e.getWindow().setVisible(false);
                        var windowLocation = e.getWindow().getLocation();
                        var windowLocationConfig = new Configuration(windowLocation.x, windowLocation.y);
                        writeConfig(windowLocationConfig);
                        System.exit(0);
                    }
                }
            });
        }
    }

    public static void writeConfig(Configuration config) {
        File file = new File("data.bin");
        try {
            OutputStream os = new FileOutputStream(file);
            try {
                ObjectOutputStream oos =
                        new ObjectOutputStream(new BufferedOutputStream(os));
                try {
                    oos.writeObject(config);
                    oos.flush();
                } finally {
                    oos.close();
                }
            } finally {
                os.close();
            }
            InputStream is = new FileInputStream(file);
            try {
                ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(is));
                try {
                    Configuration restored = (Configuration) ois.readObject();
                    System.out.println(restored.getGameWindowLocationX() + ": " + restored.getGameWindowLocationY());
                    boolean bSame = (config == restored);
                    System.out.println("Same object: " + bSame);
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                } finally {
                    ois.close();
                }
            } finally {
                is.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
