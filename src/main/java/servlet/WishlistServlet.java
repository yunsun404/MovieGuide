package servlet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dao.WishlistDAO;
import dto.UserDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/wishlist")
public class WishlistServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final WishlistDAO dao = new WishlistDAO();

    // 같은 톰캣에서 서비스면 사실 CORS 거의 필요없지만, 개발 편의로 둠
    private void setCorsHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:8080");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setHeader("Access-Control-Allow-Methods", "GET,POST,DELETE,OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }

    // ✅ 세션에서 로그인 유저 번호 가져오기 (없으면 401 처리)
    private Integer getLoginUserNoOr401(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        UserDTO user = (session != null) ? (UserDTO) session.getAttribute("loginUser") : null;

        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json; charset=UTF-8");
            resp.getWriter().print("{\"ok\":false,\"error\":\"not logged in\"}");
            return null;
        }
        return user.getUserNo();
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
        setCorsHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    // ✅ 위시리스트 목록 조회: GET /api/wishlist  -> { ok:true, movieIds:[...] }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(resp);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        Integer userNo = getLoginUserNoOr401(req, resp);
        if (userNo == null) return;

        try {
            List<Integer> ids = dao.selectMovieIdsByUser(userNo);

            JsonObject out = new JsonObject();
            out.addProperty("ok", true);

            JsonArray arr = new JsonArray();
            for (Integer id : ids) arr.add(id);

            out.add("movieIds", arr);
            resp.getWriter().print(out.toString());

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().print("{\"ok\":false,\"error\":\"" + e.getMessage().replace("\"", "'") + "\"}");
        }
    }

    // ✅ 추가: POST /api/wishlist  body: {"movieId":123}
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(resp);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        Integer userNo = getLoginUserNoOr401(req, resp);
        if (userNo == null) return;

        try {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = req.getReader()) {
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
            }

            JsonObject body = JsonParser.parseString(sb.toString()).getAsJsonObject();
            if (!body.has("movieId")) {
                resp.setStatus(400);
                resp.getWriter().print("{\"ok\":false,\"error\":\"movieId required\"}");
                return;
            }

            int movieId = body.get("movieId").getAsInt();

            int rows = dao.insertIgnoreDuplicate(userNo, movieId);

            JsonObject out = new JsonObject();
            out.addProperty("ok", true);
            out.addProperty("rows", rows);
            out.addProperty("movieId", movieId);
            resp.getWriter().print(out.toString());

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().print("{\"ok\":false,\"error\":\"" + e.getMessage().replace("\"", "'") + "\"}");
        }
    }

    // ✅ 삭제: DELETE /api/wishlist?movieId=123
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(resp);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        Integer userNo = getLoginUserNoOr401(req, resp);
        if (userNo == null) return;

        try {
            String movieIdStr = req.getParameter("movieId");
            if (movieIdStr == null) {
                resp.setStatus(400);
                resp.getWriter().print("{\"ok\":false,\"error\":\"movieId required\"}");
                return;
            }

            int movieId = Integer.parseInt(movieIdStr);

            int rows = dao.deleteByUserAndMovie(userNo, movieId);

            JsonObject out = new JsonObject();
            out.addProperty("ok", true);
            out.addProperty("rows", rows); // 1: 삭제됨, 0: 원래 없었음
            out.addProperty("movieId", movieId);
            resp.getWriter().print(out.toString());

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().print("{\"ok\":false,\"error\":\"" + e.getMessage().replace("\"", "'") + "\"}");
        }
    }
}
