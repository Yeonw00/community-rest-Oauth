package com.example.community.rest.community_rest.comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.community.rest.community_rest.post.Post;
import com.example.community.rest.community_rest.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 댓글 PK

    //  어떤 게시글의 댓글인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @JsonBackReference  // 순환참조 방지 (Post ↔ Comment)
    private Post post;
    
    //  대댓글 관계 설정 (부모 댓글 참조)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    // 대댓글 리스트 (부모 댓글에서 자식 댓글 리스트 조회 가능)
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies = new ArrayList<>();

    @Column(nullable = false, length = 500)
    private String content;  // 댓글 내용

    //  어떤 유저가 작성했는지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // 댓글 작성자 (User 참조)

    @Column(nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedDate = LocalDateTime.now();
    
    public Comment() {
    	
    }

	public Comment(Long id, Post post, Comment parent, List<Comment> replies, String content, User user,
			LocalDateTime createdDate, LocalDateTime updatedDate) {
		super();
		this.id = id;
		this.post = post;
		this.parent = parent;
		this.replies = replies;
		this.content = content;
		this.user = user;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public Comment getParent() {
		return parent;
	}

	public void setParent(Comment parent) {
		this.parent = parent;
	}

	public List<Comment> getReplies() {
		return replies;
	}

	public void setReplies(List<Comment> replies) {
		this.replies = replies;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", post=" + post + ", parent=" + parent + ", replies=" + replies + ", content="
				+ content + ", user=" + user + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + "]";
	}
}

