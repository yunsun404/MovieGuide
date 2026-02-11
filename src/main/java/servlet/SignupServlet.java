package servlet;

import java.io.BufferedReader;
import java.io.IOException;

import org.json.JSONObject;

import dao.UserDAO;
import dto.UserDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");

        JSONObject result = new JSONObject();

        try {

            StringBuilder sb = new StringBuilder();
            BufferedReader br = req.getReader();
            String line;

            while((line = br.readLine()) != null){
                sb.append(line);
            }

            JSONObject json = new JSONObject(sb.toString());

            String name = json.getString("name");
            String email = json.getString("email");
            String password = json.getString("password");

            UserDTO user = new UserDTO(0, name, email, password);

            int userNo = new UserDAO().signup(user); // ⭐ 이제 정상


            if(userNo > 0){

                HttpSession session = req.getSession();

                user.setUserNo(userNo);
                session.setAttribute("loginUser", user);

                result.put("result", true);
            }
            else{
                result.put("result", false);
            }

        } catch(Exception e){
            e.printStackTrace();
            result.put("result", false);
        }

        resp.getWriter().write(result.toString());
    }
}
