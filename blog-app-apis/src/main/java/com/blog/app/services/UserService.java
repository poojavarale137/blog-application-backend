package com.blog.app.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.blog.app.dto.UserDTO;
import com.blog.app.entity.User;
import com.blog.app.repositories.UserRepository;

@Service
public class UserService {

	private UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public UserDTO saveUser(UserDTO userDto) {
		User user = toEntity(userDto);
		User savedUser = userRepository.save(user);
		return toDto(savedUser);
	}

	public List<UserDTO> getAllUsers() {
		return userRepository.findAll().stream().map(this::toDto).toList();
	}

	public UserDTO getUserById(Long id) {
		return userRepository.findById(id).map(this::toDto).orElse(null);
	}

	public UserDTO updateUser(UserDTO userDto) {

		User existingUser = userRepository.findById(userDto.getId())
				.orElseThrow(() -> new RuntimeException("User not found with id: " + userDto.getId()));

		existingUser.setName(userDto.getName());
		existingUser.setEmail(userDto.getEmail());
		existingUser.setPassword(userDto.getPassword());
		existingUser.setAbout(userDto.getAbout());

		User updatedUser = userRepository.save(existingUser);

		return toDto(updatedUser);
	}

	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}

	private UserDTO toDto(User user) {
		UserDTO dto = new UserDTO();
		dto.setId(user.getId());
		dto.setName(user.getName());
		dto.setEmail(user.getEmail());
		dto.setPassword(user.getPassword());
		dto.setAbout(user.getAbout());
		return dto;
	}

	private User toEntity(UserDTO dto) {
		User user = new User();
		user.setId(dto.getId());
		user.setName(dto.getName());
		user.setEmail(dto.getEmail());
		user.setPassword(dto.getPassword());
		user.setAbout(dto.getAbout());
		return user;
	}
}
