package com.example.community.rest.community_rest.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.community.rest.community_rest.comment.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostIdAndParentIsNull(Integer postId);
}
