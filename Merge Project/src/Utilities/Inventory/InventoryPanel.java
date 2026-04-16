package Utilities.Inventory;

import Utilities.Game;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class InventoryPanel extends JPanel {
    LinkedList<Element> inventory;

    public InventoryPanel(LinkedList<Element> inventory) {
        this.inventory = inventory;
        setPreferredSize(new Dimension(8 * Game.getWindowScale(), 36 * Game.getWindowScale()));
        setBackground(Color.orange);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!inventory.isEmpty()) {

            int itemSlotAmount = inventory.size();
            int itemSlotHeight = getHeight() / 10;
            int width = getWidth();

            g.setColor(Color.BLACK);

            for (int i = 0; i < itemSlotAmount; i++) {

                int y = i * itemSlotHeight;

                g.drawRect(0, y, width, itemSlotHeight);

                if (i < inventory.size()) {
                    Element e = inventory.get(i);
                    g.drawString(e.id + " - " + e.amount, inventory.size(), y + itemSlotHeight / 2);
                }
            }
        }
    }
}

