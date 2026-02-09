package dto;

import java.util.Collection;

public class UserDTO {

    private int userNo;
    private String userId;   // ⭐ 추가 (닉네임/이름)
    private String email;
    private String password;

    public UserDTO() {}

    // 회원가입이랑 조회용
    public UserDTO(int userNo, String userId, String email, String password) {
        this.userNo = userNo;
        this.userId = userId;
        this.email = email;
        this.password = password;
    }
    
 // 로그인용
    public UserDTO(int userNo, String email, String password) {
        this.userNo = userNo;
        this.email = email;
        this.password = password;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public Collection<?> getName() {
		// TODO Auto-generated method stub
		return null;
	}
}
