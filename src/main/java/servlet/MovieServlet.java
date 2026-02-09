package servlet;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@WebServlet("/api/*")
public class MovieServlet extends HttpServlet {

    
    private static final String TMDB_API_KEY = "93f55bc880e6eb7b87f2962cce95349f";
    private static final String TMDB_BASE = "https://api.themoviedb.org/3";

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json; charset=UTF-8");

        String pathInfo = req.getPathInfo(); // 예: /top100, /genres
        if (pathInfo == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\":\"No endpoint\"}");
            return;
        }

        try {
            if (pathInfo.equals("/genres")) {
                handleGenres(req, resp);
            } else if (pathInfo.equals("/top100")) {
                handleTop100(req, resp);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\":\"Unknown endpoint\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Server error\"}");
        }
    }

    private void handleGenres(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String lang = getOrDefault(req.getParameter("lang"), "ko-KR");

        String url = TMDB_BASE + "/genre/movie/list"
                + "?api_key=" + encode(TMDB_API_KEY)
                + "&language=" + encode(lang);

        String tmdbJson = httpGet(url);

        // 그대로 반환
        resp.getWriter().write(tmdbJson);
    }

    private void handleTop100(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String lang = getOrDefault(req.getParameter("lang"), "ko-KR");

        // 1~5 페이지 합쳐서 results 100개 만들기
        // 결과 형식을 { results: [...] } 로 통일해서 프론트에서 쓰기 쉽게 해줌.
        StringBuilder mergedResults = new StringBuilder();
        mergedResults.append("{\"results\":[");

        int count = 0;
        for (int page = 1; page <= 5; page++) {
            String url = TMDB_BASE + "/movie/top_rated"
                    + "?api_key=" + encode(TMDB_API_KEY)
                    + "&language=" + encode(lang)
                    + "&page=" + page;

            String tmdbJson = httpGet(url);

            JsonObject obj = JsonParser.parseString(tmdbJson).getAsJsonObject();
            var results = obj.getAsJsonArray("results");

            for (int i = 0; i < results.size(); i++) {
                if (count >= 100) break;
                if (count > 0) mergedResults.append(",");
                mergedResults.append(results.get(i).toString());
                count++;
            }
            if (count >= 100) break;
        }

        mergedResults.append("]}");
        resp.getWriter().write(mergedResults.toString());
    }

    private String httpGet(String urlStr) throws IOException {
        HttpURLConnection conn = null;
        BufferedReader br = null;

        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);

            int code = conn.getResponseCode();
            InputStream is = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();

            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);

            if (code < 200 || code >= 300) {
                throw new IOException("TMDB HTTP " + code + " : " + sb);
            }
            return sb.toString();
        } finally {
            if (br != null) try { br.close(); } catch (Exception ignored) {}
            if (conn != null) conn.disconnect();
        }
    }

    private void setCorsHeaders(HttpServletResponse resp) {
        // 개발 단계에서 편하게: 모두 허용
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

    private String getOrDefault(String v, String def) {
        return (v == null || v.trim().isEmpty()) ? def : v.trim();
    }

    private String encode(String s) throws UnsupportedEncodingException {
        return URLEncoder.encode(s, "UTF-8");
    }
}