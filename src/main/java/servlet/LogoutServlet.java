package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
	@Override
	 protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

	    HttpSession session = req.getSession(false);
	    if (session != null) session.invalidate();

	    // JSESSIONID 쿠키 제거(확실하게)
	    Cookie cookie = new Cookie("JSESSIONID", "");
	    cookie.setPath(req.getContextPath());
	    cookie.setMaxAge(0);
	    resp.addCookie(cookie);

	    resp.sendRedirect(req.getContextPath() + "/index.html");
	  }
	

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		HttpSession s = req.getSession(false);

		if (s != null)
			s.invalidate(); // ⭐ 세션 삭제

		resp.getWriter().write("{\"result\":true}");
	}
}
