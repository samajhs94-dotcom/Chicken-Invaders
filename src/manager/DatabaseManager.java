package manager;
import model.User;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:game.db";
    private Connection connection;

    //میخوایم کانکشن در کل برنامه باز بمونه
    public boolean connect() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Connected to database successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to connect to database." + e.getMessage());
            return false;
        }
    }

    public void createTables() {

        String usersTable = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT UNIQUE NOT NULL, "
                + "password TEXT NOT NULL, "
                + "high_score INTEGER DEFAULT 0, "
                + "last_level INTEGER DEFAULT 1, "
                + "music_enabled BOOLEAN DEFAULT TRUE, "
                + "shot_sound_enabled BOOLEAN DEFAULT TRUE, "
                + "explosion_sound_enabled BOOLEAN DEFAULT TRUE, "
                + "game_over_sound_enabled BOOLEAN DEFAULT TRUE"
                + ");";

        String gameHistoryTable = "CREATE TABLE IF NOT EXISTS game_history ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "user_id INTEGER NOT NULL, "
                + "score INTEGER NOT NULL, "
                + "level INTEGER NOT NULL, "
                + "game_date DATETIME DEFAULT CURRENT_TIMESTAMP, "
                + "music_enabled BOOLEAN DEFAULT TRUE, "
                + "shot_sound_enabled BOOLEAN DEFAULT TRUE, "
                + "explosion_sound_enabled BOOLEAN DEFAULT TRUE, "
                + "game_over_sound_enabled BOOLEAN DEFAULT TRUE, "
                + "FOREIGN KEY(user_id) REFERENCES users(id)"
                + ");";

        try {

            Statement statement = connection.createStatement();
            statement.execute(usersTable);
            statement.execute(gameHistoryTable);
            statement.close();
            System.out.println("Tables created successfully.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void registerUser(User user) throws SQLException {

        String sql = "INSERT INTO users(username,password,high_score,last_level,music_enabled,"
                + "shot_sound_enabled,explosion_sound_enabled,game_over_sound_enabled)"
                + " VALUES(?,?,?,?,?,?,?,?)";

        String username = user.getUsername();

        if (username == null || username.length() < 3 || username.length() > 15) {
            throw new IllegalArgumentException("Username must be between 3 and 15 characters.");
        }

        if (user.getPassword() == null || user.getPassword().length() < 3) {
            throw new IllegalArgumentException("Password must be at least 3 characters.");
        }

        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setString(1, user.getUsername());
        ps.setString(2, user.getPassword());
        ps.setInt(3, 0);
        ps.setInt(4, 1);
        ps.setBoolean(5, user.getMusicEnabled());
        ps.setBoolean(6, user.getShotSoundEnabled());
        ps.setBoolean(7, user.getExplosionSoundEnabled());
        ps.setBoolean(8, user.getGameOverSoundEnabled());

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();

        if(rs.next()){
            user.setId(rs.getInt(1));
        }

        rs.close();
        ps.close();

        user.setHighScore(0);
        user.setLastLevel(1);
    }

    public User login(String username, String password) throws SQLException {

        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        PreparedStatement prstm = connection.prepareStatement(sql);
        prstm.setString(1, username);
        prstm.setString(2, password);

        ResultSet rs = prstm.executeQuery();

        if (rs.next()) {

            User user = new User(rs.getString("username"), rs.getString("password"));
            user.setId(rs.getInt("id"));
            user.setHighScore(rs.getInt("high_score"));
            user.setLastLevel(rs.getInt("last_level"));
            user.setMusicEnabled(rs.getBoolean("music_enabled"));
            user.setShotSoundEnabled(rs.getBoolean("shot_sound_enabled"));
            user.setExplosionSoundEnabled(rs.getBoolean("explosion_sound_enabled"));
            user.setGameOverSoundEnabled(rs.getBoolean("game_over_sound_enabled"));
            rs.close();
            prstm.close();
            return user;
        }
        rs.close();
        prstm.close();
        return null;
    }


    public void saveGame(User user, int score, int level, boolean musicEnabled, boolean shotSoundEnabled,
                         boolean explosionSoundEnabled, boolean gameOverSoundEnabled) {


        if (user == null) {
            return;
        }

        try {
            //اپدیت خلاصه وضعیت کاربر و ذخیره رکورد کامل بازی
            connection.setAutoCommit(false);

            //ذخیره در هیستوری
            String sql = "INSERT INTO game_history(user_id, score, level ,music_enabled,"
            + " shot_sound_enabled, explosion_sound_enabled, game_over_sound_enabled)" +
                    " VALUES (?, ?, ?, ?, ?, ? , ? )";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, user.getId());
            ps.setInt(2, score);
            ps.setInt(3, level);
            ps.setBoolean(4, musicEnabled);
            ps.setBoolean(5, shotSoundEnabled);
            ps.setBoolean(6, explosionSoundEnabled);
            ps.setBoolean(7, gameOverSoundEnabled);
            ps.executeUpdate();
            ps.close();

            //اپدیت خلاصه وضعیت کاربر
            String updateUserSql = "UPDATE users SET "
                    + "high_score = CASE WHEN ? > high_score THEN ? ELSE high_score END, "
                    + "last_level = ?, "
                    + "music_enabled = ?, "
                    + "shot_sound_enabled = ?, "
                    + "explosion_sound_enabled = ?, "
                    + "game_over_sound_enabled = ? "
                    + "WHERE id = ?";

            PreparedStatement updatePs = connection.prepareStatement(updateUserSql);

            //اگر امتیاز جدیدی بیشتر از قبلی باشه اپدیت میشه
            updatePs.setInt(1, score);
            updatePs.setInt(2, score);

            updatePs.setInt(3, level);
            updatePs.setBoolean(4, musicEnabled);
            updatePs.setBoolean(5, shotSoundEnabled);
            updatePs.setBoolean(6, explosionSoundEnabled);
            updatePs.setBoolean(7, gameOverSoundEnabled);
            updatePs.setInt(8, user.getId());

            updatePs.executeUpdate();
            updatePs.close();

            // اگر همه چیز موفق بود، تغییرات در دیتابیس ثبت نهایی می‌شود
            connection.commit();

            //  اتصال دیتابیس را به حالت عادی برمی‌گردانی
            connection.setAutoCommit(true);

            //اپدیت ابجکت یوزر همراه با دیتابیس
            if (score > user.getHighScore()) {
                user.setHighScore(score);
            }
            user.setLastLevel(level);

            user.setMusicEnabled(musicEnabled);
            user.setShotSoundEnabled(shotSoundEnabled);
            user.setExplosionSoundEnabled(explosionSoundEnabled);
            user.setGameOverSoundEnabled(gameOverSoundEnabled);

        }
        catch (SQLException e) {
            // نوت: اگر خطا رخ داد، تغییرات نصفه‌نیمه برگردانده می‌شود
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException rollbackException) {
                System.out.println("Rollback error: " + rollbackException.getMessage());
            }
            System.out.println("Save game error: " + e.getMessage());
        }

    }


    public void updateSoundSettings(User user) {

        String sql = "UPDATE users SET "
                + "music_enabled=?, "
                + "shot_sound_enabled=?, "
                + "explosion_sound_enabled=?, "
                + "game_over_sound_enabled=? "
                + "WHERE id=?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setBoolean(1, user.getMusicEnabled());
            ps.setBoolean(2, user.getShotSoundEnabled());
            ps.setBoolean(3, user.getExplosionSoundEnabled());
            ps.setBoolean(4, user.getGameOverSoundEnabled());
            ps.setInt(5, user.getId());

            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            System.out.println("Update sound settings error: " + e.getMessage());
         }

    }

    //بستن اتصال دیتابیس هنگام خروج از برنامه
    public void close() {

        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
    }

    public ArrayList<Object[]> getHighScores() {

        ArrayList<Object[]> scores = new ArrayList<>();
        ArrayList<Integer> addedUserIds = new ArrayList<>();

        String sql = "SELECT users.id, users.username, game_history.score, "
                + "game_history.level, game_history.game_date "
                + "FROM game_history "
                + "JOIN users ON users.id = game_history.user_id "
                + "ORDER BY game_history.score DESC, game_history.id DESC";

        try {

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {

                int userId = resultSet.getInt("id");

                // از هر کاربر فقط بالاترین امتیاز نمایش داده می‌شود
                if (addedUserIds.contains(userId)) {
                    continue;
                }

                addedUserIds.add(userId);

                Object[] scoreData = {
                        resultSet.getString("username"),
                        resultSet.getInt("score"),
                        resultSet.getInt("level"),
                        resultSet.getString("game_date")
                };

                scores.add(scoreData);
            }

            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            System.out.println("High scores error: " + e.getMessage());
        }

        return scores;
    }

}

