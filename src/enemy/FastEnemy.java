package enemy;

import javax.swing.*;

public class FastEnemy extends Enemy{


    public FastEnemy(int x, int y,int health) {
        super(x, y, 2,health, new ImageIcon("src/resources/images/FastEnemy.png").getImage());
    }

}
