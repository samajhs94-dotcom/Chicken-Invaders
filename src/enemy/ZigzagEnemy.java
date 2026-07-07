package enemy;

import javax.swing.*;
import java.awt.*;

public class ZigzagEnemy extends Enemy{

    private double replacementAngle = 0;

    private int eggZigzagFrames = 0;
    private double eggZigzagAngle = 0;

    private double replacementOffsetX = 0;
    private double replacementOffsetY = 0;

    private double eggOffsetX = 0;
    private double eggOffsetY = 0;

    public ZigzagEnemy(int x, int y,int health) {
        super(x, y, 1.5, health, new ImageIcon("src/resources/images/ZigzagEnemy.png").getImage());
    }

    @Override
    public void moveToTarget() {

        if (hasReachedTarget()) {
            replacementOffsetX = 0;
            replacementOffsetY = 0;
            return;
        }

        replacementAngle += 0.22;

        // حرکت اصلی به سمت خونه
        super.moveToTarget();

        // زیگزاگ فقط افکت تصویریه نه تغییر جایگاه اصلی
        if (!hasReachedTarget()) {
            replacementOffsetX = Math.sin(replacementAngle) * 18;
            replacementOffsetY = Math.cos(replacementAngle * 0.8) * 5;
        } else {
            replacementOffsetX = 0;
            replacementOffsetY = 0;
        }
    }

    //وقتی صدا زده میشه که دشمن تخم انداخته باشه
    public void startEggZigzag() {
        eggZigzagFrames = 45;
        eggZigzagAngle = 0;
    }

    //  زیگزاگ هنگام تخم‌اندازی
    public void updateEggZigzag() {

        //اگر مرغ هنوز دارد جایگزین می‌شود تخم اندازی اجرا نمیشه تا جایگزینی خراب نشه
        if (!hasReachedTarget()) {
            return;
        }

        if (eggZigzagFrames <= 0) {
            eggOffsetX = 0;
            eggOffsetY = 0;
            return;
        }

        eggZigzagAngle += 0.45;

        eggOffsetX = Math.sin(eggZigzagAngle) * 20;
        eggOffsetY = Math.cos(eggZigzagAngle * 0.8) * 4;

        eggZigzagFrames--;

        if (eggZigzagFrames <= 0) {
            eggOffsetX = 0;
            eggOffsetY = 0;
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, getX(), getY(), width, height, null);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), width, height);
    }

    @Override
    public int getX() {
        return (int) Math.round(x + replacementOffsetX + eggOffsetX);
    }

    @Override
    public int getY() {
        return (int) Math.round(y + replacementOffsetY + eggOffsetY);
    }

}
