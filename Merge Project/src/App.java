import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Dimension;

public class App {
    public static void main(String[] args) throws Exception {
        //Not a lot to explain here
        JFrame frame = new JFrame("Merge Project");

        frame.setPreferredSize(new Dimension(1280, 720));
        frame.setFocusable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.BLACK);
        frame.setResizable(false);

        Game game = new Game();

        frame.add(game);

        frame.pack();
        frame.setVisible(true);

        game.create();

        double frameStart = System.nanoTime();
        double frameLength = 1000000000/120;

        while (true) {
            while (frameStart + frameLength < System.nanoTime()) {
                game.run();
                frameStart += frameLength;
            }
        }
    }
}
