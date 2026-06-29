package ui;
import main.GameMain;
import manager.DatabaseManager;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class RegisterPanel extends BackgroundPanel{

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    private JButton registerButton;
    private JButton loginButton;
    private JButton backButton;

    private JLabel messageLabel;
    private DatabaseManager db;
    private GameMain frame;

    public RegisterPanel(GameMain frame, DatabaseManager db) {

        super("src/resources/images/welcomeBackground.png");
        this.frame = frame;
        this.db = db;

        initializeComponents();
        addListeners();

    }

    private void initializeComponents(){

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);

        JLabel title = new JLabel("REGISTER");
        title.setFont(new Font("Arial", Font.BOLD,30));
        title.setForeground(Color.WHITE);
        JLabel userLabel = new JLabel("Username");
        userLabel.setForeground(Color.WHITE);
        JLabel passLabel = new JLabel("Password");
        passLabel.setForeground(Color.WHITE);
        JLabel confirmLabel = new JLabel("Confirm Password");
        confirmLabel.setForeground(Color.WHITE);

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        confirmPasswordField = new JPasswordField(15);

        registerButton = new JButton("Register");
        loginButton = new JButton("Login");
        backButton = new JButton("Back");

        //اندازه یکسان دکمه ها
        Dimension buttonSize = new Dimension(120, 30);
        registerButton.setPreferredSize(buttonSize);
        loginButton.setPreferredSize(buttonSize);
        backButton.setPreferredSize(buttonSize);

        messageLabel = new JLabel();
        messageLabel.setVisible(false);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 14));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
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
        add(confirmLabel, gbc);

        gbc.gridy++;
        add(confirmPasswordField, gbc);

        gbc.gridy++;
        add(registerButton, gbc);

        gbc.gridy++;
        add(loginButton, gbc);

        gbc.gridy++;
        add(backButton, gbc);
    }

    private void addListeners() {

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){

                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if(username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
                    messageLabel.setForeground(Color.RED);
                    messageLabel.setText("Please fill in all fields.");
                    messageLabel.setVisible(true);
                    return;
                }
                if(!password.equals(confirmPassword)) {

                    messageLabel.setForeground(Color.RED);
                    messageLabel.setText("Passwords do not match.");
                    messageLabel.setVisible(true);

                    passwordField.setText("");
                    confirmPasswordField.setText("");
                    return;
                }

                try {

                    User user = new User(username, password);
                    db.registerUser(user);

                    messageLabel.setForeground(new Color(1, 175, 1));
                    messageLabel.setText("Registration successful!");
                    messageLabel.setVisible(true);

                    usernameField.setText("");
                    passwordField.setText("");
                    confirmPasswordField.setText("");

                    //اینجا بعد ثبت نام به صفحه لاگین هدایت میکننه چون هنوز صفحه بازی رو درست نکردم
                    Timer timer = new Timer(
                            1000,
                            new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {

                                    frame.showLoginPanel();//یعنی اینجا

                                }
                            });

                    timer.setRepeats(false);
                    timer.start();

                }
                catch (IllegalArgumentException ex) {

                    messageLabel.setForeground(Color.RED);
                    messageLabel.setText(ex.getMessage());
                    messageLabel.setVisible(true);
                }
                catch (SQLException ex) {

                    if(ex.getMessage().contains("UNIQUE")) {

                        messageLabel.setForeground(Color.RED);
                        messageLabel.setText(
                                "Username already exists.");
                    }
                    else {

                        messageLabel.setForeground(Color.RED);
                        messageLabel.setText(
                                "Database error.");
                    }

                    messageLabel.setVisible(true);
                }
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                frame.showLoginPanel();
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
