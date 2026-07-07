package enemy;

import java.awt.*;

public abstract class Enemy {

    //موقعیت، سرعت، میزان سلامتی دشمن
    protected double x;
    protected double y;
    protected int width;
    protected int height;
    protected double speed;
    protected int health;

    protected double targetX;
    protected double targetY;
    private boolean reachedTarget = true;

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
        g.drawImage(image,getX(), getY(),width,height,null);
    }

    public void takeDamage() {
        health--;
    }

    public boolean isDead() {
        return health <= 0;
    }

    // محدوده برخورد
    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), width, height);
    }

    //گترها
    public int getX() {
        return (int) Math.round(x);
    }

    public int getY() {
        return (int) Math.round(y);
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

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setTarget(double x, double y) {
        targetX = x;
        targetY = y;
        reachedTarget=false;
    }

    public void moveToTarget() {

        if (reachedTarget)
            return;

        double dx = targetX - x;
        double dy = targetY - y;
        double distance = Math.sqrt(dx * dx + dy * dy);


        double flySpeed = speed * 5.0;

        if (distance <= flySpeed) {
            forceSetTarget(targetX, targetY);
            return;
        }

        x += (dx / distance) * flySpeed;
        y += (dy / distance) * flySpeed;


    }

    public boolean hasReachedTarget() {
        return reachedTarget;
    }

    public void forceSetTarget(double x, double y) {
        this.x = x;
        this.y = y;
        this.targetX = x;
        this.targetY = y;
        reachedTarget = true;
    }

}
