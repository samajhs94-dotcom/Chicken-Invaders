package enemy;

import javax.swing.*;

public class NormalEnemy extends Enemy{

    //در ادامه میزان سلامتی تو هر مرحله رو مشخص میکنم
    public NormalEnemy(int x, int y){
        super(x, y, 1, 1, new ImageIcon("src/resources/images/NormalEnemy.png").getImage());
    }

    @Override
    public void move(){}
    //چون حرکت مرغ ها شبکه ایه و تو بخش دیگه ای کنترل میشه

}
