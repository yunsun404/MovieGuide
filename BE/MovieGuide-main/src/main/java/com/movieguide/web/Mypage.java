package com.movieguide.web;

import java.io.IOException;
import java.util.List;

import com.movieguide.dao.MypageDAO;
import com.movieguide.dto.GenresDTO;
import com.movieguide.dto.UserDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Mypage
 */
@WebServlet("/mypage")
public class Mypage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public Mypage() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 로그인된 아이디 가져오는 코드 추가 필요
		// 지금은 임시로 지정
		int userNo = 92; 
		
		if (userNo > 0) {
			MypageDAO dao = new MypageDAO();
			UserDTO dto = dao.getUserInfo(userNo);
			List<GenresDTO> genresList = dao.getGenrePreferences(dto.getUserNo());
			
			request.setAttribute("userInfo", dto);
			request.setAttribute("genresList", genresList);
			request.getRequestDispatcher("mypage.jsp").forward(request, response); 
		} // 로그인이 안되어 있다면 로그인페이지로 이동하는 코드 추가 (else)
		else {
	        // 유저 정보가 없을 경우 처리 (예: 로그인 페이지로 리다이렉트)
	        response.sendRedirect("login.jsp");
	    }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
