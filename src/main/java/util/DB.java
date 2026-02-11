package util;
import java.sql.Connection;
import java.sql.DriverManager;

public class DB {

    private static final String URL  = "jdbc:mariadb://db.wisejia.com:3306/movieguide";
    private static final String USER = "movieguide";
    private static final String PASS = "MovieGuide";

    // 🔑 핵심: MariaDB 드라이버 강제 로딩
    static {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            System.out.println("MariaDB JDBC Driver loaded");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MariaDB JDBC Driver not found", e);
        }
    }

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}