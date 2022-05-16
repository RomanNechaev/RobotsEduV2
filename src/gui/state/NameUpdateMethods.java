package gui.state;

import javax.swing.*;
import java.util.Map;

import static gui.frames.MainApplicationFrame.bundle;

public class NameUpdateMethods {
    public static Map<String, String> itemNamesFromeng = Map.of(
            "Look Paragraph", "lookParagraph",
            "Test Paragraph", "testParagraph",
            "Option Paragraph",  "optionParagraph"
    );

    public static Map<String, String> itemNames = Map.of(
            "Режим отображения", "lookParagraph",
            "Тесты", "testParagraph",
            "Опции",  "optionParagraph"
    );
    public static Map<String, String> windNamesFromeng = Map.of(
            "Game Window", "gameWindow",
            "Log Window", "logWindow",
            "Coordinates Window",  "coordinatesWindow"
    );

    public static Map<String, String> windNames = Map.of(
            "Игровое поле", "gameWindow",
            "Протокол работы", "logWindow",
            "Координаты",  "coordinatesWindow"
    );

    public static void updateMenus(String bundleName, JMenuBar bar) {
        Map<String, String> swapMap;
        if (bundleName.contains("resources/text_en")) {
            swapMap = itemNames;

        } else {
            swapMap = itemNamesFromeng;
        }
        for (int i = 0; i < bar.getMenuCount(); i++) {
            bar.getMenu(i).setText(bundle.getString(swapMap.get(bar.getMenu(i).getText())));
        }
    }

    public static void updateTitles(String bundleName, JInternalFrame[] windows) {
        Map<String, String> swapMap;
        if (bundleName.contains("resources/text_en")) {
            swapMap = windNames;

        } else {
            swapMap = windNamesFromeng;
        }
        for (var w: windows) {
            w.setTitle(bundle.getString(swapMap.get(w.getTitle())));
        }
    }
}
