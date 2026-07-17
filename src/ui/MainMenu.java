package ui;

import main.GameMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends BackgroundPanel{


    private GameMain frame;
    private JButton newGameButton;
    private JButton highScoreButton;
    private JButton settingsButton;
    private JButton howToPlayButton;
    private JButton exitButton;
    private JLabel messageLabel;
    private JButton storeButton;

    public MainMenu(GameMain frame){
        super("src/resources/images/FirstBackground.png");
        this.frame=frame;
        initializeComponents();
        addListeners();
    }

    private void initializeComponents() {

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12,12,12,12);

        JLabel title = new JLabel("CHICKEN INVADERS");
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(new Color(255, 200, 0));

        messageLabel = new JLabel(" ");
        messageLabel.setForeground(Color.RED);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 14));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        newGameButton = new JButton("New Game");
        highScoreButton = new JButton("High Scores");
        settingsButton = new JButton("Settings");
        howToPlayButton = new JButton("How To Play");
        exitButton = new JButton("Exit");
        storeButton = new JButton("Store");

        styleButton(newGameButton);
        styleButton(highScoreButton);
        styleButton(settingsButton);
        styleButton(howToPlayButton);
        styleButton(exitButton);
        styleButton(storeButton);

        Dimension buttonSize = new Dimension(220,45);

        //اندازه دکمه ها
        newGameButton.setPreferredSize(buttonSize);
        highScoreButton.setPreferredSize(buttonSize);
        settingsButton.setPreferredSize(buttonSize);
        howToPlayButton.setPreferredSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);
        storeButton.setPreferredSize(buttonSize);

        gbc.gridx = 0;
        gbc.gridy = 0;

        add(title,gbc);

        gbc.gridy++;
        add(messageLabel, gbc);

        gbc.gridy++;
        add(newGameButton,gbc);

        gbc.gridy++;
        add(highScoreButton,gbc);

        gbc.gridy++;
        add(settingsButton,gbc);

        gbc.gridy++;
        add(storeButton, gbc);

        gbc.gridy++;
        add(howToPlayButton,gbc);

        gbc.gridy++;
        add(exitButton,gbc);
    }

    private void styleButton(JButton button) {

        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setForeground(new Color(255, 200, 0));
        button.setBackground(new Color(30, 30, 35));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);

        button.setBorder(BorderFactory.createLineBorder(new Color(255, 200, 0), 2));

    }


    private void addListeners() {

        // New Game
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (frame.getCurrentUser() == null) {
                    frame.showLoginPanel();
                }
                else {
                    frame.showGamePanel();
                }
            }
        });

        // High Scores
        highScoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.showHighScorePanel();
            }
        });

        // Settings
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(frame.getCurrentUser() == null){
                    messageLabel.setText("Please login first!");

                    // پاک شدن پیام بعد از ۳ ثانیه
                    Timer timer = new Timer(3000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            messageLabel.setText(" ");
                        }
                    });

                    timer.setRepeats(false);
                    timer.start();
                    return;
                }
                frame.showSettingsPanel();

            }
        });

        // How To Play
        howToPlayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.showHowToPlayPanel();
            }
        });

        // Exit
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getDb().close();
                System.exit(0);
            }
        });

        //store
        storeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (frame.getCurrentUser() == null) {

                    messageLabel.setText("Please login first!");

                    // پاک‌شدن پیام بعد از سه ثانیه
                    Timer timer = new Timer(3000, new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            messageLabel.setText(" ");
                        }
                    });

                    timer.setRepeats(false);
                    timer.start();
                    return;

                }

                frame.showStorePanel();

            }
        });

    }


}
