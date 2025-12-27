package com.blog.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.app.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
