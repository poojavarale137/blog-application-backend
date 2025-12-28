package com.blog.app.controllers;

import com.blog.app.dto.CommentDTO;
import com.blog.app.services.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

	private final CommentService commentService;

	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

	// Create comment
	@PostMapping("/post/{postId}/user/{userId}")
	public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO commentDTO, @PathVariable Long postId,
			@PathVariable Long userId) {
		return ResponseEntity.ok(commentService.createComment(commentDTO, postId, userId));
	}

	// Update comment
	@PutMapping
	public ResponseEntity<CommentDTO> updateComment(@RequestBody CommentDTO commentDTO) {
		return ResponseEntity.ok(commentService.updateComment(commentDTO));
	}

	// Delete comment
	@DeleteMapping("/{commentId}")
	public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
		commentService.deleteComment(commentId);
		return ResponseEntity.noContent().build();
	}

	// Get single comment
	@GetMapping("/{commentId}")
	public ResponseEntity<CommentDTO> getCommentById(@PathVariable Long commentId) {
		return ResponseEntity.ok(commentService.getCommentById(commentId));
	}

	// Get all comments for a post
	@GetMapping("/post/{postId}")
	public ResponseEntity<List<CommentDTO>> getCommentsByPost(@PathVariable Long postId) {
		return ResponseEntity.ok(commentService.getCommentsByPost(postId));
	}

	// Get all comments for a user
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<CommentDTO>> getCommentsByUser(@PathVariable Long userId) {
		return ResponseEntity.ok(commentService.getCommentsByUser(userId));
	}
}
