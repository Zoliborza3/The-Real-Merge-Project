import javax.swing.*;

import Utilities.Game;
import Utilities.Inventory.Inventory;
import Utilities.Inventory.InventoryPanel;

import java.awt.*;

public class App {
    public static void main(String[] args) throws Exception {
        //Not a lot to explain here
        JFrame frame = new JFrame("Merge Project");
        double frameStart = System.nanoTime();
        int frameRate = 120;
        double frameLength = 1000000000/(double)frameRate;
        long frameCounter = 0;

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.BLACK);
        frame.setLayout(new BorderLayout());

        Game game = new Game(frameRate);
        InventoryPanel inventoryPanel = new InventoryPanel(Inventory.inverntory);


        frame.add(game, BorderLayout.CENTER);
        frame.add(inventoryPanel, BorderLayout.EAST);

        frame.setResizable(false);

        frame.pack();
        frame.setVisible(true);

        game.create(frameCounter);

        while (true) {
            while (frameStart + frameLength < System.nanoTime()) {
                game.run(frameCounter);
                inventoryPanel.repaint();
                frameStart += frameLength;
                frameCounter ++;
            }
        }
    }
}
