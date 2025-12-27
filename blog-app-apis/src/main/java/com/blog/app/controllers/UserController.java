package com.blog.app.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.app.dto.UserDTO;
import com.blog.app.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	public UserDTO createUser(@Valid @RequestBody UserDTO userDto) {
		return userService.saveUser(userDto);
	}

	@PutMapping
	public UserDTO updateUser(@Valid @RequestBody UserDTO userDto) {
		return userService.updateUser(userDto);
	}

	@GetMapping
	public List<UserDTO> getUsers() {
		return userService.getAllUsers();
	}

	@GetMapping("/{id}")
	public UserDTO getUser(@PathVariable Long id) {
		return userService.getUserById(id);
	}

	@DeleteMapping("/{id}")
	public String deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return "User deleted successfully";
	}
}
