package gui.menu;

import log.Logger;
import javax.swing.*;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static gui.frames.MainApplicationFrame.*;

public class MenuItemStore {
    public static Map<String, String> itemNamesFromeng = Map.of(
            "System scheme", "systemLookAndFeel",
            "Universal scheme", "crossplatformLookAndFeel",
            "Exit",  "closeMain",
            "Change language","changeLanguage",
            "Message to the log", "addLogMessage",
            "Delete old entry", "deleteLogMessage"
    );

    public static Map<String, String> itemNames = Map.of(
            "Системная схема", "systemLookAndFeel",
            "Универсальная схема", "crossplatformLookAndFeel",
            "Выход",  "closeMain",
            "Смена языка","changeLanguage",
            "Сообщение в лог", "addLogMessage",
            "Удалить старую запись", "deleteLogMessage"
    );

    private static JMenuItem[] lookParagraphItems;
    private static JMenuItem[] closingParagraphItems;
    private static JMenuItem[] testParagraphItems;

    public MenuItemStore(JFrame jframe){
        lookParagraphItems = createLookParagraphItems();
        closingParagraphItems = createOptionParagraphItems(jframe);
        testParagraphItems = createTestParagraphItems();
    }

    private JMenuItem[] createLookParagraphItems() {
        JMenuItem systemLookAndFeel = JMenuItemFactory.createInstance(bundle.getString("systemLookAndFeel"), (event) -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        });

        JMenuItem crossplatformLookAndFeel = JMenuItemFactory.createInstance(bundle.getString("crossplatformLookAndFeel"), (event) -> {
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

    private JMenuItem[] createOptionParagraphItems(JFrame jframe) {
        JMenuItem closeMain = JMenuItemFactory.createInstance(bundle.getString("closeMain"), (event) -> {
            jframe.dispatchEvent(new WindowEvent(jframe, WindowEvent.WINDOW_CLOSING));
        });

        JMenuItem changeLanguage = JMenuItemFactory.createInstance(bundle.getString("changeLanguage"), (event) -> {
            updateNames();
        });

        return new JMenuItem[]{closeMain, changeLanguage};
    }

    public static JMenuItem[] getOptionParagraphItems() {
        return closingParagraphItems;
    }

    private JMenuItem[] createTestParagraphItems() {
        {
            JMenuItem addLogMessage = JMenuItemFactory.createInstance(bundle.getString("addLogMessage"),
                    (event) -> Logger.debug(bundle.getString("newString")));

            JMenuItem deleteLogMessage = JMenuItemFactory.createInstance(bundle.getString("deleteLogMessage"),
                    (event) -> Logger.debugDelete());
            return new JMenuItem[]{addLogMessage, deleteLogMessage};
        }
    }

    public static JMenuItem[] getTestParagraphItems() {
        return testParagraphItems;
    }


    public static void updateNames(){
        List<JMenuItem[]> itemArrays = Arrays.asList(lookParagraphItems, closingParagraphItems, testParagraphItems);
        Map<String, String> swapMap;
        var nameBundle = bundle.getBaseBundleName();
        if (nameBundle.contains("resources/text_en")) {
            swapMap = itemNamesFromeng;
            updateLanguage("resources/text_ru", new Locale("ru", "RU"));

        } else {
            swapMap = itemNames;
            updateLanguage("resources/text_en", new Locale("en", "UK"));
        }

        for (var array: itemArrays) {
            for (var item: array) {
                item.setText(bundle.getString(swapMap.get(item.getText())));
            }
        }
    }
}