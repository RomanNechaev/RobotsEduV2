package gui;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class WindowsCommon {
    static void exitWindow(Component window) {
        if (window instanceof JInternalFrame) {
            ((JInternalFrame) window).addInternalFrameListener(new InternalFrameAdapter() {
                public void internalFrameClosing(InternalFrameEvent e) {
                    if (confirmClosing(e.getInternalFrame())) {
                        e.getInternalFrame().getDesktopPane().getDesktopManager().closeFrame(e.getInternalFrame());
                        var iconState = e.getInternalFrame().isIcon();
                        var windowLocation = e.getInternalFrame().getLocation();
                        var windowSize = e.getInternalFrame().getSize();
                        var name = e.getInternalFrame().getName();
                        var windowLocationConfig = new GameWindowConfiguration(windowLocation.x, windowLocation.y, windowSize.width, windowSize.height, iconState, name);
                        writeConfig(windowLocationConfig);
                    }
                }
            });
        } else {
            ((JFrame) window).addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    if (confirmClosing(e.getWindow())) {
                        e.getWindow().setVisible(false);
                        System.exit(0);
                    }
                }
            });
        }
    }

    public static void writeConfig(GameWindowConfiguration config) {
        File file = new File(System.getProperty("user.home"), "data.bin");
        var append = Files.exists(Path.of(System.getProperty("user.home"), "data.bin"));
        try {
            OutputStream os = new FileOutputStream(file, append);
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
//            InputStream is = new FileInputStream(file);
//            try {
//                ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(is));
//                try {
//                    GameWindowConfiguration restored = (GameWindowConfiguration) ois.readObject();
//                    System.out.println(restored.getLocationX() + ": " + restored.getLocationY() + restored.getName());
//                    boolean bSame = (config == restored);
//                    System.out.println("Same object: " + bSame);
//                } catch (ClassNotFoundException ex) {
//                    ex.printStackTrace();
//                } finally {
//                    ois.close();
//                }
//            } finally {
//                is.close();
//            }
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
