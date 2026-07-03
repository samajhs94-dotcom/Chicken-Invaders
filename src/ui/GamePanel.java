package ui;

import enemy.Enemy;
import enemy.NormalEnemy;
import main.GameMain;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;

public class GamePanel extends BackgroundPanel {

    private Plane plane;
    private ArrayList<Bullet> bullets;
    private ArrayList<Enemy> enemies;

    private Timer gameTimer;

    private boolean leftPressed;
    private boolean rightPressed;
    private boolean upPressed;
    private boolean downPressed;

    private long lastShotTime;

    private Image heartImage;
    private int score;
    private int level;
    private User currentUser;

    private ArrayList<Egg> eggs = new ArrayList<>();
    private int direction = 1;

    private boolean gameOver = false;


    public GamePanel() {

        super("src/resources/images/1.png");

        heartImage = new ImageIcon("src/resources/images/heart.png").getImage();
        // فعلا باشه تا اوکیش کنم
        score = 0;
        level = 1;

        // برای دریافت ورودی صفحه کلید
        setFocusable(true);

        plane = new Plane((GameMain.WINDOW_WIDTH-75)/2, 400, 1);

        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        createEnemies();

        initializeTimer();

        initializeKeyboard();

    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    //ایجاد دشمن های اولیه- فعلا NormalEnemy
    private void createEnemies() {

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 8; col++) {
                int x = 150 + col * 65;
                int y = 40 + row * 60;
                enemies.add(new NormalEnemy(x, y));
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

        updateInvincibility();

        updateBullets();

        moveEnemies();

        handleEggDrop();
        updateEggs();
        checkEnemyPlaneCollision();

        // برخورد تیر و دشمن
        checkBulletEnemyCollision();

    }

    private void updateBullets() {

        Iterator<Bullet> iterator = bullets.iterator();

        while (iterator.hasNext()) {

            Bullet bullet = iterator.next();
            bullet.move();
            if (bullet.isOutOfScreen()) {
                iterator.remove();
            }

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

        if (plane.shouldRender()) {
            plane.draw(g);
        }

        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial",Font.BOLD,18));
        g.drawString("Lives:",20,30);
        for(int i=0;i<plane.getLives();i++){
            g.drawImage(heartImage, 90+i*30, 10, 25, 25, null);
        }

        g.drawString("Score: "+score,20,60);
        g.drawString("Level: "+level,20,90);
        g.drawString("Player: "+currentUser.getUsername(),20,120);

        for (Egg egg : eggs) {
            egg.draw(g);
        }

        if (gameOver) {

            Graphics2D g2 = (Graphics2D) g;

            g2.setColor(new Color(0, 0, 0, 170));
            g2.fillRect(0, 0, getWidth(), getHeight());

            String text = "!! GAME OVER !!";

            g2.setFont(new Font("Impact", Font.BOLD, 60));
            g2.setColor(Color.RED);

            FontMetrics fm = g2.getFontMetrics();

            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

            g2.drawString(text, x, y);
        }

    }

    private void shoot() {

        long currentTime = System.currentTimeMillis();

        if(currentTime-lastShotTime<plane.getFireRate())
            return;

        bullets.add( new Bullet ( plane.getX()+plane.getWidth()/2-16, plane.getY()));

        lastShotTime=currentTime;

    }

    //بررسی اینکه ایا گلوله به دشمن برخورد کرده یا نه
    private void checkBulletEnemyCollision() {

        Iterator<Bullet> bulletIterator = bullets.iterator();

        while (bulletIterator.hasNext()) {

            Bullet bullet = bulletIterator.next();
            Iterator<Enemy> enemyIterator = enemies.iterator();

            while (enemyIterator.hasNext()) {

                Enemy enemy = enemyIterator.next();

                if (bullet.getBounds().intersects(enemy.getBounds())) {

                    enemy.takeDamage();
                    bulletIterator.remove();

                    if (enemy.isDead()) {
                        enemyIterator.remove();
                        score += 10;
                    }
                    break;

                }

            }
        }

    }

    //تولید تخم مرغ
    private void handleEggDrop() {

        for (Enemy enemy : enemies) {

            if (enemy instanceof enemy.ShooterEnemy) {
                if (Math.random() < 0.01) {
                    eggs.add(new Egg(enemy.getX() + enemy.getWidth()/2, enemy.getY()));
                }
            }

        }

    }


    //برخورد مرغ با هواپیما
    private void checkEnemyPlaneCollision() {

        Rectangle planeRect = new Rectangle(plane.getX(), plane.getY(), plane.getWidth(), plane.getHeight());

        for (Enemy enemy : enemies) {
            if (enemy.getBounds().intersects(planeRect)) {
                handlePlayerHit();
            }
        }

    }

    //کم شدن جون
    private void handlePlayerHit() {

        if (plane.isInvincible())
            return;

        plane.setLives(plane.getLives() - 1);

        //شروع حالت مخافظت
        plane.setInvincible(true);

        if (plane.getLives() <= 0) {
            gameOver();
        }

    }

    //مدیریت زمان اسیب ناپذیر بودن هواپیما
    private void updateInvincibility() {

        if (!plane.isInvincible())
            return;

        long now = System.currentTimeMillis();

        //2 ثانیه اسیب ناپذیری
        if (now - plane.getInvincibleStartTime() > 2000) {
            plane.setInvincible(false);
        }

    }

    private void gameOver() {

        gameTimer.stop();
        gameOver = true;
        repaint();

    }

    //حرکت تخم مرغ، حذف از صفحه، برخورد با هواپیما
    private void updateEggs() {

        Iterator<Egg> it = eggs.iterator();

        Rectangle planeRect = new Rectangle(plane.getX(), plane.getY(), plane.getWidth(), plane.getHeight());

        while (it.hasNext()) {

            Egg egg = it.next();
            egg.move();

            if (egg.isOutOfScreen()) {
                it.remove();
                continue;
            }

            if (egg.getBounds().intersects(planeRect)) {
                handlePlayerHit();
                it.remove();
            }

        }

    }

    private void moveEnemies() {

        boolean changeDirection = false;

        for (Enemy enemy : enemies) {

            if (enemy.getX() <= 0 || enemy.getX() + enemy.getWidth() >= GameMain.WINDOW_WIDTH) {
                changeDirection = true;
                break;
            }

        }

        if (changeDirection) {

            direction *= -1;

            for (Enemy enemy : enemies) {
                enemy.setY(enemy.getY() + 20);
            }

        }

        for (Enemy enemy : enemies) {
            enemy.setX(enemy.getX() + (int)(direction * enemy.getSpeed()));
        }

    }

}
