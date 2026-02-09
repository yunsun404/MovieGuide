package dao;

import dto.UserDTO;
import java.sql.*;

public class UserDAO {

    private static final String URL =
            "jdbc:mariadb://db.wisejia.com:3306/movieguide";

    private static final String USER = "movieguide";
    private static final String PASSWORD = "MovieGuide";


    private Connection getConnection() throws Exception {
        Class.forName("org.mariadb.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


    /* =========================
       ⭐ 회원가입 (PK 반환)
    ========================= */
    public int signup(UserDTO user) {

        String sql =
            "INSERT INTO login(user_id, user_email, user_pw) VALUES(?,?,?)";

        try (Connection conn = getConnection();
             PreparedStatement ps =
                conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getUserId());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());

            int result = ps.executeUpdate();

            if(result > 0){
                ResultSet rs = ps.getGeneratedKeys();
                if(rs.next()){
                    return rs.getInt(1);   // ⭐ user_no 반환
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }



    /* =========================
       로그인
    ========================= */
    public UserDTO login(UserDTO user) {

        String sql =
            "SELECT user_no, user_id, user_email, user_pw FROM login WHERE user_email=?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getEmail());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String dbPw = rs.getString("user_pw");

                if (dbPw.trim().equals(user.getPassword().trim())) {

                    return new UserDTO(
                            rs.getInt("user_no"),
                            rs.getString("user_id"),
                            rs.getString("user_email"),
                            dbPw
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
