package com.movieguide.web;

import java.io.IOException;

import com.movieguide.dao.MypageDAO;

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
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("./mypage.jsp");
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 세션에서 로그인한 유저 정보 가져오기
        // int userNo = (int) request.getSession().getAttribute("userNo");
		// 일단 테스트를 위해서 1
        int userNo = 1; 
        
        if (userNo <= 0) {
            // 로그인 세션이 없으면 로그인 페이지로 보냄
            response.sendRedirect("./login.jsp");
            return;
        }

        // 2. JS에서 생성한 hidden input 값 배열로 받기
        String[] likes = request.getParameterValues("likeGenres");
        String[] dislikes = request.getParameterValues("dislikeGenres");

        // 3. DAO 호출하여 DB 업데이트
        MypageDAO dao = new MypageDAO();
        int result = dao.updateGenrePreferences(userNo, likes, dislikes);

        // 4. 결과에 따른 페이지 이동
        if (result > 0) {
            response.sendRedirect("./mypage.jsp?status=updated");
        } else {
            response.sendRedirect("./editGenres.jsp?error=true");
        }
	}

}
