package com.example.community.rest.community_rest.user;

import java.time.LocalDate;
import java.util.List;

import com.example.community.rest.community_rest.post.Post;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

@Entity(name="user_details")
public class User {
	@Id
	@GeneratedValue
	private Long id;
	@Size(min=2, message = "이름은 최소 두 글자 이상이여야 합니다.")
	private String username;
	@Column(nullable = true)
	private String password; // 패스워드 필드 추가
	@Past(message = "생일은 과거날짜여야 합니다.")
	private LocalDate birthDate;
	private String emailAddress;
	private String phoneNumber;
	private boolean isMale;
	@Column(unique = true, nullable = true)
    private String googleId; // 구글 로그인 사용자

    @Column(unique = true, nullable = true)
    private String facebookId; // 페이스북 로그인 사용자
	
	@OneToMany(mappedBy = "user")
	@JsonIgnore
	private List<Post> posts;
	
	public User() {
		
	}
	
	public User(Long id, String username, String password, LocalDate birthDate, String emailAddress, String phoneNumber, boolean isMale) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.birthDate = birthDate;
		this.emailAddress = emailAddress;
		this.phoneNumber = phoneNumber;
		this.isMale = isMale;
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
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
	
	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}
	
	public String getGoogleId() {
		return googleId;
	}

	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", birthDate=" + birthDate + ", emailAddress="
				+ emailAddress + ", phoneNumber=" + phoneNumber + ", isMale=" + isMale + "]";
	}
	
}
