package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import org.json.JSONObject;

import dto.UserDTO;

/**
 * Servlet implementation class UserInfoServlet
 */
@WebServlet("/api/me")
public class UserInfoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("application/json; charset=UTF-8");

        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.setStatus(401);
            resp.getWriter().print("{\"error\":\"not logged in\"}");
            return;
        }

        UserDTO user = (UserDTO) session.getAttribute("loginUser");
        if (user == null) {
            resp.setStatus(401);
            resp.getWriter().print("{\"error\":\"not logged in\"}");
            return;
        }


        

        JSONObject out = new JSONObject();
        out.put("userId", user.getUserId());   // ⭐ 여기
        out.put("userNo", user.getUserNo());

        resp.getWriter().print(out.toString());
    }
}



