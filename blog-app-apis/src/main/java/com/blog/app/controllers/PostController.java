package com.blog.app.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;

import com.blog.app.constants.AppConstants;
import com.blog.app.dto.PostDTO;
import com.blog.app.dto.PostResponse;
import com.blog.app.services.FileService;
import com.blog.app.services.PostService;

@RestController
@RequestMapping("/api/posts")
public class PostController {

	@Value("${project.image}")
    private String imagePath;

	private final PostService postService;
	private final FileService fileService;

	public PostController(PostService postService, FileService fileService) {
		this.postService = postService;
		this.fileService = fileService;
	}

	// CREATE POST
	@PostMapping("/user/{userId}/category/{categoryId}")
	public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDto, @PathVariable Long userId,
			@PathVariable Long categoryId) {

		return new ResponseEntity<>(postService.createPost(postDto, userId, categoryId), HttpStatus.CREATED);
	}

	// UPDATE POST
	@PutMapping
	public PostDTO updatePost(@RequestBody PostDTO postDto) {

		return postService.updatePost(postDto);
	}

	// DELETE POST
	@DeleteMapping("/{postId}")
	public void deletePost(@PathVariable Long postId) {
		postService.deletePost(postId);
	}

	// GET ALL POSTS
	@GetMapping
	public ResponseEntity<PostResponse> getAllPosts(
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER + "") int pageNumber,
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE + "") int pageSize,
			@RequestParam(defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
			@RequestParam(defaultValue = AppConstants.DEFAULT_SORT_DIR) String sortDir) {

		PostResponse postResponse = postService.getAllPosts(pageNumber, pageSize, sortBy, sortDir);
		return ResponseEntity.ok(postResponse);
	}

	// GET SINGLE POST
	@GetMapping("/{postId}")
	public PostDTO getPostById(@PathVariable Long postId) {
		return postService.getPostById(postId);
	}

	// POSTS BY CATEGORY
	@GetMapping("/category/{categoryId}")
	public List<PostDTO> getPostsByCategory(@PathVariable Long categoryId) {
		return postService.getPostsByCategory(categoryId);
	}

	// POSTS BY USER
	@GetMapping("/user/{userId}")
	public List<PostDTO> getPostsByUser(@PathVariable Long userId) {
		return postService.getPostsByUser(userId);
	}

	@GetMapping("/search/{keyword}")
	public ResponseEntity<List<PostDTO>> searchPosts(@PathVariable String keyword) {
		List<PostDTO> result = postService.searchPosts(keyword);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/image/upload/{postId}")
	public ResponseEntity<PostDTO> uploadImage(@RequestParam("image") MultipartFile image, @PathVariable Long postId)
			throws IOException {

		PostDTO postDTO = postService.getPostById(postId);
		String fileName = fileService.uploadImage(image, postId);

		postDTO.setImageName(fileName);
		postDTO.setId(postId);
		PostDTO updatedPost = postService.updatePost(postDTO);

		return new ResponseEntity<>(updatedPost, HttpStatus.OK);
	}
	@GetMapping("/image/{fileName}")
    public ResponseEntity<Resource> serveImage(@PathVariable String fileName) throws IOException {
        Path path = Paths.get(imagePath).resolve(fileName).normalize();
        Resource resource = new UrlResource(path.toUri());

        String contentType = Files.probeContentType(path);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}
