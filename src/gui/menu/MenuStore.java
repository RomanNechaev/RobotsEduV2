package gui.menu;

import gui.frames.MainApplicationFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

import static gui.frames.MainApplicationFrame.bundle;

public class MenuStore extends Component {
    private JFrame jframe;
    private MenuItemStore itemStore;

    public MenuStore(JFrame jframe) {
        this.jframe = jframe;
        MenuItemStore itemStore = new MenuItemStore(jframe);
    }
    private JMenu createLookParagraph() {
        return JMenuFactory.createInstance(bundle.getString("lookParagraph"), "Управление режимом отображения приложения", MenuItemStore.getLookParagraphItems());
    }

    private JMenu createTestParagraph() {
        return JMenuFactory.createInstance("Тесты", "Тестовые команды", MenuItemStore.getTestParagraphItems());
    }

    private JMenu createClosingParagraph() {
        return JMenuFactory.createInstance("Опции", "Опциональные команды", MenuItemStore.getOptionParagraphItems());
    }

    public JMenu[] getAllMenus() {
        return new JMenu[]{createLookParagraph(), createTestParagraph(), createClosingParagraph()};
    }
}