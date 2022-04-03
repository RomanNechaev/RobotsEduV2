package gui.menu;

import javax.swing.*;

public class MenuGenerator extends JMenuBar {
    public JFrame jframe;
    ItemActions act = new ItemActions();
    public MenuGenerator(JFrame jframe) {
        this.jframe = jframe;
        MenuStore ms = new MenuStore(jframe);
        act.appendToBar(this, ms.getAllMenus());
    }
}