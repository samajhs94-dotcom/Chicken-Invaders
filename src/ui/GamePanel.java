package ui;

import enemy.Enemy;
import enemy.NormalEnemy;
import model.Bullet;
import model.Plane;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;

public class GamePanel extends JPanel {

    private Plane plane;
    private ArrayList<Bullet> bullets;
    private ArrayList<Enemy> enemies;

    private Timer gameTimer;

    private boolean leftPressed;
    private boolean rightPressed;
    private boolean upPressed;
    private boolean downPressed;

    private long lastShotTime;

    public GamePanel() {

        setFocusable(true);
        setBackground(Color.BLACK);

        plane = new Plane(370, 500, 1);

        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        createEnemies();

        initializeTimer();

        initializeKeyboard();

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

    private void initializeTimer() {

        gameTimer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGame();
                repaint();
            }

        });
        gameTimer.start();

    }

    private void updateGame() {

        if(leftPressed)
            plane.moveLeft();

        if(rightPressed)
            plane.moveRight();

        if(upPressed)
            plane.moveUp();

        if(downPressed)
            plane.moveDown();

        Iterator<Bullet> iterator = bullets.iterator();

        while(iterator.hasNext()){

            Bullet bullet = iterator.next();
            bullet.move();
            if(bullet.isOutOfScreen()){
                iterator.remove();
            }

        }

        for (Enemy enemy : enemies) {
            enemy.move();
        }

    }

    // کنترل صفحه کلید
    private void initializeKeyboard() {

        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {

                switch (e.getKeyCode()) {

                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_A:
                        leftPressed = true;
                        break;

                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_D:
                        rightPressed = true;
                        break;

                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_W:
                        upPressed = true;
                        break;

                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_S:
                        downPressed = true;
                        break;

                    case KeyEvent.VK_SPACE://چون یه عمل لحظه ایه و نه پیوسته مثل بقیه کلید ها
                        shoot();
                        break;

                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

                switch (e.getKeyCode()) {

                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_A:
                        leftPressed = false;
                        break;

                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_D:
                        rightPressed = false;
                        break;

                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_W:
                        upPressed = false;
                        break;

                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_S:
                        downPressed = false;
                        break;
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        plane.draw(g);
        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }

    }

    private void shoot() {

        long currentTime = System.currentTimeMillis();

        if(currentTime-lastShotTime<plane.getFireRate())
            return;

        bullets.add( new Bullet ( plane.getX()+plane.getWidth()/2-5, plane.getY()));

        lastShotTime=currentTime;

    }


}
