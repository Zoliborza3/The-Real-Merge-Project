package Utilities.Entities;

import java.awt.event.KeyEvent;

import Utilities.*;
import Utilities.Object;

public class Player extends Entity {

    public void act(Game game, Object object) {
        
        Vector move = new Vector(0, 0);
        if (Game.inputHandler.key(KeyEvent.VK_RIGHT)) {move.x ++;}
        if (Game.inputHandler.key(KeyEvent.VK_LEFT)) {move.x --;}
        if (Game.inputHandler.key(KeyEvent.VK_UP)) {move.y --;}
        if (Game.inputHandler.key(KeyEvent.VK_DOWN)) {move.y ++;}
        move = move.thisVectorWithLength(3);

        if (move.length() > 0) object.setRelativePosition(object.getRelativePosition().applyVector(move));


        //how to use the wheel rotation
        object.setRelativePosition(object.getRelativePosition().applyVector(new Vector(0, MouseHandler.rotation*5)));

    }

}
