package model;

import javax.swing.*;
import java.awt.*;

public class EnemyBullet {

    private int x;
    private int y;
    private int width;
    private int height;
    private int dx;
    private int dy;

    int speed = 5;
    Image image;

    public EnemyBullet(int x, int y,int dx,int dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        height=35;
        width=30;
        image = new ImageIcon("src/resources/images/EnemyBullet.png").getImage();
    }

    public void move() {
        x += dx;
        y += dy;
    }


    public void draw(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        double centerX = x + width / 2.0;
        double centerY = y + height / 2.0;

        //محاسبه جهت حرکت و تنظیم گلوله متناسب با جهت
        double angle = Math.atan2(dy, dx) - Math.PI / 2;

        g2.rotate(angle, centerX, centerY);
        g2.drawImage(image, x, y, width, height, null);

        g2.dispose();
    }


    public boolean isOutOfScreen(int screenWidth, int screenHeight) {
        return x + width < 0
                || x > screenWidth
                || y + height < 0
                || y > screenHeight;
    }

    public Rectangle getBounds(){
        return new Rectangle(x,y,width,height);
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
}
