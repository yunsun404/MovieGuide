package servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.json.JSONObject;

import dto.UserDTO;

@WebServlet("/genre/select")
public class GenreSelectServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws IOException {

        resp.setContentType("application/json;charset=UTF-8");

        try {

            HttpSession session = req.getSession();
            UserDTO user = (UserDTO) session.getAttribute("loginUser");

            if(user == null){
                resp.getWriter().write("{\"result\":false}");
                return;
            }

            int userNo = user.getUserNo();

            /* JSON 읽기 */
            StringBuilder sb = new StringBuilder();
            BufferedReader br = req.getReader();
            String line;

            while((line = br.readLine()) != null){
                sb.append(line);
            }

            JSONObject json = new JSONObject(sb.toString());

            Connection conn = DriverManager.getConnection(
                "jdbc:mariadb://db.wisejia.com:3306/movieguide",
                "movieguide",
                "MovieGuide"
            );

            /* ⭐ UPSERT */
            String upsert =
                "INSERT INTO genres(user_no, genres_id, genres_like) " +
                "VALUES(?,?,?) " +
                "ON DUPLICATE KEY UPDATE genres_like=?";

            /* ⭐ DELETE (취소용) */
            String delete =
                "DELETE FROM genres WHERE user_no=? AND genres_id=?";

            PreparedStatement psUp = conn.prepareStatement(upsert);
            PreparedStatement psDel = conn.prepareStatement(delete);


            for(String key : json.keySet()){

                int genreId = Integer.parseInt(key);
                int like = json.getInt(key);

                if(like == 0){
                    psDel.setInt(1, userNo);
                    psDel.setInt(2, genreId);
                    psDel.executeUpdate();
                }
                else{
                    psUp.setInt(1, userNo);
                    psUp.setInt(2, genreId);
                    psUp.setInt(3, like);
                    psUp.setInt(4, like);
                    psUp.executeUpdate();
                }
            }

            psUp.close();
            psDel.close();
            conn.close();

            resp.getWriter().write("{\"result\":true}");

        } catch(Exception e){
            e.printStackTrace();
            resp.getWriter().write("{\"result\":false}");
        }
    }
}
