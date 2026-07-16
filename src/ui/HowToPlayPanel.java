package ui;

import main.GameMain;

import javax.swing.*;
import java.awt.*;

public class HowToPlayPanel extends BackgroundPanel {

    private GameMain frame;

    public HowToPlayPanel(GameMain frame) {

        super("src/resources/images/FirstBackground.png");
        this.frame = frame;
        initializeComponents();

    }

    private void initializeComponents() {

        setLayout(new BorderLayout(20, 25));
        setBorder(BorderFactory.createEmptyBorder(35, 100, 35, 100));

        JLabel titleLabel = new JLabel("HOW TO PLAY", SwingConstants.CENTER);

        titleLabel.setFont(new Font("Arial", Font.BOLD, 34));
        titleLabel.setForeground(new Color(255, 200, 0));

        JPanel controlsBox = new JPanel(new GridLayout(6, 1, 8, 8));
        controlsBox.setBackground(new Color(15, 15, 20));

        controlsBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 200, 0), 2),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)));

        controlsBox.add(createControlRow("MOVE", "Arrow Keys or W A S D"));
        controlsBox.add(createControlRow("SHOOT", "SPACE"));
        controlsBox.add(createControlRow("PAUSE / RESUME", "P"));
        controlsBox.add(createControlRow("RETURN TO MENU", "ESC"));
        controlsBox.add(createControlRow("MISSION", "Destroy all chickens and bosses"));
        controlsBox.add(createControlRow("POWER UPS", "Collect falling power-ups"));

        JButton backButton = new JButton("BACK");
        backButton.setFont(new Font("Arial", Font.BOLD, 15));
        backButton.setPreferredSize(new Dimension(140, 42));
        backButton.setBackground(new Color(255, 200, 0));
        backButton.setForeground(Color.BLACK);
        backButton.setFocusPainted(false);

        backButton.addActionListener(e -> frame.showMainMenu());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(backButton);

        add(titleLabel, BorderLayout.NORTH);
        add(controlsBox, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createControlRow(String title, String description) {

        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(new Color(30, 30, 35));

        row.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));
        titleLabel.setForeground(new Color(255, 200, 0));

        JLabel descriptionLabel = new JLabel(description, SwingConstants.RIGHT);

        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 15));

        descriptionLabel.setForeground(Color.WHITE);

        row.add(titleLabel, BorderLayout.WEST);
        row.add(descriptionLabel, BorderLayout.CENTER);

        return row;

    }

}
