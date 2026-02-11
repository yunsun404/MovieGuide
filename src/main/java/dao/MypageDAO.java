package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dto.GenresDTO;

public class MypageDAO {
	// 1. 유저 정보 읽어오기
	public dto.UserDTO getUserInfo(int user_no) {
		dto.UserDTO dto = null;
		String sql = "select * from login where user_no=?";
		try (Connection conn = DBConnection.getInstance().getConn();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				) {
			
			pstmt.setInt(1, user_no);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				dto = new dto.UserDTO();
				dto.setUserNo(rs.getInt("user_no"));
				dto.setUserId(rs.getString("user_id"));
				dto.setEmail(rs.getString("user_email"));
			}
		} catch (Exception e) {
			
		}
		return dto;
	}
	
	// 2. 장르 선호도 읽어오기
	public List<dto.GenresDTO> getGenrePreferences(int userNo) {
	    List<dto.GenresDTO> list = new ArrayList<>();
	    String sql = "SELECT g.genres_no, g.genres_id, g.genres_like, gn.genres_name "
	    		+ "	    FROM genres g JOIN genres_name gn ON g.genres_id = gn.genres_id"
	    		+ "	    WHERE g.user_no = ?";
	    
	    
	    try (Connection conn = DBConnection.getInstance().getConn();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        pstmt.setInt(1, userNo);
	        ResultSet rs = pstmt.executeQuery();
	        
	        while(rs.next()) {
	            dto.GenresDTO dto = new dto.GenresDTO();
	            dto.setGenresNo(rs.getInt("genres_no"));
	            dto.setGenresID(rs.getInt("genres_id"));
	            dto.setGenresLike(rs.getInt("genres_like"));
	            dto.setGenresName(rs.getString("genres_name"));
	            list.add(dto);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return list;
	}
	
	// 3. 장르 종류 읽어오기
	public List<dto.GenresDTO> getAllGenreNames() {
	    List<dto.GenresDTO> list = new ArrayList<>();
	    String sql = "SELECT genres_id, genres_name FROM genres_name ORDER BY genres_id ASC";

	    try (Connection conn = DBConnection.getInstance().getConn();
	         PreparedStatement pstmt = conn.prepareStatement(sql);
	         ResultSet rs = pstmt.executeQuery()) {

	        while (rs.next()) {
	            dto.GenresDTO dto = new dto.GenresDTO();
	            dto.setGenresID(rs.getInt("genres_id"));
	            dto.setGenresName(rs.getString("genres_name"));
	            list.add(dto);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return list;
	}
	
	// 4. 장르 업데이트
	public int updateGenrePreferences(int userNo, String[] likes, String[] dislikes, String[] neutrals) {
	    Connection conn = null;
	    String deleteSql = "DELETE FROM genres WHERE user_no = ?";
	    String insertSql = "INSERT INTO genres (user_no, genres_id, genres_like) VALUES (?, ?, ?)";

	    try {
	        conn = DBConnection.getInstance().getConn();
	        conn.setAutoCommit(false); // 트랜잭션 : 끝까지 가면 성공, 중간에 멈추면 되돌리기

	        // 특정 유저의 선호도 모두 삭제
	        try (PreparedStatement dPstmt = conn.prepareStatement(deleteSql)) {
	            dPstmt.setInt(1, userNo);
	            dPstmt.executeUpdate();
	        }

	        // 새로운 데이터 삽입
	        try (PreparedStatement iPstmt = conn.prepareStatement(insertSql)) {
	            // 선호 장르
	            if (likes != null) {
	                for (String id : likes) {
	                    iPstmt.setInt(1, userNo);
	                    iPstmt.setInt(2, Integer.parseInt(id));
	                    iPstmt.setInt(3, 1); // genres_like = 1
	                    iPstmt.addBatch();
	                }
	            }
	            // 싫어하는 장르
	            if (dislikes != null) {
	                for (String id : dislikes) {
	                    iPstmt.setInt(1, userNo);
	                    iPstmt.setInt(2, Integer.parseInt(id));
	                    iPstmt.setInt(3, 2); // genres_like = 2
	                    iPstmt.addBatch();
	                }
	            }
	            // 선택 안한 장르
	            if (neutrals != null) {
	                for (String id : neutrals) {
	                    iPstmt.setInt(1, userNo);
	                    iPstmt.setInt(2, Integer.parseInt(id));
	                    iPstmt.setInt(3, 0); // genres_like = 0
	                    iPstmt.addBatch();
	                }
	            }
	            
	            iPstmt.executeBatch(); // 한꺼번에 실행
	        }
	        conn.commit(); // 모두 성공하면 커밋
	        return 1;
	        
	    } catch (Exception e) {
	        try { if(conn != null) conn.rollback(); } catch (Exception ex) {} // 중간에 실패하면 롤백
	        e.printStackTrace();
	        return 0;
	    } finally {
	    	try {
	            if (conn != null) {
	                conn.setAutoCommit(true); 
	                conn.close();
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}

	public List<GenresDTO> getGenres(int userNo) {
		// TODO Auto-generated method stub
		return null;
	}
	public List<Integer> getLikeGenreIds(int userNo) throws Exception {
	    String sql = "SELECT genres_id FROM genres WHERE user_no=? AND genres_like=1";
	    List<Integer> list = new ArrayList<>();

	    try (Connection conn = util.DB.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setInt(1, userNo);
	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) list.add(rs.getInt("genres_id"));
	        }
	    }
	    return list;
	}
	
	


}
