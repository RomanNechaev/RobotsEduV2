package gui.menu;

import javax.swing.*;
import java.awt.event.ActionListener;

public class JMenuItemFactory {
    public static JMenuItem createInstance(String name, ActionListener action){
        {
            JMenuItem item = new JMenuItem(name);
            item.addActionListener(action);
            return item;
        }
    }
}
