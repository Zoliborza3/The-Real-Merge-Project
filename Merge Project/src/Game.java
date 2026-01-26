import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import Utilities.*;
import Utilities.Object;

import javax.swing.JPanel;

public class Game extends JPanel {
    int windowScale = 20;

    //contains all the instances in the game
    HashMap<String, Object> instance = new HashMap<>();

    //contains all the used up ids
    LinkedList<String> expandedKeys = new LinkedList<>();

    //contains all the loaded sprites
    HashMap<String, Sprite> sprite = new HashMap<>();

    //Just the camera object
    Camera camera = new Camera(new Point(0, 0), 2560, 1440, "");

    //The inputs on the current frame
    KeyHandler inputHandler = new KeyHandler();

    //The inputs of the previous frame
    KeyHandler impulseHandler = inputHandler.copy();

    public Game() {
        addKeyListener(inputHandler);
    }

    public void run() {

        if (inputHandler.key(KeyEvent.VK_F2) && !impulseHandler.key(KeyEvent.VK_F2)) {
            if (windowScale == 20) {windowScale = 40;} else {windowScale = 20;}
        }

        if (inputHandler.key(KeyEvent.VK_F2)) {System.out.println("yes");}

        repaint();

        impulseHandler = inputHandler.copy();
    }

    public void create() {
        BufferedImage test = new BufferedImage(100, 100, BufferedImage.BITMASK);
        Graphics draw = test.getGraphics();
        draw.setColor(Color.WHITE);
        draw.fillRect(0, 0, 100, 100);
        draw.dispose();
        sprite.put("test", new Sprite(new BufferedImage[] {test}, new Collision(), new Point(0, 0)));

        String key = generateIdentifier();
        instance.put(key, new Object(new Point(0, 0), "test", key));
    }

    public void paintComponent(Graphics g) {

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        double downScale = (1.0*windowScale/40);

        //categorizes each object into their respective depth level; -99 to 99
        LinkedList<String> depthOrder[] = new LinkedList[199];
        for (int i = 0; i < depthOrder.length; i ++) {
            depthOrder[i] = new LinkedList<String>();
        }

        String instanceKeyset[] = instance.keySet().toString().split(", ");

        if (instanceKeyset.length > 0) {instanceKeyset[0] = instanceKeyset[0].replace("[", ""); instanceKeyset[instanceKeyset.length-1] = instanceKeyset[instanceKeyset.length-1].replace("]", "");}

        for (int i = 0; i < instance.size(); i ++) {

            Object ins = instance.get(instanceKeyset[i]);
            if (ins != null && ins.visible && sprite.containsKey(ins.spriteIndex)) {

                Sprite spr = sprite.get(ins.spriteIndex);
                Image img = spr.getImage(ins.imageIndex);
                double realWidth = img.getWidth(null) * ins.imageXScale * downScale;
                double realHeight = img.getHeight(null) * ins.imageYScale * downScale;
                Point realOrigin = new Point((int)(spr.origin.x * ins.imageXScale * downScale), (int)(spr.origin.y * ins.imageYScale * downScale));
                double realRightBound = realWidth - realOrigin.x - realOrigin.x + ins.position.x;
                double realLeftBound = realWidth - realRightBound;
                double realBottomBound = realHeight - realOrigin.y - realOrigin.y + ins.position.y;
                double realTopBound = realHeight - realBottomBound;

                if (
                    (
                        (realRightBound <= camera.rightBound() && camera.leftBound() <= realRightBound)
                        ||
                        (realLeftBound <= camera.rightBound() && camera.leftBound() <= realLeftBound)
                        ||
                        (realLeftBound <= camera.leftBound() && realRightBound >= camera.rightBound())
                    ) 
                    &
                    (
                        (realBottomBound <= camera.bottomBound() && camera.topBound() <= realBottomBound)
                        ||
                        (realTopBound <= camera.bottomBound() && camera.topBound() <= realTopBound)
                        ||
                        (realTopBound <= camera.topBound() && camera.bottomBound() >= realBottomBound)
                    )
                ) {depthOrder[instance.get(instanceKeyset[i]).depth+99].add(instanceKeyset[i]);}

            }
        }

        g.setColor(Color.WHITE);

        //draws every instance or every collision box
        if (!inputHandler.key(KeyEvent.VK_F1)) {
            for (int i = 198; i >= 0; i --) {
                LinkedList<String> depthLevel = depthOrder[i];

                for (int j = 0; j < depthLevel.size(); j ++) {
                    Object currentInstance = instance.get(depthLevel.get(j));

                    Sprite currentSprite = sprite.get(currentInstance.spriteIndex);

                    Image imageToDraw = currentSprite.getImage(currentInstance.imageIndex);

                    Integer scaledWidth = (int)(Math.round(imageToDraw.getWidth(null) * currentInstance.imageXScale * downScale));
                    Integer scaledHeight = (int)(Math.round(imageToDraw.getHeight(null) * currentInstance.imageYScale * downScale));
                    Point realOrigin = new Point((int)(currentSprite.origin.x * currentInstance.imageXScale * downScale), (int)(currentSprite.origin.y * currentInstance.imageYScale * downScale));

                    if (scaledHeight != 0 && scaledWidth != 0) {
                        imageToDraw = imageToDraw.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_FAST);

                        g.drawImage(imageToDraw, (int)Math.round((currentInstance.position.x - camera.position.x) * downScale - realOrigin.x), (int)Math.round((currentInstance.position.y - camera.position.y) * downScale - realOrigin.y), imageToDraw.getWidth(null), imageToDraw.getHeight(null), null);
                    }

                }
            }
        }
        else {
            /*
            g.setColor(Color.RED);
            for (int i = 198; i >= 0; i --) {
                LinkedList<String> depthLevel = depthOrder[i];

                for (int j = 0; j < depthLevel.size(); j ++) {
                    Object currentInstance = instance.get(depthLevel.get(j));
                    Collision current = currentInstance.mask();

                    Point[] currentMask = current.points;

                    int xPoints[] = new int[currentMask.length];
                    int yPoints[] = new int[currentMask.length];

                    Point newPoints[] = new Point[currentMask.length];

                    for (int x = 0; x < currentMask.length; x ++) {
                        Point point = currentMask[x];

                        xPoints[x] = (int)Math.round((currentInstance.position.x + point.x - camera.position.x) * downScale);
                        yPoints[x] = (int)Math.round((currentInstance.position.y + point.y - camera.position.y) * downScale);
                        newPoints[x] = new Point(xPoints[x], yPoints[x]);
                    }

                    g.fillPolygon(xPoints, yPoints, currentMask.length);

                }
            }
            */
        }

    }

    //generates a unique identifier
    public String generateIdentifier() {
        Random random = new Random();

        String key = "" + (System.currentTimeMillis()%2) + Math.abs(random.nextLong());

        while (expandedKeys.contains(key)) {key = "" + (System.currentTimeMillis()%2) + Math.abs(random.nextLong());}

        return key;

    }

}
