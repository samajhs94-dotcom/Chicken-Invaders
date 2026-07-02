package main;
import manager.DatabaseManager;
import model.User;
import ui.GamePanel;
import ui.LoginPanel;
import ui.MainMenu;
import ui.RegisterPanel;

import javax.swing.*;
import java.awt.*;

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

    private GamePanel gamePanel;

    private GameMain(){

        db=new DatabaseManager();
        if(!db.connect()){
            JOptionPane.showMessageDialog(null,"Database connection failed!");
            System.exit(0);
        }

        db.createTables();

        initializeFrame();
        initializePanels();
        setVisible(true);

    }

    private void initializeFrame() {

        setTitle("Chicken Invaders");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);//پنجره وسط صفجه باز شه
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);//کاربر نتونه سایز پنجره رو تغییر بده
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);

        gamePanel = new GamePanel();
        mainPanel.add(gamePanel,"GAME");

    }


    //ساخت تمام صفجه های برنامه
    private void initializePanels() {

        mainMenu = new MainMenu(this);
        loginPanel = new LoginPanel(this,db);
        registerPanel = new RegisterPanel(this,db);
        mainPanel.add(mainMenu,"MENU");
        mainPanel.add(loginPanel,"LOGIN");
        mainPanel.add(registerPanel,"REGISTER");
        cardLayout.show(mainPanel,"MENU");//اولین صفحه
    }

    //جابه جایی بین صفحات
    public void showMainMenu() {
        cardLayout.show(mainPanel,"MENU");
    }

    public void showLoginPanel() {
        cardLayout.show(mainPanel,"LOGIN");
    }

    public void showRegisterPanel() {
        cardLayout.show(mainPanel,"REGISTER");
    }

    public void showHighScorePanel() {
        cardLayout.show(mainPanel, "HIGHSCORE");
    }

    public void showSettingsPanel() {
        cardLayout.show(mainPanel, "SETTINGS");
    }

    public void showHowToPlayPanel() {
        cardLayout.show(mainPanel, "HOWTOPLAY");
    }

    public void showGamePanel() {
        cardLayout.show(mainPanel, "GAME");
        gamePanel.requestFocusInWindow();
        gamePanel.setCurrentUser(currentUser);
    }


    //کاربر فعلی
    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
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
