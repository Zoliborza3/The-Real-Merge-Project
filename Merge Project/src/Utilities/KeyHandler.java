package Utilities;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class KeyHandler implements KeyListener {

    Map<Integer, Boolean> keys = new HashMap<>();

    public boolean key(Integer code) {

        if (keys.containsKey(code)) {return keys.get(code);}
        else {
            keys.put(code, false);
            return false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {



    }

    @Override
    public void keyPressed(KeyEvent e) {
        Integer code = e.getKeyCode();

        if (keys.containsKey(code)) {keys.remove(code); keys.put(code, true);}
        else {keys.put(code, true);}
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (keys.containsKey(code)) {keys.remove(code); keys.put(code, false);}
        else {keys.put(code, false);}

    }

    public KeyHandler copy() {
        KeyHandler copy = new KeyHandler();
        for (Integer code : this.keys.keySet()) {
            copy.keys.put(code, this.key(code));
        }
        return copy;
    }

}
