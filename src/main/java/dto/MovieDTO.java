package dto;

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
		if (poster_path == null || poster_path.isEmpty())
			return null;
		return "https://image.tmdb.org/t/p/w500" + poster_path;
	}

	public void setMovieId(int id) {
		this.id = id;
	}

	public MovieDTO(int id, String title, String original_title, String poster_path, String release_date,
			double vote_average, List<Integer> genreIds) {
		this.id = id;
		this.title = title;
		this.genre_ids = genreIds;
		this.original_title = original_title;
		this.poster_path = poster_path;
		this.release_date = release_date;
		this.vote_average = vote_average;
		this.genre_ids = genreIds;
	}

	public MovieDTO(int id, String title, List<Integer> genreIds, String poster_path, double vote_average,
			String release_date) {
		this.id = id;
		this.title = title;
		this.genre_ids = genreIds;
		this.poster_path = poster_path;
		this.vote_average = vote_average;
		this.release_date = release_date;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public List<Integer> getGenreIds() {
		return genre_ids;
	}

	// ⭐ 추천 판단 로직
	public boolean isRecommend(java.util.Set<Integer> likeGenres, java.util.Set<Integer> hateGenres) {

		boolean hasLike = false;

		for (int g : genre_ids) {
			if (hateGenres.contains(g))
				return false;
			if (likeGenres.contains(g))
				hasLike = true;
		}
		return hasLike;
	}

}
