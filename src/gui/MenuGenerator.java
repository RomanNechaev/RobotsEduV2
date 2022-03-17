package gui;

import log.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

public class MenuGenerator extends JMenuBar {
    public MenuGenerator() {
        add(createLookParagraph());
        add(createTestParagraph());
        add(createCloseParagraph());
    }

    private JMenu createLookParagraph() {
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription("Управление режимом отображения приложения");

        {
            JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
            systemLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeel);
        }

        {
            JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
            crossplatformLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(crossplatformLookAndFeel);
        }
        return lookAndFeelMenu;
    }

    private JMenu createTestParagraph() {
        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");

        {
            JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> Logger.debug("Новая строка"));
            testMenu.add(addLogMessageItem);

        }
        {
            JMenuItem addLogMessageItem = new JMenuItem("Удалить старую запись", KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> Logger.debugDelete());
            testMenu.add(addLogMessageItem);
        }
        return testMenu;
    }

    private JButton createCloseParagraph() {
        JButton closeButton = new JButton("Выход");
        closeButton.setMnemonic(KeyEvent.VK_Q);
        closeButton.setContentAreaFilled(false);
        closeButton.getAccessibleContext().setAccessibleDescription("Выход из программы");
        closeButton.addActionListener(e -> {
            Window window = SwingUtilities.windowForComponent((JButton) e.getSource());
            window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
        });
        return closeButton;
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
        }
    }
}