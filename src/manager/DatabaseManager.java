package manager;
import model.User;
import java.sql.*;

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
            System.out.println("Failed to connect to database.");
            e.printStackTrace();
            return false;
        }
    }

    public void createTables() {

        String usersTable = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT UNIQUE NOT NULL, "
                + "password TEXT NOT NULL, "
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

        String sql = "INSERT INTO users(username,password,music_enabled,"
                + "shot_sound_enabled,explosion_sound_enabled,"
                + "game_over_sound_enabled)"
                + " VALUES(?,?,?,?,?,?)";

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
        ps.setBoolean(3, user.getMusicEnabled());
        ps.setBoolean(4, user.getShotSoundEnabled());
        ps.setBoolean(5, user.getExplosionSoundEnabled());
        ps.setBoolean(6, user.getGameOverSoundEnabled());

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();

        if(rs.next()){
            user.setId(rs.getInt(1));
        }

        rs.close();
        ps.close();
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
            user.setMusicEnabled(rs.getBoolean("music_enabled"));
            user.setShotSoundEnabled(rs.getBoolean("shot_sound_enabled"));
            user.setExplosionSoundEnabled(rs.getBoolean("explosion_sound_enabled"));
            user.setGameOverSoundEnabled(rs.getBoolean("game_over_sound_enabled"));
            loadGameStats(user);
            rs.close();
            prstm.close();
            return user;
        }
        rs.close();
        prstm.close();
        return null;
    }


    //متد کمکی لاگین
    private void loadGameStats(User user) throws SQLException {

        String sqr= "SELECT MAX(score) FROM game_history WHERE user_id=?";
        String sqr2= "SELECT Max(level) FROM game_history WHERE user_id=?" ;


        // High Score
        PreparedStatement ps1 = connection.prepareStatement(sqr);
        ps1.setInt(1,user.getId());
        ResultSet rs1 = ps1.executeQuery();
        user.setHighScore( rs1.next() ? rs1.getInt(1) : 0);
        rs1.close();
        ps1.close();

        // Last Level
        PreparedStatement ps2 = connection.prepareStatement(sqr2);
        ps2.setInt(1, user.getId());
        ResultSet rs2 = ps2.executeQuery();
        user.setLastLevel(rs2.next() ? rs2.getInt(1) : 1);
        rs2.close();
        ps2.close();


    }

    public void saveGame(User user, int score, int level) {

        try {
            //ذخیره در هیستوری
            String sql = "INSERT INTO game_history(user_id, score, level) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, user.getId());
            ps.setInt(2, score);
            ps.setInt(3, level);
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}

