package com.example.community.rest.community_rest.comment;

public class CommentRequest {
    private String content;
    private int userId;  // 댓글 작성자 (User PK)
    
	public CommentRequest(String content, int userId) {
		super();
		this.content = content;
		this.userId = userId;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	@Override
	public String toString() {
		return "CommentRequest [content=" + content + ", userId=" + userId + "]";
	}
}

