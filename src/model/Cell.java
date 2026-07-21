package model;

import enemy.*;
import main.GameMain;

public class Cell {

    private int row;
    private int col;
    private Enemy enemy;

    private int x;
    private int y;

    private EnemyType type;

    // تعداد دفعاتی که این خانه هنوز باید دوباره پر شود
    private int counter;

    public Cell(int row, int col, int x, int y, EnemyType type, int counter) {
        this.row = row;
        this.col = col;
        this.x = x;
        this.y = y;
        this.type = type;
        this.counter = counter;
    }

    // آیا این خانه در حال حاضر دشمن دارد؟
    public boolean hasEnemy() {
        return enemy != null;
    }

    // حذف دشمن از خانه
    public void removeEnemy() {
        enemy = null;
        decreaseCounter();
    }

    // کم شدن شمارنده
    public void decreaseCounter() {
        if (counter > 0) {
            counter--;
        }
    }

    // آیا این خانه هنوز باید دوباره دشمن تولید کند؟
    public boolean hasRemainingEnemies() {
        return counter > 0;
    }


    // گترها
    public int getRow() {
        return row;
    }
    public Enemy getEnemy() {
        return enemy;
    }
    public int getX() { return x; }
    public int getY() { return y; }


    // سترها
    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }

    public Enemy createEnemy(Level level) {

        switch (type) {

            case NORMAL:
                return new NormalEnemy(x,y, level.getNormalHealth());

            case FAST:
                return new FastEnemy(x,y, level.getFastHealth());

            case ZIGZAG:
                return new ZigzagEnemy(x,y, level.getZigzagHealth());

            case SHOOTER:
                return new ShooterEnemy(x,y, level.getShooterHealth());

        }
        return null;

    }

    public Enemy spawnEnemy(Level level, double offsetX, int offsetY) {

        if (counter <= 0)
            return null;

        Enemy enemy = createEnemy(level);

        // مرغ جایگزین از گوشه بالا-چپ یا بالا-راست وارد می‌شود
        boolean fromLeft = Math.random() < 0.5;

        if (fromLeft) {
            enemy.setX(-enemy.getWidth());
        } else {
            enemy.setX(GameMain.WINDOW_WIDTH);
        }

        enemy.setY(-enemy.getHeight());

        int targetX = (int) Math.round(x + offsetX);
        int targetY = y + offsetY;

        enemy.setTarget(targetX, targetY);

        this.enemy = enemy;

        return enemy;
    }

    public int getBaseX() { return x; }
    public int getBaseY() { return y; }

}
