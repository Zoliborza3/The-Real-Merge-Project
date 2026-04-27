package Utilities;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import Utilities.Collision.Dot;

public class MouseHandler implements MouseListener {

    //onscreen is the mouse's position relative to the upperleft corner of the game window; ingame is the ingame coordinates of where the mouse falls
    Point onScreen = new Point(0, 0), inGame = new Point(0, 0);
    //list of keys of the objects that the mouse is hovered over sorted by layer and depth
    HashMap<Integer, HashMap<Integer, LinkedList<String>>> hoverOver = new HashMap<>();

    //Collision box of the mousePointer
    Dot mask = new Dot();

    Map<Integer, Boolean> keys = new HashMap<>();

    public boolean key(Integer code) {

        if (keys.containsKey(code)) {return keys.get(code);}
        else {
            keys.put(code, false);
            return false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Integer code = e.getButton();

        if (keys.containsKey(code)) {keys.remove(code); keys.put(code, true);}
        else {keys.put(code, true);}
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int code = e.getButton();

        if (keys.containsKey(code)) {keys.remove(code); keys.put(code, false);}
        else {keys.put(code, false);}
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    public MouseHandler copy() {
        MouseHandler copy = new MouseHandler();
        for (Integer code : this.keys.keySet()) {
            copy.keys.put(code, this.key(code));
        }
        copy.onScreen = this.onScreen;
        copy.inGame = this.inGame;
        return copy;
    }

}
