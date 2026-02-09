package com.movieguide.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MovieDTO {
	private int id;                
    private String title;       
    private String original_title; 
    private String poster_path;   
    private String release_date;  
    private double vote_average;  
    private List<Integer> genre_ids;
    
    public String getFullPosterPath() {
        if (poster_path == null || poster_path.isEmpty()) {
            return "./img/no-image.png"; // 포스터가 없는 경우 기본 이미지 추가?
        }
        return "https://image.tmdb.org/t/p/w500" + poster_path;
    }
}
