package dto;

import java.util.Collection;

public class UserDTO {

    private int userNo;
    private String userId;   // ⭐ 추가 (닉네임/이름)
    private String email;
    private String password;
	private Object userEmail;

    public UserDTO() {}

    // 회원가입이랑 조회용
    public UserDTO(int userNo, String userId, String email, String password) {
        this.userNo = userNo;
        this.userId = userId;
        this.email = email;
        this.password = password;
    }
    public String getUserNickname() {
        // 1) 이메일이 없으면 닉네임을 만들 수 없으니 안전하게 처리
        if (userEmail == null || ((String) userEmail).isBlank()) {
            return ""; // 또는 "사용자"
        }

        // 2) @ 없는 이메일도 대비
        int at = ((String) userEmail).indexOf('@');
        if (at <= 0) return (String) userEmail;

        return ((String) userEmail).substring(0, at);
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
