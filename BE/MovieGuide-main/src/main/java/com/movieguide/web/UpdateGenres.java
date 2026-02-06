package com.movieguide.web;

import java.io.IOException;
import java.util.List;

import com.movieguide.dao.MypageDAO;
import com.movieguide.dto.GenresDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UpdateGenres
 */
@WebServlet("/updateGenres")
public class UpdateGenres extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public UpdateGenres() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 장르 종류 불러오기
		MypageDAO dao = new MypageDAO();
        List<GenresDTO> allGenres = dao.getAllGenreNames(); 
        
        // 유저가 선택했던 것들 반영해서 보여주기
        // 테스트용 (나중에 세션으로 변경)
        int userNo = 92; 
        List<GenresDTO> userGenres = dao.getGenrePreferences(userNo);
        
        request.setAttribute("allGenres", allGenres);
        request.setAttribute("userGenres", userGenres);
        request.getRequestDispatcher("updateGenres.jsp").forward(request, response);
        
        // System.out.println("가져온 기존 장르 개수: " + (userGenres != null ? userGenres.size() : "null"));
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 세션에서 로그인한 유저 정보 가져오기
        // Integer userNo = (Integer) session.getAttribute("userNo");
		// 일단 테스트를 위해서 1
        int userNo = 92; 
        
        if (userNo <= 0) { // 추후 세션 받는걸로 수정하면 null 인지 아닌지 확인으로 변경
            response.sendRedirect("./login.jsp");
            return;
        }

        // 2. js에서 생성한 값 가져오기
        String[] likes = request.getParameterValues("likeGenres");
        String[] dislikes = request.getParameterValues("dislikeGenres");
        String[] neutrals = request.getParameterValues("neutralGenres");

        // 3. db에 업데이트
        MypageDAO dao = new MypageDAO();
        int result = dao.updateGenrePreferences(userNo, likes, dislikes, neutrals);

        // 4. 다시 마이페이지로 이동
        if (result > 0) {
            response.sendRedirect("./mypage");
        } else {
            response.sendRedirect("./updateGenres?error=true");
        }
	}
}
