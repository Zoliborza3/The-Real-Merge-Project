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
        double frameRate = 120;
        double frameLength = 1000000000/frameRate;
        long frameCounter = 0;

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.BLACK);

        Game game = new Game(frameRate);

        frame.add(game);

        frame.setResizable(false);

        frame.pack();
        frame.setVisible(true);

        game.create(frameCounter);

        while (true) {
            while (frameStart + frameLength < System.nanoTime()) {
                game.run(frameCounter);
                frameStart += frameLength;
                frameCounter ++;
            }
        }
    }
}
