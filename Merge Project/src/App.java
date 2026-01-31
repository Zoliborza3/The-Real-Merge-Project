import javax.swing.JFrame;

import java.awt.Color;

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
