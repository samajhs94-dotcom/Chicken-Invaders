package ui;

import main.GameMain;
import manager.DatabaseManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class HighScorePanel extends BackgroundPanel  {

    private GameMain frame;
    private DatabaseManager databaseManager;
    private DefaultTableModel tableModel;

    public HighScorePanel(GameMain frame, DatabaseManager databaseManager) {

        super("src/resources/images/FirstBackground.png");
        this.frame = frame;
        this.databaseManager = databaseManager;
        initializeComponents();

    }

    private void initializeComponents() {

        setLayout(new BorderLayout(20, 25));
        setBorder(BorderFactory.createEmptyBorder(35, 70, 35, 70));

        JLabel titleLabel = new JLabel("HIGH SCORES", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 34));
        titleLabel.setForeground(new Color(255, 200, 0));

        String[] columns = {"Username",
                "Score", "Level", "Date"
        };

        tableModel = new DefaultTableModel(columns, 0);

        JTable scoresTable = new JTable(tableModel);

        // تنظیم ظاهر جدول
        scoresTable.setFont(new Font("Arial", Font.BOLD, 15));
        scoresTable.setForeground(Color.WHITE);
        scoresTable.setBackground(new Color(25, 25, 30));
        scoresTable.setSelectionBackground(new Color(255, 200, 0));
        scoresTable.setSelectionForeground(Color.BLACK);
        scoresTable.setGridColor(new Color(70, 70, 75));
        scoresTable.setRowHeight(38);
        scoresTable.setShowVerticalLines(false);
        scoresTable.setIntercellSpacing(new Dimension(0, 1));
        scoresTable.setFillsViewportHeight(true);

        // جلوگیری از تغییر اطلاعات جدول
        scoresTable.setDefaultEditor(Object.class, null);

        // وسط‌چین کردن اطلاعات جدول
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < scoresTable.getColumnCount(); i++) {
            scoresTable.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(centerRenderer);
        }

        // تنظیم اندازه ستون‌ها
        scoresTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        scoresTable.getColumnModel().getColumn(1).setPreferredWidth(90);
        scoresTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        scoresTable.getColumnModel().getColumn(3).setPreferredWidth(190);

        // تنظیم ظاهر عنوان ستون‌ها
        scoresTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        scoresTable.getTableHeader().setBackground(new Color(255, 200, 0));

        scoresTable.getTableHeader().setForeground(Color.BLACK);
        scoresTable.getTableHeader().setOpaque(true);
        scoresTable.getTableHeader().setPreferredSize(
                new Dimension(0, 42));

        JScrollPane scrollPane = new JScrollPane(scoresTable);

        scrollPane.setBorder(BorderFactory.createLineBorder(
                new Color(255, 200, 0), 2));

        scrollPane.getViewport().setBackground(new Color(25, 25, 30));

        // کادر اصلی جدول
        JPanel tableBox = new JPanel(new BorderLayout());
        tableBox.setBackground(new Color(15, 15, 20));

        tableBox.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(255, 200, 0), 2),
                        BorderFactory.createEmptyBorder(12, 12, 12, 12)));

        tableBox.add(scrollPane, BorderLayout.CENTER);

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
        add(tableBox, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // خواندن دوباره امتیازها هنگام باز شدن صفحه
    public void loadScores() {

        tableModel.setRowCount(0);
        ArrayList<Object[]> scores = databaseManager.getHighScores();
        for (Object[] score : scores) {
            tableModel.addRow(score);
        }

    }

}
