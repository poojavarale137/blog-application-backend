package com.blog.app.services;

import org.springframework.stereotype.Service;

import com.blog.app.dto.CategoryDTO;
import com.blog.app.entity.Category;
import com.blog.app.repositories.CategoryRepository;
import com.blog.app.services.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

	private final CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public CategoryDTO createCategory(CategoryDTO categoryDTO) {
		Category category = mapToEntity(categoryDTO);
		Category saved = categoryRepository.save(category);
		return mapToDTO(saved);
	}

	public CategoryDTO updateCategory(CategoryDTO categoryDTO) {
		Category category = categoryRepository.findById(categoryDTO.getId())
				.orElseThrow(() -> new RuntimeException("Category not found"));
		category.setTitle(categoryDTO.getTitle());
		category.setDescription(categoryDTO.getDescription());
		Category updated = categoryRepository.save(category);
		return mapToDTO(updated);
	}

	public void deleteCategory(Long id) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Category not found"));
		categoryRepository.delete(category);
	}

	public CategoryDTO getCategoryById(Long id) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Category not found"));
		return mapToDTO(category);
	}

	public List<CategoryDTO> getAllCategories() {
		return categoryRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
	}

	private CategoryDTO mapToDTO(Category category) {
		CategoryDTO dto = new CategoryDTO();
		dto.setId(category.getId());
		dto.setTitle(category.getTitle());
		dto.setDescription(category.getDescription());
		return dto;
	}

	private Category mapToEntity(CategoryDTO dto) {
		Category category = new Category();
		category.setId(dto.getId());
		category.setTitle(dto.getTitle());
		category.setDescription(dto.getDescription());
		return category;
	}
}
