package com.app.fileprocess.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.fileprocess.dao.entity.User;

public interface UserJpaRepository extends JpaRepository<User, Long>{
	User findByUsername(String username);
	Boolean existsByUsername(String username);
}
