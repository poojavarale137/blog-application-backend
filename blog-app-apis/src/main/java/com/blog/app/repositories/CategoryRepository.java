package com.blog.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blog.app.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{

}
