package model;

import enemy.*;

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

    // آیا این خانه کاملاً تمام شده است؟
    public boolean isFinished() {
        return enemy == null && counter == 0;
    }

    // گترها
    public int getRow() {
        return row;
    }
    public int getColumn() {
        return col;
    }
    public Enemy getEnemy() {
        return enemy;
    }
    public int getCounter() {
        return counter;
    }
    public int getX() { return x; }
    public int getY() { return y; }
    public EnemyType getType() { return type; }


    // سترها
    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }
    public void setCounter(int counter) {
        this.counter = counter;
    }


}
