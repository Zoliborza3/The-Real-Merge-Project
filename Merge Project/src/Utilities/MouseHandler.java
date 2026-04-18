package Utilities;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

public class MouseHandler implements MouseListener {

    int x, y;
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
        copy.x = this.x;
        copy.y = this.y;
        return copy;
    }

}
