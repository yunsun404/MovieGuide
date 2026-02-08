package Servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;

import org.json.JSONObject;

import dto.UserDTO;
import dao.UserDAO;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");

        try {

            /* ===== JSON 읽기 ===== */
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = req.getReader();

            String line;
            while((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject json = new JSONObject(sb.toString());

            String email = json.getString("email");
            String password = json.getString("password");
            
            System.out.println("===== 로그인 요청 =====");
            System.out.println("email = [" + email + "]");
            System.out.println("password = [" + password + "]");



            /* ===== 로그인 ===== */
            UserDTO param = new UserDTO(0, email, password);

            UserDTO loginUser = new UserDAO().login(param);


            JSONObject result = new JSONObject();

            if (loginUser != null) {

                HttpSession session = req.getSession();
                session.setAttribute("loginUser", loginUser);

                result.put("result", true);
                result.put("message", "로그인 성공");

            } else {

                result.put("result", false);
                result.put("message", "이메일 또는 비밀번호 오류");
            }

            resp.getWriter().write(result.toString());

        } catch(Exception e) {
            e.printStackTrace();

            JSONObject err = new JSONObject();
            err.put("result", false);
            err.put("message", "서버 오류");

            resp.getWriter().write(err.toString());
        }
    }
}
