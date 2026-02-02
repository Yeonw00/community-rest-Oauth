package com.example.community.rest.community_rest.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;

public class UserDTO {
	private Long id;
	private String username;
	private LocalDate birthDate;
	private String emailAddress;
	private String phoneNumber;
	private boolean isMale;
	
	public UserDTO(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.birthDate = user.getBirthDate();
		this.emailAddress = user.getEmailAddress();
		this.phoneNumber = user.getPhoneNumber();
		this.isMale = user.isMale();
	}
	
	 // List<User>를 받아서 UserDTO 리스트로 변환하는 메서드
    public static List<UserDTO> from(List<User> users) {
        List<UserDTO> userDTOList = new ArrayList<>();
        for (User user : users) {
            userDTOList.add(new UserDTO(user)); // 각 User 객체를 UserDTO로 변환하여 리스트에 추가
        }
        return userDTOList;
    }
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public LocalDate getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public boolean isMale() {
		return isMale;
	}
	public void setMale(boolean isMale) {
		this.isMale = isMale;
	}

	@Override
	public String toString() {
		return "UserDTO [id=" + id + ", username=" + username + ", birthDate=" + birthDate + ", emailAddress="
				+ emailAddress + ", phoneNumber=" + phoneNumber + ", isMale=" + isMale + "]";
	}
}
