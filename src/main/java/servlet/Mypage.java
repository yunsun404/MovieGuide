package servlet;
import java.io.IOException;
import java.util.List;

import dao.MypageDAO;
import dto.GenresDTO;
import dto.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/mypage")
public class Mypage extends HttpServlet {
	   private static final long serialVersionUID = 1L;

	   public Mypage() {
	      super();
	   }

	   @Override
	   protected void doGet(HttpServletRequest request, HttpServletResponse response)
	         throws ServletException, IOException {

	      // ✅ 기존 세션 가져오기 (없으면 null)
	      HttpSession session = request.getSession(false);

	      // 세션 자체가 없는 경우 → 로그인 페이지로
	      if (session == null) {
	         response.sendRedirect("login.jsp");
	         return;
	      }

	      // 세션에 로그인 정보가 없는 경우 → 로그인 페이지로
	      UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
	      if (loginUser == null) {
	         response.sendRedirect("login.jsp");
	         return;
	      }

	      // 🔥 로그인한 유저 번호
	      int userNo = loginUser.getUserNo();

	      // 🔍 마이페이지 데이터 조회
	      MypageDAO dao = new MypageDAO();
	      UserDTO dto = dao.getUserInfo(userNo);
	      List<GenresDTO> genresList = dao.getGenrePreferences(userNo);

	      // 📦 JSP로 데이터 전달
	      request.setAttribute("userInfo", dto);
	      request.setAttribute("genresList", genresList);

	      // 👉 마이페이지 이동
	      request.getRequestDispatcher("mypage.jsp").forward(request, response);
	   }

	   @Override
	   protected void doPost(HttpServletRequest request, HttpServletResponse response)
	         throws ServletException, IOException {
	      // 필요 시 확장
	   }
	}
