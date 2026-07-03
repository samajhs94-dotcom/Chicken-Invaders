package enemy;

import javax.swing.*;

public class FastEnemy extends Enemy{

        public FastEnemy(int x, int y) {
            super(x, y, 2,1, new ImageIcon("src/resources/images/FastEnemy.png").getImage());
        }

        @Override
        public void move() {
            // حرکت شبکه‌ای توسط EnemyManager مدیریت می‌شود.
        }

}
