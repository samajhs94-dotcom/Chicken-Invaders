package enemy;

import javax.swing.*;

public class BossLevel8 extends Boss{


    private double verticalPhase = 0;

    public BossLevel8(int startX, int startY) {
        super(startX, startY, 100, 2.0, 1000, 8,
                new ImageIcon("src/resources/images/boss2.png").getImage());
        width = 200;
        height = 200;
    }

    @Override
    public void move(int screenWidth) {

        x += direction * horizontalSpeed;

        if (x <= 0 || x + width >= screenWidth) {
            direction *= -1;

            if (x < 0) {
                x = 0;
            } else if (x + width > screenWidth) {
                x = screenWidth - width;
            }
        }

        // تغییر جهت گاه‌به‌گاه
        if (Math.random() < 0.01) {
            direction *= -1;
        }

        // حرکت عمودی در محدوده‌ی ۱۰۰ پیکسل
        verticalPhase += 0.015;
        y = 40 + Math.sin(verticalPhase) * 50;
    }

}
