package model;

import main.GameMain;

import javax.swing.*;
import java.awt.*;

public class Plane {

    public static final int MAX_LIVES = 5;

    private int x;
    private int y;
    private int width;
    private int height;
    private int speed;
    private int lives;
    private int type;//این قسمت برای وقتیه که خواستم قسمت اختیاری پروژه رو بزنم
    private int bulletCount;
    private boolean doubleBullet;
    private boolean shield;
    private boolean rapidFire;
    private Image image;
    private int fireRate;


    public Plane(int x,int y,int type){

        this.x=x;
        this.y=y;
        width=75;
        height=75;

        this.type=type;
        bulletCount = 1;

        doubleBullet = false;
        shield = false;
        rapidFire = false;

        switch(type){
            case 1:
                speed = 5;
                lives = 3;
                fireRate = 300;
                image = new ImageIcon("src/resources/images/DefaultPlane.png").getImage();
                break;

            case 2:
                speed = 7;
                lives = 3;
                fireRate = 250;
                image = new ImageIcon("src/resources/images/fastPlane.png").getImage();
                break;

            case 3:
                speed = 5;
                lives = 3;
                fireRate = 150;
                image = new ImageIcon("src/resources/images/SniperPlane.png").getImage();
                break;

            case 4:
                speed = 4;
                lives = 5;
                fireRate = 200;
                image = new ImageIcon("src/resources/images/HeavyPlane.png").getImage();
                break;
            default:
                speed = 5;
                lives = 3;
                fireRate = 300;
                image = new ImageIcon("src/resources/images/DefaultPlane.png").getImage();
        }

    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, width, height, null);
    }

    public void moveLeft() {
        if (x > 0)
            x -= speed;
    }

    public void moveRight() {
        if (x + width < GameMain.WINDOW_WIDTH)
            x += speed;
    }

    public void moveUp() {
        if (y > 0)
            y -= speed;
    }

    public void moveDown() {
        if (y + height < GameMain.WINDOW_HEIGHT)
            y += speed;
    }


    //گتر ستر ها
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getSpeed() {
        return speed;
    }

    public int getFireRate() {
        return fireRate;
    }

    public int getBulletCount() {
        return bulletCount;
    }

    public void setBulletCount(int bulletCount) {
        this.bulletCount = bulletCount;
    }

    public boolean hasShield() {
        return shield;
    }

    public void setShield(boolean shield) {
        this.shield = shield;
    }

    public boolean isRapidFire() {
        return rapidFire;
    }

    public void setRapidFire(boolean rapidFire) {
        this.rapidFire = rapidFire;
    }

    public int getType() {
        return type;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


}
