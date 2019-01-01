package cn.itheima.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InitListener implements ActionListener {
    
    private JFrame jFrame;
    private Rectangle rect;
    
    private int left;
    
    private int right;
   
    private int screenXX;
    private int top;
    private int width;
    private int height;
    private Point point;
    private int xx, yy;


    public InitListener(JFrame frame) {
        this.jFrame = frame;

    }

    
    public void actionPerformed(ActionEvent e) {
        left = jFrame.getLocationOnScreen().x;
        top = jFrame.getLocationOnScreen().y;
        width = jFrame.getWidth();
        height = jFrame.getHeight();
        screenXX = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
        right = screenXX - left - width;
        rect = new Rectangle(0, 0, width, height);
        point = jFrame.getMousePosition();
        if (left < 0 && isPtInRect(rect, point)) {
            jFrame.setLocation(0, top); 
        } else if (left > -5 && left < 10 && !(isPtInRect(rect, point))) {
            jFrame.setLocation(left - width + 1, top); 
        } else if ((top < 0 && left < 0) && isPtInRect(rect, point)) {
            jFrame.setLocation(0, 0);
        } else if ((top > -5 && top < 10) && (left > -5 && left < 10)
                && !(isPtInRect(rect, point))) {
            
            jFrame.setLocation(left - width + 1, 1);
        } else if ((top < 0) && isPtInRect(rect, point)) {
            jFrame.setLocation(left, 0);
        } else if (top > -5 && top < 10 && !(isPtInRect(rect, point))) {
            
            int n = 1 - height;
            jFrame.setLocation(left, 1 - height);
        } else if (right < 0 && isPtInRect(rect, point)) {
            jFrame.setLocation(screenXX - width + 1, top);
        } else if (right > -5 && right < 10 && !(isPtInRect(rect, point))) {
            jFrame.setLocation(screenXX - 1, top);
        } else if (right < 0 && top < 0 && isPtInRect(rect, point)) {
            jFrame.setLocation(screenXX - width + 1, 0);
        } else if ((right > -5 && right < 10) && (top > -5 && top < 10)
                && !(isPtInRect(rect, point))) {
            jFrame.setLocation(screenXX - 1, 1);
        }
    }

    public boolean isPtInRect(Rectangle rect, Point point) {
        if (rect != null && point != null) {
            int x0 = rect.x;
            int y0 = rect.y;
            int x1 = rect.width;
            int y1 = rect.height;
            int x = point.x;
            int y = point.y;
            return x >= x0 && x < x1 && y >= y0 && y < y1;
        }
        return false;
    }

}
