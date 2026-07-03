package enemy;

import javax.swing.*;

public class ZigzagEnemy extends Enemy{

    private double angle;

    public ZigzagEnemy(int x, int y) {
        super(x, y, 1.5, 2, new ImageIcon("src/resources/images/ZigzagEnemy.png").getImage());
        angle=0;
    }

    @Override
    public void move() {
        angle += 0.2;
    }

}
