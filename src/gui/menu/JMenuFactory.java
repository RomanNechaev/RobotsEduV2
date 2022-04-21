package gui.menu;

import javax.swing.*;

public class JMenuFactory {
    public static JMenu createInstance(String name, String description, JMenuItem[] items){
        ItemActions act = new ItemActions();
        {
            JMenu menu = new JMenu(name);
            menu.getAccessibleContext().setAccessibleDescription(description);
            act.appendToMenu(menu, items);
            return menu;
        }
    }
}
