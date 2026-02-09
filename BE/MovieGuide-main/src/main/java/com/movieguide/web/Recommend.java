package com.movieguide.web;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.movieguide.dao.MovieAPIHandler;
import com.movieguide.dao.MypageDAO;
import com.movieguide.dto.GenresDTO;
import com.movieguide.dto.MovieDTO;
import com.movieguide.dto.UserDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Recommend
 */
@WebServlet("/recommend")
public class Recommend extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public Recommend() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 로그인된 아이디 가져오는 코드 추가 필요
		// 지금은 임시로 지정
		int userNo = 92; 
		
		if (userNo > 0) {
			MypageDAO dao = new MypageDAO();
			UserDTO dto = dao.getUserInfo(userNo);
			List<GenresDTO> genresList = dao.getGenrePreferences(dto.getUserNo());
			List<GenresDTO> allGenreNames = dao.getAllGenreNames();
			
			// 유저의 장르 문자열 만들기
			// 선호 장르 한개만 충족해도 가져오기
            String likeIds = genresList.stream()
                                .filter(g -> g.getGenresLike() == 1)
                                .map(g -> String.valueOf(g.getGenresID()))
                                .collect(Collectors.joining("|"));

            // 불호 장르는 모두 제외
            String dislikeIds = genresList.stream()
                                .filter(g -> g.getGenresLike() == 2)
                                .map(g -> String.valueOf(g.getGenresID()))
                                .collect(Collectors.joining(","));

	        // API 호출
	        MovieAPIHandler apiHandler = new MovieAPIHandler();
	        List<MovieDTO> recommendList = apiHandler.getRecommendedMovies(likeIds, dislikeIds);

	        // 모든 데이터를 request에 담기
	        request.setAttribute("userInfo", dto);
	        request.setAttribute("genresList", genresList); // 선호 장르 표시용
	        request.setAttribute("recommendList", recommendList); // 추천 영화 리스트
	        request.setAttribute("allGenreNames", allGenreNames); // 영화마다 장르 표시용

	        request.getRequestDispatcher("recommend.jsp").forward(request, response);
		} else {
	        response.sendRedirect("login.jsp");
	    }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
