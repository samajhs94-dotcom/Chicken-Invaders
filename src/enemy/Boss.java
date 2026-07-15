package enemy;

import model.BossBullet;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Boss {

    protected double x, y;
    protected int width = 150, height = 150;
    protected int maxHealth;
    protected int health;
    protected double horizontalSpeed;
    protected int direction = 1;
    protected long lastAttackTime = 0;
    protected long attackInterval;
    protected int directions;
    protected double bulletSpeed;
    protected Image image;

    public Boss(int x, int y, int maxHealth, double horizontalSpeed,
                long attackInterval, int directions,double bulletSpeed, Image image) {

        this.x = x;
        this.y = y;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.horizontalSpeed = horizontalSpeed;
        this.attackInterval = attackInterval;
        this.directions = directions;
        this.bulletSpeed = bulletSpeed;
        this.image = image;

    }

    public abstract void move(int screenWidth);

    // تولید گلوله‌ها در ان جهت مساوی
    public java.util.List<BossBullet> tryAttack() {

        long now = System.currentTimeMillis();
        List<BossBullet> newBullets = new ArrayList<>();

        if (now - lastAttackTime >= attackInterval) {

            lastAttackTime = now;
            double step = 360.0 / directions;
            double cx = x + width / 2.0;
            double cy = y + height / 2.0;

            for (int i = 0; i < directions; i++) {
                newBullets.add(new BossBullet(cx, cy, i * step, bulletSpeed));
            }
        }

        return newBullets;
    }

    public void takeDamage() {
        health--;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) Math.round(x), (int) Math.round(y), width, height);
    }

    public void draw(Graphics g) {
        g.drawImage(image, (int) Math.round(x), (int) Math.round(y), width, height, null);
        drawHealthBar(g);
    }

    //نوار سلامت که هر فریم رسم می‌شه و با آسیب دیدن غول کوتاه‌تر می‌شه
    protected void drawHealthBar(Graphics g) {

        int barWidth = 300, barHeight = 20;
        int barX = (800 - barWidth) / 2;
        int barY = 20;

        double ratio = (double) health / maxHealth; //درصد جون باقی مونده

        g.setColor(Color.DARK_GRAY);
        g.fillRect(barX, barY, barWidth, barHeight);

        g.setColor(ratio > 0.5 ? Color.GREEN : (ratio > 0.25 ? Color.ORANGE : Color.RED));
        g.fillRect(barX, barY, (int) (barWidth * ratio), barHeight);

        g.setColor(Color.WHITE);
        g.drawRect(barX, barY, barWidth, barHeight);
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getX() {
        return (int) Math.round(x);
    }

    public int getY() {
        return (int) Math.round(y);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
