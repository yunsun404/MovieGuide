package dao;

import dto.MovieDTO;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecommendDAO {

    private static final String TMDB_API_KEY = "93f55bc880e6eb7b87f2962cce95349f";
    private static final String TMDB_BASE = "https://api.themoviedb.org/3";

    public List<MovieDTO> getMoviesByGenreIds(List<Integer> genreIds) throws Exception {
        if (genreIds == null || genreIds.isEmpty()) return Collections.emptyList();

        // TMDB는 with_genres=28,12 이런 식
        String withGenres = joinIds(genreIds);

        String urlStr = TMDB_BASE + "/discover/movie"
                + "?api_key=" + TMDB_API_KEY
                + "&language=ko-KR"
                + "&sort_by=popularity.desc"
                + "&with_genres=" + URLEncoder.encode(withGenres, "UTF-8")
                + "&page=1";

        JSONObject json = getJson(urlStr);
        JSONArray results = json.optJSONArray("results");
        if (results == null) return Collections.emptyList();

        List<MovieDTO> list = new ArrayList<>();

        // ⚠️ TMDB page=1은 보통 20개만 옴. (100개 제한은 의미 거의 없음)
        int limit = Math.min(100, results.length());

        for (int i = 0; i < limit; i++) {
            JSONObject o = results.getJSONObject(i);

            // ✅ genre_ids 파싱
            List<Integer> genreList = new ArrayList<>();
            JSONArray gids = o.optJSONArray("genre_ids");
            if (gids != null) {
                for (int j = 0; j < gids.length(); j++) {
                    genreList.add(gids.getInt(j));
                }
            }

            // ✅ 생성자에 맞춰서 한 번에 세팅
            MovieDTO m = new MovieDTO(
                    o.optInt("id"),
                    o.optString("title", ""),
                    o.optString("original_title", ""),
                    o.isNull("poster_path") ? null : o.optString("poster_path"),
                    o.optString("release_date", ""),
                    o.optDouble("vote_average", 0),
                    genreList
            );

            list.add(m);
        }

        return list;
    }

    private String joinIds(List<Integer> ids) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(ids.get(i));
        }
        return sb.toString();
    }

    private JSONObject getJson(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "UTF-8"))) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);

            return new JSONObject(sb.toString());
        }
    }
}
