package com.movieguide.dto;

import lombok.Data;

@Data
public class UserDTO {
	private String userID, userPW, userEmail;
	private int userNo;
	
	public String getUserNickname() {
		return this.userEmail.split("@")[0]; // @ 앞부분을 닉네임으로 사용
	}
}
