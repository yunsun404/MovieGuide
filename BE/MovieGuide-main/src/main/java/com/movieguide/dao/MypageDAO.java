package com.movieguide.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.movieguide.dto.GenresDTO;
import com.movieguide.dto.UserDTO;

public class MypageDAO {
	// 1. 유저 정보 읽어오기
	public UserDTO getUserInfo(String user_id) {
		UserDTO dto = null;
		String sql = "select * from login where user_id=?";
		try (Connection conn = DBConnection.getInstance().getConn();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				) {
			
			pstmt.setString(1, user_id);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				dto = new UserDTO();
				dto.setUserNo(rs.getInt("user_no"));
				dto.setUserID(rs.getString("user_id"));
				dto.setUserPW(rs.getString("user_pw"));
				dto.setUserEmail(rs.getString("user_email"));
			}
		} catch (Exception e) {
			
		}
		return dto;
	}
	
	// 2. 장르 선호도 읽어오기
	public List<GenresDTO> getGenrePreferences(int userNo) {
	    List<GenresDTO> list = new ArrayList<>();
	    String sql = "SELECT g.genres_no, g.genres_id, g.genres_like, gn.genres_name "
	    		+ "	    FROM genres g JOIN genres_name gn ON g.genres_id = gn.genres_id"
	    		+ "	    WHERE g.user_no = ?";
	    
	    
	    try (Connection conn = DBConnection.getInstance().getConn();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        pstmt.setInt(1, userNo);
	        ResultSet rs = pstmt.executeQuery();
	        
	        while(rs.next()) {
	            GenresDTO dto = new GenresDTO();
	            dto.setGenresNo(rs.getInt("genre_no"));
	            dto.setGenresID(rs.getInt("genre_id"));
	            dto.setGenresLike(rs.getInt("genre_like"));
	            dto.setGenresName(rs.getString("genre_name"));
	            list.add(dto);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return list;
	}
	
	// 3. 장르 업데이트 (기존 데이터 삭제 후 일괄 삽입)
	public int updateGenrePreferences(int userNo, String[] likes, String[] dislikes) {
	    Connection conn = null;
	    String deleteSql = "DELETE FROM genres WHERE user_no = ?";
	    String insertSql = "INSERT INTO genres (user_no, genres_id, genres_like) VALUES (?, ?, ?)";

	    try {
	        conn = DBConnection.getInstance().getConn();
	        conn.setAutoCommit(false); // 트랜잭션 시작 (원자성 확보)

	        // [Step 1] 기존 선호/비선호 장르 모두 삭제
	        try (PreparedStatement dPstmt = conn.prepareStatement(deleteSql)) {
	            dPstmt.setInt(1, userNo);
	            dPstmt.executeUpdate();
	        }

	        // [Step 2] 새로운 데이터 삽입
	        try (PreparedStatement iPstmt = conn.prepareStatement(insertSql)) {
	            // 선호 장르 처리 (like = 1)
	            if (likes != null) {
	                for (String id : likes) {
	                    iPstmt.setInt(1, userNo);
	                    iPstmt.setInt(2, Integer.parseInt(id));
	                    iPstmt.setInt(3, 1);
	                    iPstmt.addBatch();
	                }
	            }
	            // 싫어하는 장르 처리 (like = 0)
	            if (dislikes != null) {
	                for (String id : dislikes) {
	                    iPstmt.setInt(1, userNo);
	                    iPstmt.setInt(2, Integer.parseInt(id));
	                    iPstmt.setInt(3, 0);
	                    iPstmt.addBatch();
	                }
	            }
	            iPstmt.executeBatch(); // 한꺼번에 실행
	        }

	        conn.commit(); // 모든 쿼리 성공 시 DB 반영
	        return 1;
	    } catch (Exception e) {
	        try { if(conn != null) conn.rollback(); } catch (Exception ex) {} // 에러 발생 시 되돌리기
	        e.printStackTrace();
	        return 0;
	    }
	}

}
