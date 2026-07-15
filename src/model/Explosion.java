package model;

import javax.swing.*;
import java.awt.*;

public class Explosion {

    private final int x;
    private final int y;

    private int age;//عمر انفجار
    private final int maxAge;

    private final int width;
    private final int height;

    private Image image;

    public Explosion(int x, int y) {
        this.x = x;
        this.y = y;
        this.age = 0;
        this.maxAge = 30;
        this.width = 50;
        this.height = 50;
        loadImage();
    }

    private void loadImage() {
        try {
            image = new ImageIcon("src/resources/images/explosion.png").getImage();
        } catch (Exception e) {
            System.out.println("Explosion image not loaded.");
            image = null;
        }
    }

    public void update() {
        age++;
    }

    public boolean isFinished() {
        return age >= maxAge;
    }

    public void draw(Graphics g) {

        if (image == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();

        float progress = (float) age / maxAge;//درصد عمر انفجار
        float alpha = 1.0f - progress;//میزان شفافیت عکس

        if (alpha < 0) {
            alpha = 0;
        }

        //تصویر انفجار روی جسم و با اندازه شفافیت مشخص شده
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        g2.drawImage(image, x - width / 2, y - height / 2,
                width, height, null);

        g2.dispose();

    }

}
