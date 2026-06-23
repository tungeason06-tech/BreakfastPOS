package db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConfig {
    private static final String URL = "jdbc:sqlite:pos.db";

    public static Connection getConnection() throws SQLException {
        // 每次呼叫都直接回傳一個新連線
        return DriverManager.getConnection(URL);
    }
}
