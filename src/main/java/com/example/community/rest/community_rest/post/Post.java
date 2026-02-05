package com.example.community.rest.community_rest.post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.community.rest.community_rest.file.Upload;
import com.example.community.rest.community_rest.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Size;

@Entity
public class Post {
	@Id
	@GeneratedValue
	private Long id;
	@Size(min=1, message ="닉네임은 최소 한 글자 이상이여야 합니다.")
	private String username;
	@Size(min=10, message= "내용은 최소 열 글자 이상이여야 합니다.")
	private String description;
	@Column(nullable = false, updatable = false)
	private LocalDateTime uploadDate;
	@Size(min=1, message ="닉네임은 최소 한 글자 이상이여야 합니다.")
	private String title;
	private String thumbFilePath;
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<Upload> uploads = new ArrayList<>();

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	public Post() {
		
	}
	
	public Post(Long id, String username,  String title, String description, LocalDateTime uploadDate) {
		super();
		this.id = id;
		this.username = username;
		this.title = title;
		this.description = description;
		this.uploadDate = uploadDate;
	}
	
	public Post(Long id, String username, String description, LocalDateTime uploadDate, String title, List<Upload> uploads) {
		super();
		this.id = id;
		this.username = username;
		this.description = description;
		this.uploadDate = uploadDate;
		this.title = title;
		this.uploads = uploads;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(LocalDateTime uploadDate) {
		this.uploadDate = uploadDate;
	}
	
	@PrePersist
    public void prePersist() {
        if (uploadDate == null) {
            this.uploadDate = LocalDateTime.now(); // 현재 날짜를 설정
        }
    }
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public List<Upload> getUploads() {
		return uploads;
	}

	public void setUploads(List<Upload> uploads) {
		this.uploads = uploads;
	}
	
	public void addUpload(Upload upload) {
	    uploads.add(upload);
	    upload.setPost(this);  // 양방향 관계 설정
	}
	
	public String getThumbFilePath() {
		return thumbFilePath;
	}

	public void setThumbFilePath(String thumbFilePath) {
		this.thumbFilePath = thumbFilePath;
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", username=" + username + ", description=" + description + ", uploadDate="
				+ uploadDate + ", title=" + title + ", thumbFilePath=" + thumbFilePath + ", uploads=" + uploads
				+ ", user=" + user + "]";
	}
	
}
