package model;

import javax.swing.*;
import java.awt.*;

public class BossBullet {

    private double x, y;
    private double vx, vy;
    private int width;
    private int height;
    private Image image;

    public BossBullet(double x, double y, double angleDegrees, double speed) {
        this.x = x;
        this.y = y;
        width = 25;
        height = 40;
        double rad = Math.toRadians(angleDegrees);//تبدیل درجه به رادیان
        this.vx = Math.cos(rad) * speed;//سرعت افقی
        this.vy = Math.sin(rad) * speed;//سرعت عمودی
        image = new ImageIcon("src/resources/images/EnemyBullet.png").getImage();

    }

    public void move() {
        x += vx;
        y += vy;
    }

    public boolean isOutOfScreen(int screenWidth, int screenHeight) {
        return x < -width || x > screenWidth + width
                || y < -height || y > screenHeight + height;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }


    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        double centerX = x + width / 2.0;
        double centerY = y + height / 2.0;

        //عکس تیر به اندازه جت حرکت میچرخه
        double angle = Math.atan2(vy, vx) - Math.PI / 2;

        g2.rotate(angle, centerX, centerY);
        g2.drawImage(image, (int) x, (int) y, width, height, null);

        g2.dispose();
    }


}
