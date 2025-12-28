package com.blog.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.app.entity.Category;
import com.blog.app.entity.Post;
import com.blog.app.entity.User;

public interface PostRepository extends JpaRepository<Post, Long> {

	List<Post> findAllByUser(User user);

	List<Post> findAllByCategory(Category category);

	List<Post> findByTitleContainingIgnoreCase(String title);
}
