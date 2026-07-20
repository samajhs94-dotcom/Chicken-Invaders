package model;

import javax.swing.*;
import java.awt.*;

public class PowerUp {

    public enum Type {
        FIRE_ADD,
        RAPID_FIRE,
        EXTRA_LIFE,
        SHIELD,
        FREEZE_BOMB
    }

    private int x;
    private int y;
    private final int width;
    private final int height;
    private final int speed;
    private final Type type;
    private Image image;

    public PowerUp(int x, int y, Type type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.width = 40;
        this.height = 40;
        this.speed = 2;
        loadImage();
    }

    public static Type getRandomType() {
        Type[] types = Type.values();
        return types[(int) (Math.random() * types.length)];
    }

    private void loadImage() {

        String path = getImagePath();
        ImageIcon icon = new ImageIcon(path);

        if (icon.getIconWidth() == -1) {
            System.out.println("PowerUp image not loaded: " + path);
            image = null;

        }
        else image = icon.getImage();

    }

    private String getImagePath() {
        switch (type) {
            case FIRE_ADD:
                return "src/resources/images/add_shot.png";

            case RAPID_FIRE:
                return "src/resources/images/fast_shot.png";

            case EXTRA_LIFE:
                return "src/resources/images/heal.png";

            case SHIELD:
                return "src/resources/images/sheild.png";

            case FREEZE_BOMB:
                return "src/resources/images/freeze.png";

            default:
                return "";
        }
    }

    public void move() {
        y += speed;
    }

    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        }
    }

    public boolean isOutOfScreen(int screenHeight) {
        return y > screenHeight;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public Type getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
