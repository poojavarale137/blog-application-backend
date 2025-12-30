package com.blog.app.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.blog.app.constants.AppConstants;
import com.blog.app.entity.Role;
import com.blog.app.repositories.RoleRepository;

@Component
public class RoleInitializer implements CommandLineRunner {

	private final RoleRepository roleRepository;

	public RoleInitializer(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	public void run(String... args) throws Exception {

		// Check if ROLE_USER exists, if not create it
		if (!roleRepository.existsByName(AppConstants.ROLE_USER)) {
			roleRepository.save(new Role(AppConstants.ROLE_USER));
		}

		// Check if ROLE_ADMIN exists, if not create it
		if (!roleRepository.existsByName(AppConstants.ROLE_ADMIN)) {
			roleRepository.save(new Role(AppConstants.ROLE_ADMIN));
		}
	}
}
