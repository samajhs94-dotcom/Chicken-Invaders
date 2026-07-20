package ui;
import main.GameMain;
import manager.DatabaseManager;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginPanel extends BackgroundPanel{

    private JTextField usernameField;
    private JPasswordField passwordField; // رمز عبور رو از دید بصری پنهان میکنه (..**)
    private JButton loginButton;
    private JButton registerButton;
    private JButton backButton;
    private DatabaseManager db;
    private GameMain frame;
    private JLabel messageLabel;

    public LoginPanel(GameMain frame, DatabaseManager db) {
        super("src/resources/images/FirstBackground.png");
        this.frame = frame;
        this.db = db;
        initializeComponents();
        addListeners();
    }

    private void initializeComponents() {

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);

        JLabel title = new JLabel("LOGIN");
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(new Color(255, 200, 0));
        JLabel userLabel = new JLabel("Username");
        userLabel.setForeground(new Color(255, 200, 0));
        JLabel passLabel = new JLabel("Password");
        passLabel.setForeground(new Color(255, 200, 0));

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        backButton = new JButton("Back");

        styleField(usernameField);
        styleField(passwordField);
        styleButton(loginButton);
        styleButton(registerButton);
        styleButton(backButton);

        //اندازه دکمه ها
        Dimension buttonSize = new Dimension(120, 30);
        loginButton.setPreferredSize(buttonSize);
        registerButton.setPreferredSize(buttonSize);
        backButton.setPreferredSize(buttonSize);

        //چاپ پیام
        messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("Arial", Font.BOLD, 14));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;//ستون 1
        gbc.gridy = 0;//ردیف 1
        add(title, gbc);
        gbc.gridy++;
        add(messageLabel, gbc);
        gbc.gridy++;
        add(userLabel, gbc);
        gbc.gridy++;
        add(usernameField, gbc);
        gbc.gridy++;
        add(passLabel, gbc);
        gbc.gridy++;
        add(passwordField, gbc);
        gbc.gridy++;
        add(loginButton, gbc);
        gbc.gridy++;
        add(registerButton, gbc);
        gbc.gridy++;
        add(backButton, gbc);

    }

    private void styleButton(JButton button) {

        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(new Color(255, 200, 0));
        button.setBackground(new Color(30, 30, 35));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);

        button.setBorder(BorderFactory.createLineBorder(new Color(255, 200, 0), 2));

    }

    private void styleField(JTextField field) {

        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setForeground(Color.WHITE);
        field.setBackground(new Color(30, 30, 35));
        field.setCaretColor(Color.WHITE);
        field.setSelectionColor(new Color(255, 200, 0));
        field.setSelectedTextColor(Color.BLACK);

        field.setBorder(BorderFactory.createLineBorder(new Color(255, 200, 0), 2));

    }

    private void addListeners() {

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    messageLabel.setForeground(Color.RED);
                    messageLabel.setText("Please fill in all fields.");
                    messageLabel.setVisible(true);
                    return;
                }

                try {

                    User user = db.login(username, password);
                    if (user != null) {

                        messageLabel.setForeground(new Color(0,170,0));
                        messageLabel.setText("Login successful!");
                        messageLabel.setVisible(true);

                        // مثلا کاربر جاری
                        frame.setCurrentUser(user);

                        //پیام لاگین که با موفقیت بود رو یه ثانیه نشون بده
                        Timer timer = new Timer(1000, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                frame.showGamePanel();                            }
                        });
                        timer.setRepeats(false);
                        timer.start();

                    } else {

                        messageLabel.setForeground(Color.RED);
                        messageLabel.setText("Username or password is incorrect.");
                        messageLabel.setVisible(true);
                        passwordField.setText("");

                    }

                } catch (SQLException ex) {
                    System.out.println("❌ Database Error: " + ex.getMessage());

                    messageLabel.setForeground(Color.RED);
                    messageLabel.setText("Database error!");
                    messageLabel.setVisible(true);

                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.showRegisterPanel();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.showMainMenu();
            }
        });
    }


}
