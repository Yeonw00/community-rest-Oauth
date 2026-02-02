package com.example.community.rest.community_rest.comment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // 댓글 등록 (부모 댓글 ID 없으면 일반 댓글, 있으면 대댓글)
    @PostMapping
    public ResponseEntity<Comment> createComment(
            @PathVariable Integer postId,
            @RequestParam(required = false) Long parentId,
            @RequestBody CommentRequest request) {

        Comment savedComment = commentService.createComment(postId, parentId, request);
        return ResponseEntity.ok(savedComment);
    }

    // 특정 게시글의 전체 댓글 조회 (트리 형태로 반환 가능)
    @GetMapping
    public ResponseEntity<List<CommentResponse>> getCommentsByPost(@PathVariable Integer postId) {
        List<CommentResponse> comments = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(comments);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable Integer postId,
            @PathVariable Long commentId,
            @RequestBody CommentRequest request) {

        Comment updatedComment = commentService.updateComment(postId, commentId, request);
        return ResponseEntity.ok(updatedComment);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Integer postId,
            @PathVariable Long commentId) {

        commentService.deleteComment(postId, commentId);
        return ResponseEntity.noContent().build();
    }
}

