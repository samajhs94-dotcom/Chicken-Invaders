package enemy;

import javax.swing.*;

public class BossLevel4 extends Boss {


    private double verticalPhase = 0;

    public BossLevel4(int startX, int startY) {
        super(startX, startY, 50, 1.5, 1500, 4,
                new ImageIcon("src/resources/images/boss1.png").getImage());
    }

    @Override
    public void move(int screenWidth) {

        x += direction * horizontalSpeed;

        if (x <= 0 || x + width >= screenWidth) {
            direction *= -1;

            //نگهداشتن دشمن تو صفحه اسکرین بعد از تغییر جهت
            if (x < 0) {
                x = 0;
            } else if (x + width > screenWidth) {
                x = screenWidth - width;
            }
        }

        // حرکت عمودی آروم با دامنه‌ی کوچیک
        verticalPhase += 0.02;
        y = 60 + Math.sin(verticalPhase) * 15;
    }

}
