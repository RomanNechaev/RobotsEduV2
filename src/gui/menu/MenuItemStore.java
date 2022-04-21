package gui.menu;

import gui.frames.MainApplicationFrame;
import log.Logger;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.util.Locale;

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

        JMenuItem changeLanguage = JMenuItemFactory.createInstance("Смена языка", (event) -> {
            var nameBundle = MainApplicationFrame.bundle.getBaseBundleName();
            if (nameBundle.contains("resources/text_en")) {
                MainApplicationFrame.updateLanguage("resources/text_ru", new Locale("ru", "RU"));
            } else
                MainApplicationFrame.updateLanguage("resources/text_en", new Locale("en", "Uk"));
            lookParagraphItems[0].setText((MainApplicationFrame.bundle.getString("system_scheme")));
            lookParagraphItems[1].setText(MainApplicationFrame.bundle.getString("universal_scheme"));
            testParagraphItems[0].setText(MainApplicationFrame.bundle.getString("log_message"));
            testParagraphItems[1].setText(MainApplicationFrame.bundle.getString("delete_entry"));
            closingParagraphItems[0].setText(MainApplicationFrame.bundle.getString("exit"));
            closingParagraphItems[1].setText(MainApplicationFrame.bundle.getString("change_language"));


        });
        return new JMenuItem[]{addLogMessageItem, changeLanguage};
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