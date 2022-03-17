package gui;

import log.Logger;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

public class MenuGenerator extends JMenuBar {
    private final JFrame jframe;
    public MenuGenerator(JFrame jframe) {
        this.jframe = jframe;
        add(createLookParagraph());
        add(createTestParagraph());
        add(closingParagraph());
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

    private JMenu closingParagraph() {
        JMenu testMenu = new JMenu("Опции");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Опциональные команды");
        {
            JMenuItem addLogMessageItem = new JMenuItem("Выход", KeyEvent.VK_Q);
            addLogMessageItem.addActionListener((event) ->
            jframe.dispatchEvent(new WindowEvent(jframe, WindowEvent.WINDOW_CLOSING)));
            testMenu.add(addLogMessageItem);

        }
        return testMenu;
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
}