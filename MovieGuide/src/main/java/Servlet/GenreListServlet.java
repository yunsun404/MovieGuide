package Servlet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONObject;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/genre/list")

public class GenreListServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws IOException {

        resp.setContentType("application/json;charset=UTF-8");

        String sql = "SELECT genres_id, genres_name FROM genres_name";

        JSONArray arr = new JSONArray();

        try(Connection c = DriverManager.getConnection(
                "jdbc:mariadb://db.wisejia.com:3306/movieguide",
                "movieguide",
                "MovieGuide");
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while(rs.next()){
                JSONObject o = new JSONObject();
                o.put("id", rs.getInt("genres_id"));
                o.put("name", rs.getString("genres_name"));
                arr.put(o);
            }

        } catch(Exception e){
            e.printStackTrace();
        }

        resp.getWriter().write(arr.toString());
    }
}
