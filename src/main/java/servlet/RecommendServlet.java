package servlet;

import dao.GenreDAO;
import dto.MovieDTO;
import dto.UserDTO;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;

import java.io.*;
import java.net.*;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/recommend")
public class RecommendServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // 🔑 TMDB API KEY
    private static final String API_KEY =
            "93f55bc880e6eb7b87f2962cce95349f";

    // TMDB 영화 목록 API
    private static final String TMDB_URL =
            "https://api.themoviedb.org/3/movie/popular?language=ko-KR";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("=== RecommendServlet HIT ===");

        // 1️⃣ 로그인 체크
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("loginUser");

        if (user == null) {
            System.out.println("❌ loginUser 세션 없음");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().print("{\"error\":\"not logged in\"}");
            return;
        }

        System.out.println("✅ 로그인 유저 userNo = " + user.getUserNo());
        int userNo = user.getUserNo();

        // 2️⃣ 유저 장르 취향 조회
        Set<Integer> likeGenres;
        Set<Integer> hateGenres;

        try {
            GenreDAO genreDAO = new GenreDAO();

            likeGenres = new HashSet<>(genreDAO.getLikeGenres(userNo));
            hateGenres = new HashSet<>(genreDAO.getHateGenres(userNo));

            System.out.println("👍 좋아요 장르: " + likeGenres);
            System.out.println("👎 싫어요 장르: " + hateGenres);

        } catch (Exception e) {
            System.out.println("❌ DB 조회 중 오류");
            e.printStackTrace();

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().print("{\"error\":\"db error\"}");
            return;
        }

        // 3️⃣ TMDB API 호출
        List<MovieDTO> apiMovies = fetchMoviesFromTMDB();
        System.out.println("🎬 API 영화 수: " + apiMovies.size());

        // 4️⃣ 추천 필터링
        JSONArray result = new JSONArray();

        for (MovieDTO movie : apiMovies) {

            if (movie.isRecommend(likeGenres, hateGenres)) {
                JSONObject obj = new JSONObject();

                obj.put("id", movie.getId());
                obj.put("title", movie.getTitle());
                obj.put("genres", movie.getGenreIds());

                // ✅ 포스터/평점/개봉일 추가
                obj.put("poster_path", movie.getPoster_path());
                obj.put("vote_average", movie.getVote_average());
                obj.put("release_date", movie.getRelease_date());

                result.put(obj);
            }

            if (result.length() == 100) break;
        }

        System.out.println("⭐ 추천 결과 수: " + result.length());

        // 5️⃣ JSON 응답
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().print(result.toString());
    }

    // =========================
    // TMDB API 실제 호출
    // =========================
    private List<MovieDTO> fetchMoviesFromTMDB() {

        List<MovieDTO> list = new ArrayList<>();

        int MAX_PAGES = 5; // ✅ 5페이지면 20*5 = 100개

        for (int page = 1; page <= MAX_PAGES; page++) {
            try {
                String apiUrl = TMDB_URL + "&page=" + page + "&api_key=" + API_KEY;
                System.out.println("🌐 TMDB 호출: " + apiUrl);

                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), "UTF-8"))) {

                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) sb.append(line);

                    JSONObject json = new JSONObject(sb.toString());
                    JSONArray results = json.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject m = results.getJSONObject(i);

                        int id = m.getInt("id");
                        String title = m.optString("title", "");

                        String posterPath = m.optString("poster_path", "");
                        double vote = m.optDouble("vote_average", 0.0);
                        String releaseDate = m.optString("release_date", "");

                        JSONArray genreArr = m.optJSONArray("genre_ids");
                        List<Integer> genreIds = new ArrayList<>();
                        if (genreArr != null) {
                            for (int j = 0; j < genreArr.length(); j++) {
                                genreIds.add(genreArr.getInt(j));
                            }
                        }

                        list.add(new MovieDTO(id, title, genreIds, posterPath, vote, releaseDate));
                    }
                }

            } catch (Exception e) {
                System.out.println("❌ TMDB API 호출 실패 (page=" + page + ")");
                e.printStackTrace();
            }
        }

        return list;
    }

}
