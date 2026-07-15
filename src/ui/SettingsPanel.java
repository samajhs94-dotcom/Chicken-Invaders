package ui;

import main.GameMain;
import manager.DatabaseManager;
import manager.SoundManager;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsPanel extends BackgroundPanel{


    private GameMain frame;
    private DatabaseManager databaseManager;
    private SoundManager soundManager;
    private JCheckBox musicCheckBox;
    private JCheckBox shotCheckBox;
    private JCheckBox explosionCheckBox;
    private JCheckBox gameOverCheckBox;
    private JButton saveButton;
    private JButton backButton;
    private JLabel messageLabel;

    public SettingsPanel(GameMain frame, DatabaseManager databaseManager, SoundManager soundManager) {

        super("src/resources/images/FirstBackground.png");
        this.frame = frame;
        this.databaseManager = databaseManager;
        this.soundManager = soundManager;
        initializeComponents();
        addListeners();

    }

    private void initializeComponents() {

        setLayout(new GridBagLayout());

        JPanel settingsBox = new JPanel(new GridLayout(7, 1, 10, 10));

        settingsBox.setBackground(new Color(20, 20, 20));

        settingsBox.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(255, 200, 0), 2),
                        BorderFactory.createEmptyBorder(25, 40, 25, 40)));

        JLabel titleLabel = new JLabel("SOUND SETTINGS", SwingConstants.CENTER);

        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(255, 200, 0));

        musicCheckBox = new JCheckBox("Background Music");
        shotCheckBox = new JCheckBox("Shot Sound");
        explosionCheckBox = new JCheckBox("Explosion Sound");
        gameOverCheckBox = new JCheckBox("Game Over / Win Sound");

        styleCheckBox(musicCheckBox);
        styleCheckBox(shotCheckBox);
        styleCheckBox(explosionCheckBox);
        styleCheckBox(gameOverCheckBox);

        messageLabel = new JLabel(" ", SwingConstants.CENTER);

        messageLabel.setFont(new Font("Arial", Font.BOLD, 14));

        saveButton = new JButton("Save");
        backButton = new JButton("Back");

        JPanel buttonPanel = new JPanel();

        buttonPanel.setOpaque(false);
        buttonPanel.add(saveButton);
        buttonPanel.add(backButton);

        settingsBox.add(titleLabel);
        settingsBox.add(musicCheckBox);
        settingsBox.add(shotCheckBox);
        settingsBox.add(explosionCheckBox);
        settingsBox.add(gameOverCheckBox);
        settingsBox.add(messageLabel);
        settingsBox.add(buttonPanel);

        add(settingsBox);
    }

    private void styleCheckBox(JCheckBox checkBox) {

        checkBox.setFont(new Font("Arial", Font.BOLD, 16));
        checkBox.setForeground(Color.WHITE);
        checkBox.setBackground(new Color(20, 20, 20));
        checkBox.setFocusPainted(false);

    }

    private void addListeners() {

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveSettings();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.showMainMenu();
            }
        });
    }

    private void saveSettings() {

        User user = frame.getCurrentUser();

        if (user == null) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Please login first!");
            return;
        }

        user.setMusicEnabled(musicCheckBox.isSelected());
        user.setShotSoundEnabled(shotCheckBox.isSelected());
        user.setExplosionSoundEnabled(explosionCheckBox.isSelected());
        user.setGameOverSoundEnabled(gameOverCheckBox.isSelected());

        soundManager.setSoundSettings(user.getMusicEnabled(), user.getShotSoundEnabled(),
                user.getExplosionSoundEnabled(), user.getGameOverSoundEnabled());

        databaseManager.updateSoundSettings(user);
        messageLabel.setForeground(Color.GREEN);
        messageLabel.setText("Settings saved!");
    }

    public void loadSettings(User user) {

        if (user == null) {
            return;
        }

        musicCheckBox.setSelected(user.getMusicEnabled());
        shotCheckBox.setSelected(user.getShotSoundEnabled());
        explosionCheckBox.setSelected(user.getExplosionSoundEnabled());
        gameOverCheckBox.setSelected(user.getGameOverSoundEnabled());
        messageLabel.setText(" ");

    }

}
