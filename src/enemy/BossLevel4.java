package enemy;

import javax.swing.*;

public class BossLevel4 extends Boss {


    private double verticalPhase = 0;
    private double baseY;


    public BossLevel4(int startX, int startY) {

        super(startX, startY, 50, 1.5, 1500, 4,
             4,new ImageIcon("src/resources/images/boss1.png").getImage());

        baseY = startY;

    }

    @Override
    public void move(int screenWidth) {

        //حرکت افقی
        x += direction * horizontalSpeed;

        //تغییر جهت هنگام رسیدن به لبه
        if (x <= 0 || x + width >= screenWidth) {
            direction *= -1;

            // جلوگیری از خارج شدن باس از صفحه
            if (x < 0) {
                x = 0;
            } else if (x + width > screenWidth) {
                x = screenWidth - width;
            }
        }

        // حرکت عمودی آروم با دامنه‌ی کوچیک
        verticalPhase += 0.02;
        y = baseY + Math.sin(verticalPhase) * 20;
    }

}
