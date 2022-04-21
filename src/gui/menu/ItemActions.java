package gui.menu;

import javax.swing.*;

public class ItemActions extends IMenu {
    @Override
    public void appendToMenu(JMenu menu, JMenuItem... menuItems) {
        for (var item :
                menuItems) {
            menu.add(item);
        }
    }

    @Override
    public void appendToBar(JMenuBar bar, JMenu... menus) {
        for (var menu :
                menus) {
            bar.add(menu);
        }
    }
}
