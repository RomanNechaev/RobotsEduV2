package gui.menu;

import gui.frames.MainApplicationFrame;
import log.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;

public class MenuItemStore {
    private static JMenuItem[] lookParagraphItems;
    private static JMenuItem[] closingParagraphItems;
    private static JMenuItem[] testParagraphItems;

    public MenuItemStore(JFrame jframe) {
        lookParagraphItems = createLookParagraphItems(jframe);
        closingParagraphItems = createClosingParagraphItems(jframe);
        testParagraphItems = createTestParagraphItems();
    }

    private JMenuItem[] createLookParagraphItems(JFrame jframe) {
        JMenuItem systemLookAndFeel = JMenuItemFactory.createInstance(MainApplicationFrame.bundle.getString("scheme"), (event) -> {
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
            if (nameBundle.contains("text_en")) {
                MainApplicationFrame.updateLanguage("text_ru", new Locale("ru", "RU"));
            } else
                MainApplicationFrame.updateLanguage("text_en", new Locale("en", "Uk"));
            try {
                lookParagraphItems[0].setText(new String(MainApplicationFrame.bundle.getString("scheme").getBytes(StandardCharsets.ISO_8859_1),"windows-1251"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

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