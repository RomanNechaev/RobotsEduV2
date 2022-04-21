package gui.menu;

import javax.swing.*;

public abstract class IMenu {
    public abstract void appendToMenu(JMenu menu, JMenuItem... menuItems);

    public abstract void appendToBar(JMenuBar bar, JMenu... menus);
}
