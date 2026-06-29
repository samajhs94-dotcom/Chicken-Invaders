package model;

import javax.swing.*;
import java.awt.*;

public class Bullet {

    //موقعیت، سرعت، میزان آسیب و اندازه گلوله
    private int x;
    private int y;
    private int width;
    private int height;
    private int speed;
    private int damage;

    private Image image;

    public Bullet(int x,int y){

        this.x=x;
        this.y=y;
        width=10;
        height=20;
        speed=10;
        damage=1;
        image= new ImageIcon("src/resources/images/bullet.png").getImage();
    }

    //گترها
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDamage() {
        return damage;
    }

    //رسم گلوله
    public void draw(Graphics g){
        g.drawImage(image,x,y,width,height,null);
    }

    // حرکت به سمت بالا
    public void move() {
        y = y - speed;
    }

    // بررسی خروج از صفحه
    public boolean isOutOfScreen() {
        return y + height < 0;
    }

    // محدوده برخورد
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }


}
