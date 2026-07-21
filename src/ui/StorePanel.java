package ui;

import model.User;
import main.GameMain;
import manager.DatabaseManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StorePanel extends BackgroundPanel{


    private GameMain frame;
    private DatabaseManager databaseManager;

    private JLabel scoreLabel;
    private JLabel messageLabel;

    private JButton defaultButton;
    private JButton fastButton;
    private JButton heavyButton;
    private JButton sniperButton;
    private JButton backButton;

    public StorePanel(GameMain frame, DatabaseManager databaseManager) {

        super("src/resources/images/FirstBackground.png");
        this.frame = frame;
        this.databaseManager = databaseManager;
        initializeComponents();
        addListeners();

    }

    private void initializeComponents() {

        setLayout(new BorderLayout(15, 20));

        setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel titleLabel = new JLabel("PLANE STORE", SwingConstants.CENTER);

        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(255, 200, 0));

        scoreLabel = new JLabel("High Score: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 17));
        scoreLabel.setForeground(Color.WHITE);

        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 14));
        messageLabel.setForeground(new Color(255, 200, 0));

        JPanel topPanel = new JPanel(new GridLayout(3, 1, 0, 5));

        topPanel.setOpaque(false);
        topPanel.add(titleLabel);
        topPanel.add(scoreLabel);
        topPanel.add(messageLabel);

        defaultButton = new JButton("SELECT");
        fastButton = new JButton("SELECT");
        heavyButton = new JButton("SELECT");
        sniperButton = new JButton("SELECT");

        JPanel planesPanel = new JPanel(new GridLayout(1, 4, 12, 0));

        planesPanel.setOpaque(false);

        planesPanel.add(createPlaneCard(
                "DEFAULT", "src/resources/images/defaultPlane.png",
                "Speed: 5", "Fire Rate: 300 ms",
                "Lives: 3", "FREE", defaultButton));

        planesPanel.add(createPlaneCard(
                "FAST", "src/resources/images/fastPlane.png",
                "Speed: 7", "Fire Rate: 250 ms",
                "Lives: 3", "5000 POINTS", fastButton));

        planesPanel.add(createPlaneCard(
                "HEAVY", "src/resources/images/HeavyPlane.png",
                "Speed: 4", "Fire Rate: 200 ms",
                "Lives: 5", "8000 POINTS", heavyButton));

        planesPanel.add(createPlaneCard(
                "SNIPER", "src/resources/images/SniperPlane.png",
                "Speed: 5", "Fire Rate: 150 ms",
                "Lives: 3 | Boss Damage: x2", "10000 POINTS", sniperButton));

        backButton = new JButton("BACK");
        styleButton(backButton);

        backButton.setPreferredSize(new Dimension(140, 40));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.add(backButton);

        add(topPanel, BorderLayout.NORTH);
        add(planesPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createPlaneCard(String planeName, String imagePath,
            String firstInfo, String secondInfo, String thirdInfo,
            String price, JButton selectButton) {

        JPanel card = new JPanel(new BorderLayout(5, 8));

        card.setBackground(new Color(20, 20, 25));

        card.setBorder(BorderFactory.createLineBorder(
                new Color(255, 200, 0), 2));

        JLabel nameLabel = new JLabel(planeName, SwingConstants.CENTER);

        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setForeground(new Color(255, 200, 0));

        ImageIcon originalImage = new ImageIcon(imagePath);

        Image resizedImage = originalImage.getImage().getScaledInstance(
                90, 90, Image.SCALE_SMOOTH);

        JLabel imageLabel = new JLabel(new ImageIcon(resizedImage), SwingConstants.CENTER);
        JLabel firstInfoLabel = createInfoLabel(firstInfo);
        JLabel secondInfoLabel = createInfoLabel(secondInfo);
        JLabel thirdInfoLabel = createInfoLabel(thirdInfo);
        JLabel priceLabel = new JLabel(price, SwingConstants.CENTER);

        priceLabel.setFont(new Font("Arial", Font.BOLD, 13));
        priceLabel.setForeground(new Color(255, 200, 0));

        styleButton(selectButton);

        JPanel informationPanel = new JPanel(
                new GridLayout(5, 1, 0, 4));

        informationPanel.setOpaque(false);

        informationPanel.add(firstInfoLabel);
        informationPanel.add(secondInfoLabel);
        informationPanel.add(thirdInfoLabel);
        informationPanel.add(priceLabel);
        informationPanel.add(selectButton);

        card.add(nameLabel, BorderLayout.NORTH);
        card.add(imageLabel, BorderLayout.CENTER);
        card.add(informationPanel, BorderLayout.SOUTH);

        return card;
    }

    private JLabel createInfoLabel(String text) {

        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 13));
        label.setForeground(Color.WHITE);
        return label;

    }

    private void styleButton(JButton button) {

        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setForeground(Color.BLACK);
        button.setBackground(new Color(255, 200, 0));
        button.setFocusPainted(false);

    }

    private void addListeners() {

        defaultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectPlane("DEFAULT", 1, 0);
            }
        });

        fastButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectPlane("FAST", 2, 5000);
            }
        });

        heavyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectPlane("HEAVY", 4, 8000);
            }
        });

        sniperButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectPlane("SNIPER", 3, 10000);
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.showMainMenu();
            }
        });

    }

    private void selectPlane(String planeName, int planeType, int requiredScore) {

        User user = frame.getCurrentUser();

        if (user == null) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Please login first!");
            return;
        }

        if (user.getHighScore() < requiredScore) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Your high score is not enough!");
            return;
        }

        databaseManager.updateSelectedPlane(user, planeType);

        loadStore();

        messageLabel.setForeground(new Color(0, 200, 0));
        messageLabel.setText(planeName + " plane selected!");

    }

    public void loadStore() {

        User user = frame.getCurrentUser();
        messageLabel.setText(" ");

        if (user == null) {

            scoreLabel.setText("Please login first");
            defaultButton.setEnabled(false);
            fastButton.setEnabled(false);
            heavyButton.setEnabled(false);
            sniperButton.setEnabled(false);
            return;

        }

        scoreLabel.setText("High Score: " + user.getHighScore());

        updateButton(defaultButton, 1, 0, user);
        updateButton(fastButton, 2, 5000, user);
        updateButton(heavyButton, 4, 8000, user);
        updateButton(sniperButton, 3, 10000, user);


    }

    private void updateButton(JButton button, int planeType,
                              int requiredScore, User user) {

        if (user.getSelectedPlane() == planeType) {
            button.setText("SELECTED");
            button.setEnabled(false);
        }
        else if (user.getHighScore() >= requiredScore) {
            button.setText("SELECT");
            button.setEnabled(true);
        }
        else {
            button.setText("LOCKED");
            button.setEnabled(false);
        }

    }


}
