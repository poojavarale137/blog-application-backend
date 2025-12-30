package com.blog.app.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.blog.app.constants.AppConstants;
import com.blog.app.dto.RoleDTO;
import com.blog.app.dto.UserDTO;
import com.blog.app.entity.Role;
import com.blog.app.entity.User;
import com.blog.app.repositories.RoleRepository;
import com.blog.app.repositories.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final RoleRepository roleRepository;

	public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public UserDTO saveUser(UserDTO userDto) {
		User user = toEntity(userDto);
		// user.setPassword(passwordEncoder.encode(userDto.getPassword()));
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
		// existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
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

	public UserDTO registerUser(UserDTO userDTO) {

		User user = new User();
		user.setName(userDTO.getName());
		user.setEmail(userDTO.getEmail());
		user.setAbout(userDTO.getAbout());
		user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

		// Assign ROLE_USER correctly
		Role userRole = roleRepository.findByName(AppConstants.ROLE_USER)
				.orElseThrow(() -> new RuntimeException("ROLE_USER not found"));

		user.setRoles(Set.of(userRole));

		User savedUser = userRepository.save(user);
		return toUserDto(savedUser);
	}

	private UserDTO toUserDto(User user) {
		UserDTO dto = new UserDTO();
		dto.setId(user.getId());
		dto.setName(user.getName());
		dto.setEmail(user.getEmail());
		dto.setAbout(user.getAbout());

		Set<RoleDTO> roles = user.getRoles().stream().map(role -> new RoleDTO(role.getId(), role.getName()))
				.collect(Collectors.toSet());
		dto.setRoles(roles);

		return dto;
	}

}
