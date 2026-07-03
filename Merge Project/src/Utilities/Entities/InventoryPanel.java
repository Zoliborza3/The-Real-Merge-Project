package Utilities.Entities;

import Utilities.Collision.Circle;
import Utilities.Game;
import Utilities.Inventory;
import Utilities.Elements.Element;
import Utilities.Object;
import Utilities.Point;
import Utilities.Sprite;

import java.awt.*;
import java.awt.image.BufferedImage;

import static Utilities.Game.*;


public class InventoryPanel extends Entity {

    public Inventory inventory;
    private Color backgroundColor;
    private Dimension preferredSize;
    private int gap;
    private boolean isInventoryOpen =false;

    public InventoryPanel() {
        this.inventory = new Inventory(1000);
        preferredSize = new Dimension(8 * Game.getWindowScale(), 36 * Game.getWindowScale());
        backgroundColor = Color.BLACK;
        gap=16;
    }

    public void act(Game game) {

        if (!sprite.containsKey("sprInventoryPanel")) {

        }

    }

    public void draw(Game game, long currentFrame) {
        BufferedImage img = new BufferedImage((int)camera.width, (int)camera.height, BufferedImage.BITMASK);
        Graphics g = img.getGraphics();
        g.setColor(backgroundColor);
        g.fillRect((int)(game.camera.width*0.8), 0,(int) game.camera.width, (int) game.camera.height);
        for (int i = 0; i < 11; i++) {
            g.setColor(Color.DARK_GRAY);
            if (i>=10){
                g.setColor(Color.RED);
            }
            g.fillRect((int)(game.camera.width*0.875), (int)(gap+(game.camera.height/11)*i), (int)(game.camera.width*0.1), (int)((game.camera.height)/11)-gap);
        }
        if (!inventory.isEmpty()) {

            g.setColor(Color.WHITE);

            for (double i = 0; i < inventory.elements.size() && i < 10; i++) {
                Element e = inventory.elements.get((int)i);
                g.drawString(e.id + " - " + e.amount, (int)(game.camera.width*0.875+game.camera.width*0.05-g.getFontMetrics().stringWidth(e.id + " - " + e.amount)/2), 10+(int)(game.camera.height*(i/11)+(game.camera.height/11-gap)/2+gap-g.getFont().getSize()/2));
            }
        }
        g.dispose();

        Sprite sprInventoryPanel = new Sprite(new BufferedImage[] {img}, new Circle(100), new Point(10, 10));

        sprite.put("sprInventoryPanel", sprInventoryPanel);
        String key = "inventoryPanel";
        instance.put(key, new Utilities.Object(Object.HUD, new Point(0, 0), "sprInventoryPanel", sprite.get("sprInventoryPanel").getMask(), key, currentFrame));
    }
    public void drawTabInventory(Game game,long currentFrame){
        if (!isInventoryOpen){
            isInventoryOpen=true;
            BufferedImage img = new BufferedImage((int)camera.width, (int)camera.height, BufferedImage.BITMASK);
            Graphics g = img.getGraphics();
            g.setColor(Color.blue);
            g.fillRect((int)(game.camera.width*0.2), (int) (game.camera.height*0.2), (int)(game.camera.width*0.6),(int)(game.camera.height*0.6));
            g.dispose();
            Sprite sprTabInventory = new Sprite(new BufferedImage[]{img},new Circle(1),new Point(10,10));
            sprite.put("sprTabInventory",sprTabInventory);
            String key = "tabInventory";
            instance.put(key, new Utilities.Object(Object.HUD, new Point(0, 0), "sprTabInventory", sprite.get("sprTabInventory").getMask(), key, currentFrame));
        }
        else {
            instance.remove("tabInventory");
            isInventoryOpen=false;
            instance.get("cigány").containsKey("ákos");
        }
    }
}

