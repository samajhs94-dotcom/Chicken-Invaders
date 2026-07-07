package model;

public class Level {


    private int levelNumber;

    private int normalHealth;
    private int fastHealth;
    private int zigzagHealth;
    private int shooterHealth;

    private int cellCounter;

    private double horizontalSpeed;
    private int downStep;

    private long eggInterval;

    private EnemyType[] allowedTypes;

    //فیلدهای مخصوص مراحل 4 و8
    private boolean bossLevel;
    private int bossHealth;
    private long bossAttackInterval;
    private int bossDirections;

    public Level(int levelNumber, int normalHealth, int fastHealth,
                 int zigzagHealth, int shooterHealth, int cellCounter,
                 double horizontalSpeed, int downStep, long eggInterval,
                 EnemyType[] allowedTypes) {

        this.levelNumber = levelNumber;
        this.normalHealth = normalHealth;
        this.fastHealth = fastHealth;
        this.zigzagHealth = zigzagHealth;
        this.shooterHealth = shooterHealth;

        this.cellCounter = cellCounter;
        this.horizontalSpeed = horizontalSpeed;
        this.downStep = downStep;
        this.eggInterval = eggInterval;
        this.allowedTypes = allowedTypes;
    }

    public Level(int levelNumber, int bossHealth,
                 long bossAttackInterval, int bossDirections)
    {
        this.levelNumber = levelNumber;
        bossLevel = true;
        this.bossHealth = bossHealth;
        this.bossAttackInterval = bossAttackInterval;
        this.bossDirections = bossDirections;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public int getNormalHealth() {
        return normalHealth;
    }

    public int getFastHealth() {
        return fastHealth;
    }

    public int getZigzagHealth() {
        return zigzagHealth;
    }

    public int getShooterHealth() {
        return shooterHealth;
    }

    public int getCellCounter() {
        return cellCounter;
    }

    public double getHorizontalSpeed() {
        return horizontalSpeed;
    }

    public int getDownStep() {
        return downStep;
    }

    public long getEggInterval() {
        return eggInterval;
    }

    public EnemyType[] getAllowedTypes() {
        return allowedTypes;
    }


    public boolean isBossLevel() {
        return bossLevel;
    }

    public int getBossHealth() {
        return bossHealth;
    }

    public long getBossAttackInterval() {
        return bossAttackInterval;
    }

    public int getBossDirections() {
        return bossDirections;
    }
}
