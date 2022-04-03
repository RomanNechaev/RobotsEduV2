package gui.menu;

import log.Logger;

import javax.swing.*;
import java.awt.event.WindowEvent;

public class MenuItemStore {
    private static JMenuItem[] lookParagraphItems;
    private static JMenuItem[] closingParagraphItems;
    private static JMenuItem[] testParagraphItems;

    public MenuItemStore(JFrame jframe){
        lookParagraphItems = createLookParagraphItems(jframe);
        closingParagraphItems = createClosingParagraphItems(jframe);
        testParagraphItems = createTestParagraphItems();
    }

    private JMenuItem[] createLookParagraphItems(JFrame jframe) {
        JMenuItem systemLookAndFeel = JMenuItemFactory.createInstance("Системная схема", (event) -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        });

        JMenuItem crossplatformLookAndFeel = JMenuItemFactory.createInstance("Универсальная схема", (event) -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        });
        return new JMenuItem[]{systemLookAndFeel, crossplatformLookAndFeel};
    }

    public static JMenuItem[] getLookParagraphItems() {
        return lookParagraphItems;
    }

    private JMenuItem[] createClosingParagraphItems(JFrame jframe) {
        JMenuItem addLogMessageItem = JMenuItemFactory.createInstance("Выход", (event) -> {
            jframe.dispatchEvent(new WindowEvent(jframe, WindowEvent.WINDOW_CLOSING));
        });
        return new JMenuItem[]{addLogMessageItem};
    }

    public static JMenuItem[] getClosingParagraphItems() {
        return closingParagraphItems;
    }

    private JMenuItem[] createTestParagraphItems() {
        {
            JMenuItem addLogMessageItem = JMenuItemFactory.createInstance("Сообщение в лог",
                    (event) -> Logger.debug("Новая строка"));

            JMenuItem deleteLogMessageItem = JMenuItemFactory.createInstance("Удалить старую запись",
                    (event) -> Logger.debugDelete());
            return new JMenuItem[]{addLogMessageItem, deleteLogMessageItem};
        }
    }

    public static JMenuItem[] getTestParagraphItems() {
        return testParagraphItems;
    }
}