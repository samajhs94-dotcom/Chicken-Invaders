package enemy;

import javax.swing.*;

public class ShooterEnemy extends Enemy {

    private long lastShotTime;

    public ShooterEnemy(int x,int y){
        super(x, y, 1.0, 3, new ImageIcon("src/resources/images/shooterChicken.png").getImage());
    }

    @Override
    public void move(){
        // حرکت شبکه توسط GamePanel
    }

    public boolean canShoot(){

        long current=System.currentTimeMillis();
        if(current-lastShotTime>=2000){
            lastShotTime=current;
            return true;
        }
        return false;

    }

}
