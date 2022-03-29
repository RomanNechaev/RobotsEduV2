package gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import logic.Const;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public abstract class WindowsCommon {
    public static ArrayNode result = JsonNodeFactory.instance.arrayNode();
    public static ObjectMapper mapper = new ObjectMapper();

    static void exitWindow(Component window, RobotConfig cfg) {
        if (window instanceof JInternalFrame) {
            ((JInternalFrame) window).addInternalFrameListener(new InternalFrameAdapter() {
                public void internalFrameClosing(InternalFrameEvent e) {
                    if (confirmClosing(e.getInternalFrame())) {
                        e.getInternalFrame().getDesktopPane().getDesktopManager().closeFrame(e.getInternalFrame());
                        if ("GameWindow".equals(e.getInternalFrame().getName()) && cfg.getX() != Const.startX && cfg.getY() != Const.startY)
                            writeConfig(cfg);
                        var jsnConfig = mapper.valueToTree(setConfig(e));
                        result.add(jsnConfig);
                    }
                }
            });
        } else {
            ((JFrame) window).addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    if (confirmClosing(e.getWindow())) {
                        e.getWindow().setVisible(false);
                        var t = mapper.valueToTree(setConfig(e));
                        result.add(t);
                        writeConfig(result);
                        System.exit(0);
                    }
                }
            });
        }
    }

    public static void writeConfig(ArrayNode result) {
        File file = new File(System.getProperty("user.home"), "data.bin");
        try (BufferedWriter writter = new BufferedWriter(new FileWriter(file))) {
            writter.write(result.toString());
            writter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeConfig(RobotConfig config) {
        File file = new File(System.getProperty("user.home"), "robot.bin");
        var jsnConfig = mapper.valueToTree(config);
        try (BufferedWriter writter = new BufferedWriter(new FileWriter(file))) {
            writter.write(jsnConfig.toString());
            writter.flush();
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
