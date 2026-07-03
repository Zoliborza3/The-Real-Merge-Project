package Utilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
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
import Utilities.Elements.Element;
import Utilities.Entities.InventoryPanel;
import Utilities.Entities.Player;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Game extends JPanel {

    //defines the base layers

    //use this variable to set the size of window
    static int windowScale = 20;
    public double frameRate;

    //contains all the instances in the game
    public static HashMap<String, Object> instance = new HashMap<>();

    //contains all the used up ids
    public static LinkedList<String> expandedKeys = new LinkedList<>();

    //contains all the loaded sprites
    public static HashMap<String, Sprite> sprite = new HashMap<>();

    public static HashMap<String, BufferedImage> image = new HashMap<>();

    //Just the camera object
    public static Camera camera = new Camera(new Point(0, 0), 2560, 1440, "");

    //The inputs on the current frame
    public static KeyHandler inputHandler = new KeyHandler();

    //The inputs of the previous frame
    public static KeyHandler impulseHandler = new KeyHandler();

    //The state of the Mouse on the current frame
    public static MouseHandler inputMouse = new MouseHandler();

    //The state of the Mouse on the previous frame
    public static MouseHandler impulseMouse = new MouseHandler();

    public Object player;

    InventoryPanel inventoryPanel = new InventoryPanel();

    public Game(double frameRate) {
        setPreferredSize(new Dimension(64*windowScale, 36*windowScale));
        setFocusable(true);
        addKeyListener(inputHandler);
        addMouseListener(inputMouse);

        this.frameRate = frameRate;
    }

    public void run(long currentFrame, int windowX, int windowY, boolean borderless) {
        //updates mouse position
        double scale = 40/windowScale;
        inputMouse.onScreen.x = (int)(MouseInfo.getPointerInfo().getLocation().x-windowX)-8;
        if (borderless) {inputMouse.onScreen.y =  (int)(MouseInfo.getPointerInfo().getLocation().y-windowY);}
        else {inputMouse.onScreen.y =  (int)(MouseInfo.getPointerInfo().getLocation().y-windowY)-32;}
        if (inputMouse.onScreen.x < 0) {inputMouse.inGame.x = Double.NEGATIVE_INFINITY;}
        else if (inputMouse.onScreen.x > getWidth()*scale) {inputMouse.inGame.x = Double.POSITIVE_INFINITY;}
        else {inputMouse.inGame.x = (int)(camera.position.x + inputMouse.onScreen.x)*scale;}
        if (inputMouse.onScreen.y < 0) {inputMouse.inGame.y = Double.NEGATIVE_INFINITY;}
        else if (inputMouse.onScreen.y > getHeight()*scale) {inputMouse.inGame.y = Double.POSITIVE_INFINITY;}
        else {inputMouse.inGame.y = (int)(camera.position.y + inputMouse.onScreen.y)*scale;}

        //updates the inputMouse's hover over lists
        inputMouse.hoverOver = new HashMap<Integer, HashMap<Integer, LinkedList<String>>>();
        for (String key : instance.keySet()) {
            Object object = instance.get(key);

            if (inputMouse.mask.collidesAt(inputMouse.inGame, object.getMask(), object.getAbsolutePosition())) {
                
                if (inputMouse.hoverOver.containsKey(object.layer)) {
                    if (inputMouse.hoverOver.get(object.layer).containsKey(object.getAbsoluteDepth())) {
                        inputMouse.hoverOver.get(object.layer).get(object.getAbsoluteDepth()).add(key);
                    }
                    else {
                        inputMouse.hoverOver.get(object.layer).put(object.getAbsoluteDepth(), new LinkedList<String>());
                        inputMouse.hoverOver.get(object.layer).get(object.getAbsoluteDepth()).add(key);
                    }
                }
                else {
                    inputMouse.hoverOver.put(object.layer, new HashMap<Integer, LinkedList<String>>());
                    inputMouse.hoverOver.get(object.layer).put(object.getAbsoluteDepth(), new LinkedList<String>());
                    inputMouse.hoverOver.get(object.layer).get(object.getAbsoluteDepth()).add(key);
                }

            }
        }

        //updates the list of the taken identifier keys
        for (String string : instance.keySet()) {
            if (!expandedKeys.contains(string)) expandedKeys.add(string);
        }

        //updates the sprite states of every object
        for (int i = 0; i < instance.size(); i ++) {
            Object thisinstance = instance.get(instance.keySet().toArray()[i]);
            Sprite thisSprite = sprite.get(thisinstance.getSpriteIndex());
            if (thisSprite != null) {

                long numberOfFrames = (long)(thisSprite.imageTime*thisinstance.imageSpeed*((double)frameRate));

                if (numberOfFrames > 0) {
                    while (currentFrame >= thisinstance.previousUpdate+numberOfFrames) {
                        thisinstance.imageIndex ++;
                        if (thisinstance.imageIndex >= thisSprite.size()) thisinstance.imageIndex = 0;
                        thisinstance.previousUpdate += numberOfFrames;
                    }
                }
            }
        }

        step(currentFrame);

        repaint();

        impulseHandler = inputHandler.copy();
        impulseMouse = inputMouse.copy();
    }

    //this is where the real game stuff should go; feel free to break it up more if you all prefer to; I would advise against writing extensive amounts of codes here, rather both of you should make Entities for that
    public void step(long currentFrame) {

        act();

        Element e = new Element(1,1);
        Element e2 = new Element(2,1);

        if (inputHandler.key(KeyEvent.VK_I) && !impulseHandler.key(KeyEvent.VK_I)) {inventoryPanel.drawTabInventory(this,currentFrame);}
        if (inputHandler.key(KeyEvent.VK_E) && !impulseHandler.key(KeyEvent.VK_E)) {inventoryPanel.inventory.addElement(e);}
        if (inputHandler.key(KeyEvent.VK_Q) && !impulseHandler.key(KeyEvent.VK_Q)) {inventoryPanel.inventory.addElement(e2);}

        //example code for checking inputs
        //press
        if (inputHandler.key(KeyEvent.VK_SPACE) && !impulseHandler.key(KeyEvent.VK_SPACE)) {System.out.println("press");}
        //hold
        if (inputHandler.key(KeyEvent.VK_SPACE)) {System.out.println("hold");}
        //release
        if (!inputHandler.key(KeyEvent.VK_SPACE) && impulseHandler.key(KeyEvent.VK_SPACE)) {System.out.println("release");}


        //System.out.println(image.keySet());

    }

    //runs once on the first frame
    public void create(long currentFrame) {

        try {resource("resources");} catch (Exception e) {System.err.println("Resource reading Exception: "+e);}

        //example code for creating an instance, attaching an entity and parenting another instance
        //this how you create an instance
        String key = generateIdentifier();
        instance.put(key, new Object(0, new Point(100, 100), "sprTest", sprite.get("sprTest").getMask(), key, currentFrame));
        
        //this how you can save to a variable and add an entity to them
        player = instance.get(key);
        player.entity = new Player();

        //this how you make another instance and add it to the collection of the first one
        key = generateIdentifier();
        instance.put(key, new Object(-10, new Point(200, 100), "sprTest", sprite.get("sprTest").getMask(), key, currentFrame));
        player.addToCollection(key);

        BufferedImage img = new BufferedImage((int)camera.width, (int)camera.height, BufferedImage.BITMASK);

        Graphics g = img.getGraphics();

        g.setColor(Color.GRAY);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());

        g.setColor(Color.WHITE);
        for (int i = 0; i < 10; i ++) {
            g.fillRect(100, i*(int)((double)img.getHeight()*0.075), 900, i*(int)((double)img.getHeight()*0.05));
        }

        g.dispose();

        Sprite toAdd = new Sprite(new BufferedImage[] {img}, new Circle(100), new Point(10, 10));

        sprite.put("sprMenuComplete", toAdd);

        //thomas
        //key = "Thomas";
        //instance.put(key, new Object(Object.HUD, new Point(0, 0), "sprMenuComplete", sprite.get("sprMenuComplete").getMask(), key, currentFrame));
        inventoryPanel.draw(this,currentFrame);
    }



    public void paintComponent(Graphics g) {

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());


        double downScale = (1.0*windowScale/40);

        //categorizes each object into their respective layer and depth level; -99 to 99
        HashMap<Integer, HashMap<Integer, LinkedList<String>>> layerOrder = new HashMap<>(); 

        String instanceKeyset[] = instance.keySet().toString().split(", ");

        if (instanceKeyset.length > 0) {instanceKeyset[0] = instanceKeyset[0].replace("[", ""); instanceKeyset[instanceKeyset.length-1] = instanceKeyset[instanceKeyset.length-1].replace("]", "");}

        for (int i = 0; i < instance.size(); i ++) {

            Object ins = instance.get(instanceKeyset[i]);
            if (ins != null && ins.getAbsoluteVisibility() && sprite.containsKey(ins.getSpriteIndex())) {

                Sprite spr = sprite.get(ins.getSpriteIndex());
                Image img = spr.getImage(ins.imageIndex);
                double realWidth = img.getWidth(null) * ins.imageXScale * downScale;
                double realHeight = img.getHeight(null) * ins.imageYScale * downScale;
                Point realOrigin = new Point((int)(spr.origin.x * ins.imageXScale * downScale), (int)(spr.origin.y * ins.imageYScale * downScale));
                double realRightBound = realWidth - realOrigin.x - realOrigin.x + ins.getAbsolutePosition().x;
                double realLeftBound = realWidth - realRightBound;
                double realBottomBound = realHeight - realOrigin.y - realOrigin.y + ins.getAbsolutePosition().y;
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
                ) {

                    if (layerOrder.containsKey(ins.layer)) {
                        layerOrder.get(ins.layer).get(ins.getAbsoluteDepth()).add(instanceKeyset[i]);
                    }
                    else {
                        layerOrder.put(ins.layer, new HashMap<Integer, LinkedList<String>>());
                        layerOrder.get(ins.layer).put(ins.getAbsoluteDepth(), new LinkedList<String>());
                        layerOrder.get(ins.layer).get(ins.getAbsoluteDepth()).add(instanceKeyset[i]);
                    }

                }

            }
        }

        g.setColor(Color.WHITE);

        //draws every instance or every collision box
        for (int layer : layerOrder.keySet()) {
            for (int i : layerOrder.get(layer).keySet()) {
                if (layerOrder.get(i) != null) {
                    LinkedList<String> depthLevel = layerOrder.get(layer).get(i);

                    for (int j = 0; j < depthLevel.size(); j ++) {
                        //calls the image we are about to draw
                        Object currentinstance = instance.get(depthLevel.get(j));
                        Sprite currentSprite = sprite.get(currentinstance.getSpriteIndex());
                        Image imageToDraw = currentSprite.getImage(currentinstance.imageIndex);

                        Integer scaledWidth = (int)(Math.round(imageToDraw.getWidth(null) * currentinstance.imageXScale * downScale));
                        Integer scaledHeight = (int)(Math.round(imageToDraw.getHeight(null) * currentinstance.imageYScale * downScale));
                        Point realOrigin = new Point((int)(currentSprite.origin.x * currentinstance.imageXScale * downScale), (int)(currentSprite.origin.y * currentinstance.imageYScale * downScale));

                        if (scaledHeight != 0 && scaledWidth != 0) {
                            imageToDraw = imageToDraw.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_FAST);
                            g.drawImage(imageToDraw, (int)Math.round((currentinstance.getAbsolutePosition().x - camera.position.x) * downScale - realOrigin.x), (int)Math.round((currentinstance.getAbsolutePosition().y - camera.position.y) * downScale - realOrigin.y), imageToDraw.getWidth(null), imageToDraw.getHeight(null), null);
                        }

                    }
                }
            }
        }

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

        try {
            File sourceFolder = new File(rootDirectory+"/images");
            if (!sourceFolder.exists() || !sourceFolder.canRead()) throw new FileNotFoundException("The directory doesn't exits or cannot be read: "+sourceFolder);
            
            String assets[] = sourceFolder.list();
            for (String assetName : assets) {
                File file = new File(sourceFolder+"/"+assetName);
                if (!sourceFolder.canRead()) throw new FileNotFoundException("The file cannot be read: "+file);

                try {image.put(assetName.replace(".png", ""), ImageIO.read(file));}
                catch (IOException e) {throw new IOException(e+": "+file);}
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
                object.entity.act(this, object);
            }
        }

        camera.act(this);
    }
    public static int getWindowScale(){
        return windowScale;
    }

}
