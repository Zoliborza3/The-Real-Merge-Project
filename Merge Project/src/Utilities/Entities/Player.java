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

        object.setRelativePosition(null);

    }

}
