package enemy;

import javax.swing.*;

public class NormalEnemy extends Enemy{

    public NormalEnemy(int x, int y, int health){
        super(x, y, 1, health, new ImageIcon("src/resources/images/NormalEnemy.png").getImage());
    }

    @Override
    public void move(){}
    //چون حرکت مرغ ها شبکه ایه و تو بخش دیگه ای کنترل میشه

}
