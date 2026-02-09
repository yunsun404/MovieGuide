package servlet;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dao.WishlistDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/api/wishlist")
public class WishlistServlet extends HttpServlet {

    private final WishlistDAO dao = new WishlistDAO();

    // 톰캣에서 프론트도 같이 열고 있으니 사실 CORS 크게 필요 없지만,
    // 혹시라도 대비해서 넣어둠
    private void setCorsHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:8080");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
        setCorsHeaders(resp);
        resp.setStatus(200);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(resp);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        try {
            // 1) JSON body 읽기
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = req.getReader()) {
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
            }

            JsonObject body = JsonParser.parseString(sb.toString()).getAsJsonObject();
            int movieId = body.get("movieId").getAsInt();

            // 2) 로그인 없으니까 임시 userNo = 1
            int userNo = 4;

            // 3) DB 저장 (중복이면 0)
            int rows = dao.insertIgnoreDuplicate(userNo, movieId);

            // 4) 응답
            JsonObject out = new JsonObject();
            out.addProperty("ok", true);
            out.addProperty("rows", rows);
            out.addProperty("movieId", movieId);
            resp.getWriter().print(out.toString());

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().print("{\"ok\":false,\"error\":\"" + e.getMessage().replace("\"","'") + "\"}");
        }
    }
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        try {
            String movieIdStr = req.getParameter("movieId");
            if (movieIdStr == null) {
                resp.setStatus(400);
                resp.getWriter().print("{\"ok\":false,\"error\":\"movieId required\"}");
                return;
            }

            int movieId = Integer.parseInt(movieIdStr);
            int userNo = 4; // ✅ 테스트용

            int rows = dao.deleteByUserAndMovie(userNo, movieId);

            JsonObject out = new JsonObject();
            out.addProperty("ok", true);
            out.addProperty("rows", rows); // 1: 삭제됨, 0: 원래 없었음
            resp.getWriter().print(out.toString());

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().print("{\"ok\":false,\"error\":\"" + e.getMessage().replace("\"", "'") + "\"}");
        }
    }
}