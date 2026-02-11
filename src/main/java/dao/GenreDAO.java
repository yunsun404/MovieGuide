package dao;

import java.sql.*;
import java.util.*;

public class GenreDAO {

    private static final String URL =
        "jdbc:mariadb://db.wisejia.com:3306/movieguide";
    private static final String USER = "movieguide";
    private static final String PASSWORD = "MovieGuide";

    private Connection getConnection() throws Exception {
        Class.forName("org.mariadb.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // 👍 좋아요 장르
    public List<Integer> getLikeGenres(int userNo) {
        List<Integer> list = new ArrayList<>();

        String sql =
            "SELECT genres_id FROM genres WHERE user_no=? AND genres_like=1";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userNo);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(rs.getInt("genres_id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // 👎 싫어요 장르
    public List<Integer> getHateGenres(int userNo) {
        List<Integer> list = new ArrayList<>();

        String sql =
            "SELECT genres_id FROM genres WHERE user_no=? AND genres_like=2";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userNo);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(rs.getInt("genres_id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}

