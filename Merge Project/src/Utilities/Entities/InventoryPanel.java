package Utilities.Entities;

import Utilities.Game;
import Utilities.Inventory;
import Utilities.Elements.Element;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class InventoryPanel extends Entity {

    public Inventory inventory;
    private Color backgroundColor;
    private Dimension preferredSize;

    public InventoryPanel() {
        this.inventory = new Inventory(1000);
        preferredSize = new Dimension(8 * Game.getWindowScale(), 36 * Game.getWindowScale());
        backgroundColor = Color.orange;
    }

    public void act(Game game) {

        if (!Game.sprite.containsKey("sprInventoryPanel")) {
            
        }

    }

    public void draw(Game game, Graphics g) {
        g.setColor(backgroundColor);
        g.fillRect((int)(game.getWidth()*0.8), 0, game.getWidth(), game.getHeight());

        if (!inventory.isEmpty()) {

            g.setColor(Color.WHITE);

            for (double i = 0; i < inventory.elements.size() && i < 10; i++) {
                Element e = inventory.elements.get((int)i);
                g.drawString(e.id + " - " + e.amount, (int)(game.getWidth()*0.8), (int)(10+game.getHeight()*(i/10)));
            }
        }
    }
}

