package enemy;

import javax.swing.*;

public class BossLevel8 extends Boss{

    private double verticalPhase = -Math.PI / 2;
    private double acceleration = 0.003;
    private double baseY;

    public BossLevel8(int startX, int startY) {

        super(startX, startY, 100, 2.0, 1000, 8,
               5,new ImageIcon("src/resources/images/boss2.png").getImage());

        width = 200;
        height = 200;
        baseY = startY;

    }

    @Override
    public void move(int screenWidth) {

        // کم و زیاد شدن آرام سرعت
        horizontalSpeed += acceleration;

        if (horizontalSpeed >= 2.5) {
            horizontalSpeed = 2.5;
            acceleration = -0.003;
        }

        if (horizontalSpeed <= 1.5) {
            horizontalSpeed = 1.5;
            acceleration = 0.003;
        }

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

        // تغییر جهت گاه‌به‌گاه
        if (Math.random() < 0.01) {
            direction *= -1;
        }

        // حرکت عمودی در محدوده‌ی ۱۰۰ پیکسل
        verticalPhase += 0.015;
        y = baseY + (Math.sin(verticalPhase)+1) * 50;
    }

}
