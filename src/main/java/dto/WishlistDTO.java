package dto;
import java.sql.Timestamp;

public class WishlistDTO {
    private int wishNo;
    private int userNo;
    private int movieId;
    private Timestamp wishDate;

    public WishlistDTO() {}

    public WishlistDTO(int userNo, int movieId) {
        this.userNo = userNo;
        this.movieId = movieId;
    }

    public int getWishNo() { return wishNo; }
    public void setWishNo(int wishNo) { this.wishNo = wishNo; }

    public int getUserNo() { return userNo; }
    public void setUserNo(int userNo) { this.userNo = userNo; }

    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }

    public Timestamp getWishDate() { return wishDate; }
    public void setWishDate(Timestamp wishDate) { this.wishDate = wishDate; }
}