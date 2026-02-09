package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/genre/list")
public class GenreListServlet extends HttpServlet {

    private static final String DB_URL =
            "jdbc:mariadb://db.wisejia.com:3306/movieguide?useUnicode=true&characterEncoding=UTF-8";
    private static final String DB_USER = "movieguide";
    private static final String DB_PASS = "MovieGuide";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json;charset=UTF-8");

        String sql = "SELECT genres_id, genres_name FROM genres_name";

        JSONArray arr = new JSONArray();

        try (
            Connection c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                JSONObject o = new JSONObject();
                o.put("id", rs.getInt("genres_id"));
                o.put("name", rs.getString("genres_name"));
                arr.put(o);
            }

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(arr.toString());

        } catch (SQLException e) {
            e.printStackTrace();

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            JSONObject err = new JSONObject();
            err.put("result", false);
            err.put("message", "DB 연결/쿼리 오류");
            err.put("detail", e.getMessage()); // 디버깅용 (배포시 빼도 됨)

            resp.getWriter().write(err.toString());

        } catch (Exception e) {
            e.printStackTrace();

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            JSONObject err = new JSONObject();
            err.put("result", false);
            err.put("message", "서버 오류");
            err.put("detail", e.getMessage());

            resp.getWriter().write(err.toString());
        }
    }
}
