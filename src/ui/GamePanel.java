package ui;

import enemy.*;
import main.GameMain;
import manager.DatabaseManager;
import manager.LevelManager;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
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
    private User currentUser;

    private ArrayList<Egg> eggs = new ArrayList<>();
    private int direction = 1;

    private boolean gameOver = false;

    private LevelManager levelManager ;
    private boolean levelFinished = false;
    private Level currentLevel;

    private long lastEggDropTime = 0;

    private ArrayList<EnemyBullet> enemyBullets = new ArrayList<>();

    private Cell[][] grid;

    private double formationOffsetX = 0;

    private int[] rowOffsetsY = new int[5]; // برای ۵ ردیف

    private DatabaseManager dbManager;

    private boolean victory = false;

    private Boss boss;
    private ArrayList<BossBullet> bossBullets = new ArrayList<>();

    private boolean paused = false;

    private GameMain gameMain;

    private ArrayList<PowerUp> powerUps = new ArrayList<>();

    private static final double POWER_UP_DROP_CHANCE = 0.20;

    private static final long RAPID_FIRE_DURATION = 8000;
    private static final long SHIELD_DURATION = 10000;
    private static final long FREEZE_DURATION = 3000;

    private static final int RAPID_FIRE_RATE = 100;

    private long rapidFireEndTime = 0;
    private long shieldEndTime = 0;
    private long freezeEndTime = 0;


    public GamePanel(DatabaseManager dbManager,GameMain gameMain) {

        super("src/resources/images/1.png");

        heartImage = new ImageIcon("src/resources/images/heart.png").getImage();
        // فعلا باشه تا اوکیش کنم
        score = 0;

        this.gameMain = gameMain;

        this.dbManager = dbManager;

        levelManager = new LevelManager();
        currentLevel = levelManager.getCurrentLevel();
        grid = levelManager.buildGrid(currentLevel);

        Arrays.fill(rowOffsetsY, 0);

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

        if (currentUser != null) {

            resetGameState();
            levelManager.reset();

            currentLevel = levelManager.getCurrentLevel();
            startCurrentLevel();

        }
    }

    private void resetGameState() {

        score = 0;
        gameOver = false;
        victory = false;
        levelFinished = false;

        //اگه موقع خروج از بازی کلیدی نگه داشته شده باشه اثرش تو بازی جدید نمونه
        paused = false;
        leftPressed = false;
        rightPressed = false;
        upPressed = false;
        downPressed = false;

        plane = new Plane((GameMain.WINDOW_WIDTH - 75) / 2, 400, 1);

        bullets.clear();
        eggs.clear();
        enemyBullets.clear();
        bossBullets.clear();
        powerUps.clear();
        boss = null;

        lastShotTime = 0;
        lastEggDropTime = 0;

        rapidFireEndTime = 0;
        shieldEndTime = 0;
        freezeEndTime = 0;

        plane.setRapidFire(false);
        plane.setShield(false);

        if (gameTimer != null) {
            gameTimer.stop();
        }
    }



    private void startCurrentLevel() {

        formationOffsetX = 0;
        direction = 1;
        Arrays.fill(rowOffsetsY, 0);

        if (currentLevel.isBossLevel()) {

            grid = null;
            enemies.clear();

            int startX = (GameMain.WINDOW_WIDTH - 150) / 2;

            if (currentLevel.getLevelNumber() == 4) {
                boss = new BossLevel4(startX, 60);
            } else {
                boss = new BossLevel8(startX, 40);
            }

            bossBullets.clear();

        } else {

            boss = null;
            bossBullets.clear();
            grid = levelManager.buildGrid(currentLevel);
            createEnemies();

        }
    }

    private void createEnemies() {

        enemies.clear();

        Arrays.fill(rowOffsetsY, 0);

        for (int r = 0; r < grid.length; r++) {

            for (int c = 0; c < grid[r].length; c++) {

                Cell cell = grid[r][c];
                Enemy enemy = cell.createEnemy(currentLevel);                cell.setEnemy(enemy);
                enemies.add(enemy);

            }

        }

    }

    public void startGame() {

        if (gameTimer != null && !gameTimer.isRunning()) {
            gameTimer.start();
        }
        requestFocusInWindow();
    }

    public void stopGame() {
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
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
        updatePowerUpTimers();

        updateBullets();
        updatePowerUps();

        if (currentLevel.isBossLevel()) {
            updateBossLevel();
        } else {
            updateNormalLevel();
        }

        checkLevelCompletion();

    }


    private void updateNormalLevel() {

        if(!isFreezeActive()) {

            moveEnemies();

            if (anyEnemyReachedBottom()) {
                gameOver();
                return;
            }

            handleEggDrop();
            updateZigzagEggAnimations();

            handleShooterAttack();
            updateEnemyBullets();

            updateEggs();
        }

        checkEnemyPlaneCollision();
        checkBulletEnemyCollision();

    }

    private void updateBossLevel() {

        if (boss == null) return;

        if(!isFreezeActive()) {

            boss.move(GameMain.WINDOW_WIDTH);
            bossBullets.addAll(boss.tryAttack());

            Iterator<BossBullet> it = bossBullets.iterator();
            Rectangle planeRect = new Rectangle(plane.getX(), plane.getY(), plane.getWidth(), plane.getHeight());

            while (it.hasNext()) {
                BossBullet b = it.next();
                b.move();

                if (b.isOutOfScreen(GameMain.WINDOW_WIDTH, GameMain.WINDOW_HEIGHT)) {
                    it.remove();
                    continue;
                }
                if (b.getBounds().intersects(planeRect)) {
                    handlePlayerHit();
                    it.remove();
                }
            }

        }

        checkBulletBossCollision();
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

                    case KeyEvent.VK_P:
                        paused = !paused;
                        if (paused) {
                            gameTimer.stop();
                        } else {
                            gameTimer.start();
                        }
                        repaint();
                        break;

                    case KeyEvent.VK_ESCAPE:
                        if (!gameOver && !victory) {
                            saveCurrentGameToDatabase();
                        }
                        gameMain.showMainMenu();
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

        if (boss != null) {
            boss.draw(g);
        }

        for (BossBullet b : bossBullets) {
            b.draw(g);
        }

        for (Egg egg : eggs) {
            egg.draw(g);
        }

        for (EnemyBullet b : enemyBullets) {
            b.draw(g);
        }

        for (PowerUp powerUp : powerUps) {
            powerUp.draw(g);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial",Font.BOLD,18));
        g.drawString("Lives:",20,30);

        for(int i=0;i<plane.getLives();i++){
            g.drawImage(heartImage, 90+i*30, 10, 25, 25, null);
        }

        g.drawString("Score: "+score,20,60);

        g.drawString("Level: "+levelManager.getLevelNumber(),20,90);

        if (currentUser != null) {
            g.drawString("Player: " + currentUser.getUsername(), 20, 120);
        }

        g.drawString("Fire : " + plane.getBulletCount(),20,150);

        drawPowerUpStatus(g);

        pausedGame(g);

        if (gameOver) {
            drawOverlay(g, "!! GAME OVER !!", Color.RED);
        }

        if (victory) {
            drawOverlay(g, " YOU WON :)", Color.GREEN);
        }

    }

    private void pausedGame(Graphics g){
        if (paused) {

            Graphics2D g2 = (Graphics2D) g;

            // تیره شدن صفحه
            g2.setColor(new Color(0, 0, 0, 170));
            g2.fillRect(0, 0, getWidth(), getHeight());

            // مثلث وسط صفحه
            Polygon triangle = new Polygon();

            triangle.addPoint(getWidth()/2 - 15, getHeight()/2 - 20);
            triangle.addPoint(getWidth()/2 - 15, getHeight()/2 + 20);
            triangle.addPoint(getWidth()/2 + 20, getHeight()/2);

            g2.setColor(Color.WHITE);
            g2.fillPolygon(triangle);
        }
    }

    private void drawOverlay(Graphics g, String text, Color color) {

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(new Color(0, 0, 0, 170));
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setFont(new Font("Impact", Font.BOLD, 50));
        g2.setColor(color);

        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(text)) / 2;
        int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

        g2.drawString(text, x, y);
    }

    private void updateZigzagEggAnimations() {
        for (Enemy enemy : enemies) {
            if (enemy instanceof ZigzagEnemy) {
                ((ZigzagEnemy) enemy).updateEggZigzag();
            }
        }
    }


    private void shoot() {

        long currentTime = System.currentTimeMillis();

        int currentFireRate;
        if (plane.isRapidFire()) {
            currentFireRate = RAPID_FIRE_RATE;
        } else {
            currentFireRate = plane.getFireRate();
        }

        if(currentTime-lastShotTime < currentFireRate)
            return;

        int bulletCount = plane.getBulletCount();
        int spacing = 18;

        //مرکز هواپیما، نصف عرض گلوله و تنظیم بر اساس تعداد تیرها
        int startX = plane.getX() + plane.getWidth() / 2 - 16
                - ((bulletCount - 1) * spacing) / 2;

        for (int i = 0; i < bulletCount; i++) {
            bullets.add(new Bullet(startX + i * spacing, plane.getY()));
        }

        lastShotTime=currentTime;

    }

    //حرکت پاوراپ ها، بررسی خروج از صفحه و برخورد با هواپیما
    private void updatePowerUps() {

        Iterator<PowerUp> it = powerUps.iterator();

        Rectangle planeRect = new Rectangle(plane.getX(),
                plane.getY(), plane.getWidth(), plane.getHeight());

        while (it.hasNext()) {

            PowerUp powerUp = it.next();
            powerUp.move();

            if (powerUp.isOutOfScreen(GameMain.WINDOW_HEIGHT)) {
                it.remove();
                continue;
            }

            if (powerUp.getBounds().intersects(planeRect)) {
                applyPowerUp(powerUp);
                it.remove();
            }
        }
    }

    //اعمال اثر پاوراپ
    private void applyPowerUp(PowerUp powerUp) {

        long now = System.currentTimeMillis();

        switch (powerUp.getType()) {

            case FIRE_ADD:
                plane.setBulletCount(plane.getBulletCount() + 1);
                break;

            case RAPID_FIRE:
                plane.setRapidFire(true);
                rapidFireEndTime = now + RAPID_FIRE_DURATION;
                break;

            case EXTRA_LIFE:
                if (plane.getLives() < Plane.MAX_LIVES) {
                    plane.setLives(plane.getLives() + 1);
                }
                break;

            case SHIELD:
                plane.setShield(true);
                shieldEndTime = now + SHIELD_DURATION;
                break;

            case FREEZE_BOMB:
                freezeEndTime = now + FREEZE_DURATION;
                break;

        }

    }

    //چک کردن تموم شدن تایم پاوراپ های موقت
    private void updatePowerUpTimers() {

        long now = System.currentTimeMillis();

        if (plane.isRapidFire() && now > rapidFireEndTime) {
            plane.setRapidFire(false);
        }

        if (plane.hasShield() && now > shieldEndTime) {
            plane.setShield(false);
        }

    }

    private boolean isFreezeActive() {
        return System.currentTimeMillis() < freezeEndTime;
    }

    // وقتی دشمن میمیره با احتمال 20 درصد ساخت پاوراپ
    private void trySpawnPowerUp(int x, int y) {

        if (Math.random() > POWER_UP_DROP_CHANCE) {
            return;
        }

        PowerUp.Type randomType = PowerUp.getRandomType();

        powerUps.add(new PowerUp(x, y, randomType));
    }

    private void drawPowerUpStatus(Graphics g) {

        long now = System.currentTimeMillis();
        int y = 180;

        if (plane.isRapidFire()) {
            g.drawString("Rapid: " + Math.max(0, (rapidFireEndTime - now) / 1000) + "s", 20, y);
            y += 30;
        }

        if (plane.hasShield()) {
            g.drawString("Shield: " + Math.max(0, (shieldEndTime - now) / 1000) + "s", 20, y);
            y += 30;
        }

        if (isFreezeActive()) {
            g.drawString("Freeze: " + Math.max(0, (freezeEndTime - now) / 1000) + "s", 20, y);
        }
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

                        Cell cell = findCell(enemy);
                        enemyIterator.remove();

                        //ساخت پاوراپ با احتمال 20 درصد
                        trySpawnPowerUp(enemy.getX() + enemy.getWidth() / 2 - 20,
                                enemy.getY() + enemy.getHeight() / 2 - 20);

                        if (cell != null) {

                            cell.removeEnemy();
                            if (cell.hasRemainingEnemies()) {

                                Enemy newEnemy = cell.spawnEnemy(currentLevel, formationOffsetX, rowOffsetsY[cell.getRow()]);
                                if (newEnemy != null) {
                                    enemies.add(newEnemy);

                                }

                            }

                        }

                        score += getEnemyScore(enemy);
                    }
                    break;

                }

            }
        }

    }

    // برخورد گلوله‌ی هواپیما با غول
    private void checkBulletBossCollision() {

        if (boss == null) return;

        Iterator<Bullet> it = bullets.iterator();

        while (it.hasNext()) {
            Bullet bullet = it.next();

            if (bullet.getBounds().intersects(boss.getBounds())) {

                boss.takeDamage();
                it.remove();

                // برخورد گلوله به غول
                if (boss.isDead()) {

                    // مرگ غول

                    if (currentLevel.getLevelNumber() == 4) {
                        score += 500;
                    } else {
                        score += 1000;
                    }
                    bossBullets.clear();
                    boss = null;
                    return;
                }
            }
        }
    }

    private int getEnemyScore(Enemy enemy) {

        if (enemy instanceof NormalEnemy)
            return 10;

        if (enemy instanceof FastEnemy)
            return 15;

        if (enemy instanceof ZigzagEnemy)
            return 20;

        if (enemy instanceof ShooterEnemy)
            return 25;

        return 0;
    }


    //تولید تخم مرغ برای پرتاب
    private void handleEggDrop() {

        if (enemies.isEmpty())
            return;

        long now = System.currentTimeMillis();

        if (now - lastEggDropTime < currentLevel.getEggInterval())
            return;

        lastEggDropTime = now;

        Enemy enemy = enemies.get((int)(Math.random() * enemies.size()));

        eggs.add(new Egg(enemy.getX() + enemy.getWidth()/2,
                enemy.getY() + enemy.getHeight()));

        if (enemy instanceof ZigzagEnemy) {
            ((ZigzagEnemy) enemy).startEggZigzag();
        }

    }

    //تولید گلوله دشمن
    private void handleShooterAttack() {

        long now = System.currentTimeMillis();

        final int verticalRange = 80;

        for (Enemy enemy : enemies) {
            if (enemy instanceof ShooterEnemy shooter) {

                int shooterCenterY = shooter.getY() + shooter.getHeight() / 2;
                int planeCenterY = plane.getY() + plane.getHeight() / 2;

                // وقتی گلوله افقی شوتر شلیک شه هواپیما سمتش باشه
                if (Math.abs(shooterCenterY - planeCenterY) > verticalRange) {
                    continue;
                }

                // ایا زمان کافی از شلیک قبلی گذشته یا نه
                if (!shooter.canShoot(now)) {
                    continue;
                }

                // فقط بعضی وقتها شلیک کنه نه همیشه
                if (Math.random() > 0.55) {
                    continue;
                }

                int dx;

                if (plane.getX() < shooter.getX()) {
                    dx = -5;
                } else {
                    dx = 5;
                }

                enemyBullets.add(new EnemyBullet(shooter.getX() + shooter.getWidth() / 2,
                        shooter.getY() + shooter.getHeight() / 2, dx, 0));

                //ثبت زمان شلیک
                shooter.recordShot(now);

            }

        }

    }

    //حرکت و برخورد گلوله دشمن
    private void updateEnemyBullets() {

        Iterator<EnemyBullet> it = enemyBullets.iterator();

        Rectangle planeRect = new Rectangle(plane.getX(), plane.getY(), plane.getWidth(), plane.getHeight());

        while (it.hasNext()) {

            EnemyBullet b = it.next();
            b.move();

            if (b.isOutOfScreen(GameMain.WINDOW_WIDTH, GameMain.WINDOW_HEIGHT)) {
                it.remove();
                continue;
            }

            if (b.getBounds().intersects(planeRect)) {
                handlePlayerHit();
                it.remove();
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

        if (plane.hasShield())
            return;

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

        if (gameOver || victory) {
            return;
        }
        gameTimer.stop();
        gameOver = true;
        saveCurrentGameToDatabase();
        repaint();

    }

    private void winGame() {
        if (gameOver || victory) {
            return;
        }
        gameTimer.stop();
        victory = true;
        saveCurrentGameToDatabase();
        repaint();
    }

    private void saveCurrentGameToDatabase() {

        if (currentUser == null || dbManager == null) {
            return; // اگر کاربر وارد نشده باشد، رکوردی ذخیره نمی‌شود
        }

        int levelReached = levelManager.getLevelNumber();


        dbManager.saveGame(currentUser, score, levelReached, currentUser.getMusicEnabled(),
                currentUser.getShotSoundEnabled(), currentUser.getExplosionSoundEnabled(),
                currentUser.getGameOverSoundEnabled());


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

    private boolean anyEnemyReachedBottom() {

        if (enemies.isEmpty()) {
            return false;
        }
        for (Enemy enemy : enemies) {
            //اگه یکی از مرغها به پایین صفحه رسید گیم اوررر
            if (enemy.getY() + enemy.getHeight() >= GameMain.WINDOW_HEIGHT) {
                return true;
            }
        }
        return false;

    }

    private void moveEnemies() {

        if (enemies.isEmpty() || currentLevel.isBossLevel() || grid == null)
            return;

        int leftColumn = 0;

        while (leftColumn < grid[0].length && !isColumnActive(leftColumn)) {
            leftColumn++;
        }

        int rightColumn = grid[0].length - 1;

        while (rightColumn >= 0 && !isColumnActive(rightColumn)) {
            rightColumn--;
        }

        if (leftColumn >= grid[0].length || rightColumn < 0) {
            return;
        }

        int enemyWidth = 50;

        double leftEdge = grid[0][leftColumn].getBaseX() + formationOffsetX;
        double rightEdge = grid[0][rightColumn].getBaseX() + formationOffsetX + enemyWidth;

        if (leftEdge <= 0 || rightEdge >= GameMain.WINDOW_WIDTH) {

            direction *= -1;

            for (int r = 0; r < rowOffsetsY.length; r++) {
                rowOffsetsY[r] += currentLevel.getDownStep();
            }

            // جلوگیری از گیر کردن روی لبه
            if (leftEdge <= 0) {
                formationOffsetX = -grid[0][leftColumn].getBaseX();
            }
            else {
                formationOffsetX = GameMain.WINDOW_WIDTH - enemyWidth
                        - grid[0][rightColumn].getBaseX();
            }

        }

        //حرکت اصلی کل شبکه
        formationOffsetX += direction * currentLevel.getHorizontalSpeed();

        for (Enemy enemy : enemies) {

            Cell cell = findCell(enemy);

            if (cell == null) {
                continue;
            }

            double targetX = cell.getBaseX() + formationOffsetX;
            double targetY = cell.getBaseY() + rowOffsetsY[cell.getRow()];

            if (enemy.hasReachedTarget()) {
                // مرغ معمولی، زیگزاگ، شوتر و فست همه جایگاه شبکه‌ای خودشان را میگیرن
                enemy.setX(targetX);
                enemy.setY(targetY);
            }
            else {
                // مرغ جایگزین از بالا میاد سمت جایگاه خودش
                enemy.setTarget(targetX, targetY);
                enemy.moveToTarget();
            }

        }

    }


    private boolean isColumnActive(int col) {

        for (int r = 0; r < grid.length; r++) {

            Cell cell = grid[r][col];

            // اگر هنوز مرغ دارد
            if (cell.hasEnemy())
                return true;

            // اگر قرار است دوباره مرغ تولید شود
            if (cell.hasRemainingEnemies())
                return true;
        }

        return false;
    }


    private Cell findCell(Enemy enemy) {

        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {

                if (grid[r][c].getEnemy() == enemy)
                    return grid[r][c];

            }
        }

        return null;
    }

    // بررسی پایان مرحله برای هر دو حالت (شبکه‌ای و غول)
    private void checkLevelCompletion() {

        boolean finished;

        if (currentLevel.isBossLevel()) {
            finished = (boss == null);
        } else {
            finished = enemies.isEmpty();
        }

        if (finished && !levelFinished) {

            levelFinished = true;

            if (!currentLevel.isBossLevel()) {
                score += 200;
            }

            if (levelManager.isLastLevel()) {
                winGame();
                return;
            }

            levelManager.nextLevel();
            currentLevel = levelManager.getCurrentLevel();

            startCurrentLevel();
            levelFinished = false;
        }
    }


}
