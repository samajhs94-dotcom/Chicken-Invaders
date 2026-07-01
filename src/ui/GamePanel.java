package ui;

import enemy.Enemy;
import enemy.NormalEnemy;
import model.Bullet;
import model.Plane;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel {

    private Plane plane;
    private ArrayList<Bullet> bullets;
    private ArrayList<Enemy> enemies;

    public GamePanel() {

        setFocusable(true);
        setBackground(Color.BLACK);

        plane = new Plane(370, 500, 1);

        bullets = new ArrayList<>();
        enemies = new ArrayList<>();

        createEnemies();
    }

    //ایجاد دشمن های اولیه- فعلا NormalEnemy
    private void createEnemies() {

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 8; col++) {
                int x = 60 + col * 80;
                int y = 40 + row * 70;
                enemies.add(new NormalEnemy(x, y, 2));
            }
        }

    }

}
