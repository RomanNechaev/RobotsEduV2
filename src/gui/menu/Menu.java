package gui.menu;

import javax.swing.*;

public abstract class Menu extends IMenu {
    @Override
    public void appendToBar(JMenuBar bar, JMenu... menus) {
        for (var menu:
                menus) {
            bar.add(menu);
        }
    }
}
