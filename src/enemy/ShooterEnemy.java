package enemy;

import javax.swing.*;

public class ShooterEnemy extends Enemy {

    private long lastShotTime=0;

    public ShooterEnemy(int x,int y,int health){
        super(x, y, 1.0, health, new ImageIcon("src/resources/images/shooterChicken.png").getImage());
    }

    public boolean canShoot(long currentTime) {
        return currentTime - lastShotTime >= 3000;
    }

    //گلوله ساخته شه زمان اخرین شلیک ثبت میشه
    public void recordShot(long currentTime) {
        lastShotTime = currentTime;
    }

}
