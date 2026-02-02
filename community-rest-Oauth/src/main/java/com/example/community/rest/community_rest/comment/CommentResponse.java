package com.example.community.rest.community_rest.comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentResponse {
    private Long id;
    private String content;
    private LocalDateTime createdDate;
    private String username;  // 작성자 닉네임
    private List<CommentResponse> replies = new ArrayList<>();  // 대댓글 리스트
    
    public CommentResponse() {
    	
    }
    
	public CommentResponse(Long id, String content, LocalDateTime createdDate, String username,
			List<CommentResponse> replies) {
		super();
		this.id = id;
		this.content = content;
		this.createdDate = createdDate;
		this.username = username;
		this.replies = replies;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public List<CommentResponse> getReplies() {
		return replies;
	}
	public void setReplies(List<CommentResponse> replies) {
		this.replies = replies;
	}
	
	@Override
	public String toString() {
		return "CommentResponse [id=" + id + ", content=" + content + ", createdDate=" + createdDate + ", username="
				+ username + ", replies=" + replies + "]";
	}
}
