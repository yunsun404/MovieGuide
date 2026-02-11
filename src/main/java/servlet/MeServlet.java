package servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import org.json.JSONObject;
import dto.UserDTO;

@WebServlet("/me")
public class MeServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    resp.setContentType("application/json;charset=UTF-8");

    HttpSession session = req.getSession(false);

    JSONObject out = new JSONObject();

    if (session == null) {
      out.put("loggedIn", false);
      resp.getWriter().write(out.toString());
      return;
    }

    UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

    if (loginUser == null) {
      out.put("loggedIn", false);
    } else {
      out.put("loggedIn", true);

      // ✅ 너 DTO 필드명에 맞게 바꿔야 함
      out.put("name", loginUser.getName());     // 예: getUserName(), getNickname() 등일 수도 있음
      // out.put("userNo", loginUser.getUserNo());
    }

    resp.getWriter().write(out.toString());
  }
}
