package com.example.community.rest.community_rest.file;

import java.time.LocalDateTime;

import com.example.community.rest.community_rest.post.Post;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Upload {
	@Id
	@GeneratedValue
	private int id;
	@Column(name = "file_name")
	private String fileName;
	private String uuid;
	private String username;
	@Column(name = "upload_date")
	private LocalDateTime uploadDate;
	@Column(name = "modified_date")
	private LocalDateTime modifiedDate;
	private String type;
	private long size;
	private String extension;
	private String filePath;
	private String thumbFilePath;
	
	@ManyToOne
	@JoinColumn(name = "post_id")
	@JsonBackReference
	private Post post;
	
	public Upload() {
		
	}
	
	public Upload(String fileName, String uuid, String username, LocalDateTime uploadDate,
			LocalDateTime modifiedDate, String type, long size, String extension, String filePath) {
		super();
		this.fileName = fileName;
		this.uuid = uuid;
		this.username = username;
		this.uploadDate = uploadDate;
		this.modifiedDate = modifiedDate;
		this.type = type;
		this.size = size;
		this.extension = extension;
		this.filePath = filePath;
	}
	
	
	public Upload(int id, String fileName, String uuid, String username, LocalDateTime uploadDate,
			LocalDateTime modifiedDate, String type, long size, String extension, String filePath, String thumbFilePath) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.uuid = uuid;
		this.username = username;
		this.uploadDate = uploadDate;
		this.modifiedDate = modifiedDate;
		this.type = type;
		this.size = size;
		this.extension = extension;
		this.filePath = filePath;
		this.thumbFilePath = thumbFilePath;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	public LocalDateTime getUploadDate() {
		return uploadDate;
	}
	
	public void setUploadDate(LocalDateTime uploadDate) {
		this.uploadDate = uploadDate;
	}
	
	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}
	
	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
	
	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	    if (!post.getUploads().contains(this)) {
	        post.getUploads().add(this);  // Post의 리스트에도 추가
	    }
	}
	
	public String getThumbFilePath() {
		return thumbFilePath;
	}

	public void setThumbFilePath(String thumbFilePath) {
		this.thumbFilePath = thumbFilePath;
	}

	@Override
	public String toString() {
		return "Upload [id=" + id + ", fileName=" + fileName + ", uuid=" + uuid + ", username=" + username
				+ ", uploadDate=" + uploadDate + ", modifiedDate=" + modifiedDate + ", type=" + type + ", size=" + size
				+ ", extension=" + extension + ", filePath=" + filePath + ", thumbFilePath=" + thumbFilePath + "]";
	}
	
}