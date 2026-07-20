package model;

import main.GameMain;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Plane {

    public static final int MAX_LIVES = 5;
    public static final int MAX_BULLET_COUNT = 10;

    private int x;
    private int y;
    private int width;
    private int height;
    private int speed;
    private int lives;
    private int type;//این قسمت برای وقتیه که خواستم قسمت اختیاری پروژه رو بزنم
    private int bulletCount;
    private boolean shield;
    private boolean rapidFire;
    private Image image;
    private BufferedImage shieldGlowImage;
    private int fireRate;

    private boolean invincible = false;
    private long invincibleStartTime = 0;


    public Plane(int x,int y,int type){

        this.x=x;
        this.y=y;
        width=75;
        height=75;

        this.type=type;
        bulletCount = 1;

        shield = false;
        rapidFire = false;

        switch(type){
            case 1:
                speed = 5;
                lives = 3;
                fireRate = 300;
                image = new ImageIcon("src/resources/images/defaultPlane.png").getImage();
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
                image = new ImageIcon("src/resources/images/defaultPlane.png").getImage();
        }

        createShieldGlow();

    }

    private void createShieldGlow() {

        shieldGlowImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = shieldGlowImage.createGraphics();

        // کپی کردن شکل هواپیما
        g2.drawImage(image, 0, 0, width, height, null);

        // عوض کردن شکل کپی شده به ابی
        g2.setComposite(AlphaComposite.SrcIn);
        g2.setColor(new Color(80, 190, 255, 200));
        g2.fillRect(0, 0, width, height);

        g2.dispose();
    }

    public void draw(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        if (shield && shieldGlowImage != null) {

            // Outer glow
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.18f));

            for (int dx = -7; dx <= 7; dx += 7) {
                for (int dy = -7; dy <= 7; dy += 7) {
                    if (dx != 0 || dy != 0) {
                        g2.drawImage(shieldGlowImage, x + dx, y + dy, null);
                    }
                }
            }

            // Inner glow
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.35f));

            for (int dx = -3; dx <= 3; dx += 3) {
                for (int dy = -3; dy <= 3; dy += 3) {
                    if (dx != 0 || dy != 0) {
                        g2.drawImage(shieldGlowImage, x + dx, y + dy, null);
                    }
                }
            }
        }

        // Normal drawing mode
        g2.setComposite(AlphaComposite.SrcOver);

        // Draw plane over glow
        g2.drawImage(image, x, y, width, height, null);

        g2.dispose();
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

    public long getInvincibleStartTime() {
        return invincibleStartTime;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
        if (invincible) {
            invincibleStartTime = System.currentTimeMillis();
        }
    }

    //این متد مشخص میکنه هواپیما تو فریم فعلی رسم بشه یا نه
    public boolean shouldRender() {

        if (!invincible) return true;

        // هر 200ms یک بار خاموش/روشن
        long now = System.currentTimeMillis()-invincibleStartTime;

        return ((now / 200) % 2) == 0;
    }

    // اگر هواپیما آسیب‌ناپذیر است زمان پاز را به زمان شروع اضافه می‌کنیم
    public void addInvinciblePauseTime(long pauseDuration) {

        if (invincible) {
            invincibleStartTime += pauseDuration;
        }
    }


}
