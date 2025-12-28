package com.blog.app.services;

import com.blog.app.dto.CommentDTO;
import com.blog.app.entity.Comment;
import com.blog.app.entity.Post;
import com.blog.app.entity.User;
import com.blog.app.repositories.CommentRepository;
import com.blog.app.repositories.PostRepository;
import com.blog.app.repositories.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final UserRepository userRepository;

	public CommentService(CommentRepository commentRepository, PostRepository postRepository,
			UserRepository userRepository) {
		this.commentRepository = commentRepository;
		this.postRepository = postRepository;
		this.userRepository = userRepository;
	}

	// Create comment
	public CommentDTO createComment(CommentDTO commentDTO, Long postId, Long userId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		Comment comment = new Comment();
		comment.setContent(commentDTO.getContent());
		comment.setPost(post);
		comment.setUser(user);

		Comment saved = commentRepository.save(comment);
		return mapToDTO(saved);
	}

	// Update comment
	public CommentDTO updateComment(CommentDTO commentDTO) {
		Comment comment = commentRepository.findById(commentDTO.getId())
				.orElseThrow(() -> new RuntimeException("Comment not found"));
		comment.setContent(commentDTO.getContent());
		Comment updated = commentRepository.save(comment);
		return mapToDTO(updated);
	}

	// Delete comment
	public void deleteComment(Long commentId) {
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new RuntimeException("Comment not found"));
		commentRepository.delete(comment);
	}

	// Get single comment
	public CommentDTO getCommentById(Long commentId) {
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new RuntimeException("Comment not found"));
		return mapToDTO(comment);
	}

	// Get all comments for a post
	public List<CommentDTO> getCommentsByPost(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
		return post.getComments().stream().map(this::mapToDTO).collect(Collectors.toList());
	}

	// Get all comments for a user
	public List<CommentDTO> getCommentsByUser(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		return user.getComments().stream().map(this::mapToDTO).collect(Collectors.toList());
	}

	private CommentDTO mapToDTO(Comment comment) {
		CommentDTO dto = new CommentDTO();
		dto.setId(comment.getId());
		dto.setContent(comment.getContent());
		return dto;
	}
}
