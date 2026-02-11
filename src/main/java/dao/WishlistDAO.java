package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import util.DB;

public class WishlistDAO {

    // 저장 성공 시 1 리턴
    public int insert(int userNo, int movieId) throws Exception {
        String sql = "INSERT INTO wishlist(user_no, movie_id) VALUES (?, ?)";

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userNo);
            ps.setInt(2, movieId);
            return ps.executeUpdate();
        }
    }

    // 중복(UNIQUE user_no, movie_id)일 때 예쁘게 처리하고 싶으면 이걸 쓰면 됨
    public int insertIgnoreDuplicate(int userNo, int movieId) throws Exception {
        try {
            return insert(userNo, movieId);
        } catch (SQLIntegrityConstraintViolationException dup) {
            return 0; // 이미 있으면 0으로 처리
        }
    }
    public int deleteByUserAndMovie(int userNo, int movieId) throws Exception {
        String sql = "DELETE FROM wishlist WHERE user_no=? AND movie_id=?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userNo);
            ps.setInt(2, movieId);
            return ps.executeUpdate(); // 1이면 삭제됨, 0이면 원래 없던 것
            
        }
    }
    public List<Integer> selectMovieIdsByUser(int userNo) throws Exception {
        String sql = "SELECT movie_id FROM wishlist WHERE user_no=? ORDER BY wish_date DESC";
        List<Integer> list = new ArrayList<>();

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userNo);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(rs.getInt("movie_id"));
            }
        }
        return list;
    }
}