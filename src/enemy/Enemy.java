package enemy;

import java.awt.*;

public abstract class Enemy {

    //موقعیت، سرعت، میزان سلامتی دشمن
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected double speed;
    protected int health;

    protected Image image;

    public Enemy(int x, int y, double speed, int health, Image image) {

        this.x = x;
        this.y = y;
        this.speed = speed;
        this.health = health;
        this.image = image;
        width = 50;
        height = 50;

    }

    public void draw(Graphics g){
        g.drawImage(image,x,y,width,height,null);
    }

    public void takeDamage() {
        health--;
    }

    public boolean isDead() {
        return health <= 0;
    }
    // حرکت (هر نوع دشمن خودش پیاده‌سازی می‌کند)
    public abstract void move();

    // محدوده برخورد
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    //گترها
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHealth() {
        return health;
    }

    public double getSpeed() {
        return speed;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


}
