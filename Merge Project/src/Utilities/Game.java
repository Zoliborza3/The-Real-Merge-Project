package Utilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.RenderingHints.Key;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

import Utilities.Collision.*;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Game extends JPanel {
    //use this variable to set the size of window
    int windowScale = 10;
    public int frameRate;

    //contains all the instances in the game
    HashMap<String, Object> instance = new HashMap<>();

    //contains all the used up ids
    LinkedList<String> expandedKeys = new LinkedList<>();

    //contains all the loaded sprites
    HashMap<String, Sprite> sprite = new HashMap<>();

    //Just the camera object
    Camera camera = new Camera(new Point(0, 0), 2560, 1440, "");

    //The inputs on the current frame
    static KeyHandler inputHandler = new KeyHandler();

    //The inputs of the previous frame
    static KeyHandler impulseHandler = new KeyHandler();

    Object player;

    public Game(int frameRate) {
        setPreferredSize(new Dimension(64*windowScale, 36*windowScale));
        setFocusable(true);
        addKeyListener(inputHandler);

        this.frameRate = frameRate;
    }

    public void run(long currentFrame) {

        //updates the list of the taken identifier keys
        for (String string : instance.keySet()) {
            if (!expandedKeys.contains(string)) expandedKeys.add(string);
        }

        

        //updates the sprite states of every object
        for (int i = 0; i < instance.size(); i ++) {
            Object thisInstance = instance.get(instance.keySet().toArray()[i]);
            Sprite thisSprite = sprite.get(thisInstance.getSpriteIndex());
            if (thisSprite != null) {

                long numberOfFrames = (long)(thisSprite.imageTime*thisInstance.imageSpeed*((double)frameRate));

                if (numberOfFrames > 0) {
                    while (currentFrame >= thisInstance.previousUpdate+numberOfFrames) {
                        thisInstance.imageIndex ++;
                        if (thisInstance.imageIndex >= thisSprite.size()) thisInstance.imageIndex = 0;
                        thisInstance.previousUpdate += numberOfFrames;
                    }
                }
            }
        }

        step(currentFrame);

        repaint();

        impulseHandler = inputHandler.copy();
    }

    //this is where the real game stuff should go; feel free to break it up more if you all prefer to; I would advise against writing extensive amounts of codes here, rather both of you should make Entities for that
    public void step(long currentFrame) {

        act();

        //example code for checking inputs
        //press
        if (inputHandler.key(KeyEvent.VK_SPACE) && !impulseHandler.key(KeyEvent.VK_SPACE)) {System.out.println("press");}
        //hold
        if (inputHandler.key(KeyEvent.VK_SPACE)) {System.out.println("hold");}
        //release
        if (!inputHandler.key(KeyEvent.VK_SPACE) && impulseHandler.key(KeyEvent.VK_SPACE)) {System.out.println("release");}

    }

    //this is where you can call extra draw functions and it will all render on top of the game (it's better not to touch the real paintComponent); meant mainly for GUI as it draws on top of everything
    public void draw(Graphics g) {



    }

    //runs once on the first frame
    public void create(long currentFrame) {

        try {resource("Merge Project\\resources");} catch (Exception e) {System.err.println("Resource reading Exception: "+e);}

        //example code for creating an Instance
        String key = generateIdentifier();
        instance.put(key, new Object(new Point(100, 100), "sprTest", sprite.get("sprTest").getMask(), key, currentFrame));

        player = instance.get(key);
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
            if (ins != null && ins.visible && sprite.containsKey(ins.getSpriteIndex())) {

                Sprite spr = sprite.get(ins.getSpriteIndex());
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
                ) {depthOrder[instance.get(instanceKeyset[i]).getDepth()+99].add(instanceKeyset[i]);}

            }
        }

        g.setColor(Color.WHITE);

        //draws every instance or every collision box
        for (int i = 198; i >= 0; i --) {
            LinkedList<String> depthLevel = depthOrder[i];

            for (int j = 0; j < depthLevel.size(); j ++) {
                //calls the image we are about to draw
                Object currentInstance = instance.get(depthLevel.get(j));
                Sprite currentSprite = sprite.get(currentInstance.getSpriteIndex());
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

        draw(g);

    }

    //generates a unique identifier
    public String generateIdentifier() {
        Random random = new Random();

        String key = "" + (System.currentTimeMillis()%2) + Math.abs(random.nextLong());

        while (expandedKeys.contains(key)) {key = "" + (System.currentTimeMillis()%2) + Math.abs(random.nextLong());}

        return key;

    }

    //it reads the assets the game uses (for now only sprites)
    public void resource(String rootDirectory) throws Exception {

        try {
            File sourceFolder = new File(rootDirectory+"/sprites");
            if (!sourceFolder.exists() || !sourceFolder.canRead()) throw new FileNotFoundException("The directory doesn't exits or cannot be read: "+sourceFolder);
            
            String assets[] = sourceFolder.list();
            for (String assetName : assets) {
                File assetFolder = new File(sourceFolder+"/"+assetName);
                if (!sourceFolder.canRead()) throw new FileNotFoundException("The asset folder cannot be read: "+assetFolder);

                String elements[] = assetFolder.list();
                Point origin;
                Collision mask;
                BufferedImage image[] = new BufferedImage[elements.length-1];
                double span = 1;

                File attributeFile = new File(assetFolder+"/att.txt");
                if (!attributeFile.exists() || !attributeFile.canRead()) throw new FileNotFoundException("The attribute file doesn't exists or cannot be read: "+attributeFile);
                Scanner file = new Scanner(attributeFile);
                
                
                if (file.hasNextLine()) {
                    //gets the coordinates of the origin of this sprite
                    String line[] = file.nextLine().split(", ");
                    origin = new Point(Integer.parseInt(line[0]), Integer.parseInt(line[1]));

                    if (file.hasNextLine()) {
                        String pointStrings[] = file.nextLine().split("; ");

                        Point points[] = new Point[pointStrings.length];

                        for (int i = 0; i < pointStrings.length; i ++) {
                            String segments[] = pointStrings[i].split(", ");
                            points[i] = new Point(Integer.parseInt(segments[0]), Integer.parseInt(segments[1]));
                        }

                        if (points.length == 1) {mask = new Circle(points[0].distanceTo(new Point(0, 0)));}
                        else {mask = new Polygon(points);}

                        if (file.hasNextLine()) {span = file.nextDouble();}
                    } else {file.close(); throw new Exception("Attribute file isn't structured properly: "+attributeFile);}
                } else {file.close(); throw new Exception("Attribute file isn't structured properly: "+attributeFile);}
                file.close();

                //reads the images of the sprite
                for (int i = 0; i < image.length; i ++) {
                    File imageToRead = new File(assetFolder+"/"+i+".png");
                    if (!imageToRead.exists()) throw new FileNotFoundException("Image doesn't exists: "+imageToRead);
                    if (!imageToRead.canRead()) throw new Exception("Image cannot be read"+imageToRead);

                    try {image[i] = ImageIO.read(imageToRead);} catch (IOException e) {throw new IOException(e+": "+imageToRead);}
                }

                sprite.put(assetName, new Sprite(image, mask, origin, span));

            }

        } catch (FileNotFoundException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        } catch (Exception e) {
            System.err.println("Undefined problem while trying to read assets: "+e);
        }

    }

    //calls the innate logic of entities
    public void act() {
        for (String key : instance.keySet()) {
            Object object = instance.get(key);

            if (object.entity != null) {
                object.entity.act(this);
            }
        }
    }

}
