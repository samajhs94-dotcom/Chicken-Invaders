package main;
import manager.DatabaseManager;
import model.User;
import ui.*;
import manager.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameMain extends JFrame {

    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private DatabaseManager db;
    private User currentUser;
    private MainMenu mainMenu;
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private SoundManager soundManager;
    private SettingsPanel settingsPanel;
    private HighScorePanel highScorePanel;
    private HowToPlayPanel howToPlayPanel;
    private StorePanel storePanel;

    private GamePanel gamePanel;

    private GameMain(){

        db=new DatabaseManager();
        if(!db.connect()){
            JOptionPane.showMessageDialog(null,"Database connection failed!");
            System.exit(0);
        }

        db.createTables();
        soundManager = new SoundManager();

        initializeFrame();
        initializePanels();
        setVisible(true);
        soundManager.playBackgroundMusic();

    }

    private void initializeFrame() {

        setTitle("Chicken Invaders");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);//پنجره وسط صفجه باز شه

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                db.close();
            }
        });

        setResizable(false);//کاربر نتونه سایز پنجره رو تغییر بده
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);

        gamePanel = new GamePanel(db,this,soundManager);
        mainPanel.add(gamePanel,"GAME");

    }


    //ساخت تمام صفجه های برنامه
    private void initializePanels() {

        mainMenu = new MainMenu(this);
        loginPanel = new LoginPanel(this,db);
        registerPanel = new RegisterPanel(this,db);
        settingsPanel = new SettingsPanel(this, db, soundManager);
        highScorePanel = new HighScorePanel(this, db);
        howToPlayPanel = new HowToPlayPanel(this);
        storePanel = new StorePanel(this, db);

        mainPanel.add(mainMenu,"MENU");
        mainPanel.add(loginPanel,"LOGIN");
        mainPanel.add(registerPanel,"REGISTER");
        mainPanel.add(settingsPanel, "SETTINGS");
        mainPanel.add(highScorePanel, "HIGHSCORE");
        mainPanel.add(howToPlayPanel, "HOWTOPLAY");
        mainPanel.add(storePanel, "STORE");

        cardLayout.show(mainPanel,"MENU");//اولین صفحه

    }

    //جابه جایی بین صفحات
    public void showMainMenu() {
        gamePanel.stopGame();
        soundManager.stopEndSounds();
        soundManager.playBackgroundMusic();
        cardLayout.show(mainPanel,"MENU");
    }

    public void showLoginPanel() {
        gamePanel.stopGame();
        cardLayout.show(mainPanel,"LOGIN");
    }

    public void showRegisterPanel() {
        gamePanel.stopGame();
        cardLayout.show(mainPanel,"REGISTER");
    }

    public void showHighScorePanel() {
        gamePanel.stopGame();
        highScorePanel.loadScores();
        cardLayout.show(mainPanel, "HIGHSCORE");
    }

    public void showSettingsPanel() {
        gamePanel.stopGame();
        if (currentUser == null) {
            showLoginPanel();
            return;
        }
        settingsPanel.loadSettings(currentUser);
        cardLayout.show(mainPanel, "SETTINGS");
    }

    public void showHowToPlayPanel() {
        gamePanel.stopGame();
        cardLayout.show(mainPanel, "HOWTOPLAY");
    }

    public void showGamePanel() {
        cardLayout.show(mainPanel, "GAME");
        gamePanel.setCurrentUser(currentUser);
        soundManager.playBackgroundMusic();
        gamePanel.startGame();
    }

    public void showStorePanel() {

        gamePanel.stopGame();
        if (currentUser == null) {
            showLoginPanel();
            return;
        }
        storePanel.loadStore();
        cardLayout.show(mainPanel, "STORE");

    }


    //کاربر فعلی
    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {

        this.currentUser = currentUser;

        if (currentUser != null) {
            soundManager.setSoundSettings(currentUser.getMusicEnabled(),
                    currentUser.getShotSoundEnabled(),
                    currentUser.getExplosionSoundEnabled(),
                    currentUser.getGameOverSoundEnabled());
        }

    }

    public DatabaseManager getDb() {
        return db;
    }


    //نقطه شروع برنامه
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new GameMain();
        });

    }


}
