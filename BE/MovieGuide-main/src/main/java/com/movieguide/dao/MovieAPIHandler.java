package com.movieguide.dao;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.google.gson.Gson;
import com.movieguide.dto.MovieDTO;
import com.movieguide.dto.MovieResponseDTO;

public class MovieAPIHandler {
	private String API_KEY;
    private final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";
    
    // api 키 읽어오기
    public MovieAPIHandler() {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();

            if (is != null) {
                prop.load(is);
                this.API_KEY = prop.getProperty("tmdb.api.key");
                
                if (this.API_KEY != null && !this.API_KEY.isEmpty()) {
                    System.out.println("API KEY 불러오기 성공");
                }
            } else {
                try (InputStream is2 = getClass().getClassLoader().getResourceAsStream("config.properties")) {
                    if (is2 != null) {
                        prop.load(is2);
                        this.API_KEY = prop.getProperty("tmdb.api.key");
                        System.out.println("API KEY 불러오기 성공");
                    } else {
                        System.out.println("에러: config.properties 파일을 찾을 수 없습니다. (위치: src/main/resources)");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("에러: 설정 파일 읽기 실패");
            e.printStackTrace();
        }
    }

    public List<MovieDTO> getRecommendedMovies(String likeGenreIds, String dislikeGenreIds) {
    	// API_KEY가 비어있으면 실행 안됨
        if (API_KEY == null || API_KEY.isEmpty()) {
            System.out.println("API 키가 설정되지 않았습니다.");
            return new ArrayList<>();
        }
    	
    	List<MovieDTO> movieList = new ArrayList<>();
        
        // 장르 ID가 없으면 빈 리스트 반환
        if (likeGenreIds == null || likeGenreIds.isEmpty()) {
            return movieList;
        }

        try {
            // URL 설정
        	StringBuilder sbUrl = new StringBuilder(BASE_URL);
            sbUrl.append("?api_key=").append(API_KEY);
            sbUrl.append("&language=ko-KR"); // 한국어
            sbUrl.append("&sort_by=popularity.desc"); // 인기순 정렬
            sbUrl.append("&with_genres=").append(likeGenreIds); // 선호 장르

            if (dislikeGenreIds != null && !dislikeGenreIds.isEmpty()) {
                sbUrl.append("&without_genres=").append(dislikeGenreIds); // 불호 장르
            }
            
            URL url = new URL(sbUrl.toString());
            // System.out.println("요청 URL: " + urlString); 
            
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");

            int responseCode = conn.getResponseCode(); // 응답상태 반환
            if (responseCode == 200) { // 성공한 경우
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();

                // JSON -> DTO 객체
                Gson gson = new Gson();
                MovieResponseDTO responseData = gson.fromJson(sb.toString(), MovieResponseDTO.class);
                
                if (responseData != null && responseData.getResults() != null) {
                    movieList = responseData.getResults();
                }
            }
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return movieList;
    }
}
