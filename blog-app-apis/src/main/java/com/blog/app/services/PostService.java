
package com.blog.app.services;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blog.app.constants.AppConstants;
import com.blog.app.dto.CategoryDTO;
import com.blog.app.dto.CommentDTO;
import com.blog.app.dto.PostDTO;
import com.blog.app.dto.PostResponse;
import com.blog.app.dto.UserDTO;
import com.blog.app.entity.Category;
import com.blog.app.entity.Post;
import com.blog.app.entity.User;
import com.blog.app.repositories.CategoryRepository;
import com.blog.app.repositories.PostRepository;
import com.blog.app.repositories.UserRepository;
import com.blog.app.services.PostService;

@Service
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final CategoryRepository categoryRepository;

	public PostService(PostRepository postRepository, UserRepository userRepository,
			CategoryRepository categoryRepository) {
		this.postRepository = postRepository;
		this.userRepository = userRepository;
		this.categoryRepository = categoryRepository;
	}

	// CREATE POST

	public PostDTO createPost(PostDTO postDto, Long userId, Long categoryId) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException(AppConstants.USER_NOT_FOUND + userId));

		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new RuntimeException(AppConstants.CATEGORY_NOT_FOUND + categoryId));

		Post post = new Post();
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImageName(postDto.getImageName());
		post.setAddedDate(new Date());
		post.setUser(user);
		post.setCategory(category);

		return mapToDto(postRepository.save(post));
	}

	// UPDATE POST

	public PostDTO updatePost(PostDTO postDto) {

		Post post = postRepository.findById(postDto.getId()).orElseThrow(() -> new RuntimeException("Post not found"));

		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImageName(postDto.getImageName());
		return mapToDto(postRepository.save(post));
	}

	// DELETE POST

	public void deletePost(Long postId) {
		postRepository.deleteById(postId);
	}

	// GET ALL POSTS
	public PostResponse getAllPosts(int pageNumber, int pageSize, String sortBy, String sortDir) {

		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		Page<Post> pagePost = postRepository.findAll(pageable);

		List<PostDTO> content = pagePost.getContent().stream().map(this::mapToDto).collect(Collectors.toList());

		PostResponse postResponse = new PostResponse();
		postResponse.setContent(content);
		postResponse.setPageNumber(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalElements(pagePost.getTotalElements());
		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setLast(pagePost.isLast());

		return postResponse;
	}

	// GET SINGLE POST

	public PostDTO getPostById(Long postId) {
		return mapToDto(postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found")));
	}

	// POSTS BY CATEGORY

	public List<PostDTO> getPostsByCategory(Long categoryId) {

		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new RuntimeException("Category not found"));

		return postRepository.findAllByCategory(category).stream().map(this::mapToDto).collect(Collectors.toList());
	}

	// POSTS BY USER

	public List<PostDTO> getPostsByUser(Long userId) {

		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		return postRepository.findAllByUser(user).stream().map(this::mapToDto).collect(Collectors.toList());
	}

	// SEARCH POSTS

	public List<PostDTO> searchPosts(String keyword) {
		List<Post> posts = postRepository.findByTitleContainingIgnoreCase(keyword);

		return posts.stream().map(this::mapToDto).collect(Collectors.toList());
	}

	private PostDTO mapToDto(Post post) {

		PostDTO dto = new PostDTO();
		dto.setId(post.getId());
		dto.setTitle(post.getTitle());
		dto.setContent(post.getContent());
		dto.setImageName(post.getImageName());
		dto.setAddedDate(post.getAddedDate());

		// Category mapping
		CategoryDTO categoryDto = new CategoryDTO();
		categoryDto.setId(post.getCategory().getId());
		categoryDto.setTitle(post.getCategory().getTitle());
		categoryDto.setDescription(post.getCategory().getDescription());
		dto.setCategory(categoryDto);

		// User mapping
		UserDTO userDto = new UserDTO();
		userDto.setId(post.getUser().getId());
		userDto.setName(post.getUser().getName());
		userDto.setEmail(post.getUser().getEmail());
		userDto.setAbout(post.getUser().getAbout());
		userDto.setPassword(post.getUser().getPassword());
		dto.setUser(userDto);

		if (post.getComments() != null) {
			Set<CommentDTO> commentDTOs = post.getComments().stream().map(comment -> {
				CommentDTO commentDTO = new CommentDTO();
				commentDTO.setId(comment.getId());
				commentDTO.setContent(comment.getContent());
				return commentDTO;
			}).collect(Collectors.toSet());
			dto.setComments(commentDTOs);
		}
		return dto;
	}

}
