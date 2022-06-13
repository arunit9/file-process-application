package com.app.fileprocess.dao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.fileprocess.dao.entity.ERole;
import com.app.fileprocess.dao.entity.Role;

public interface RoleJpaRepository extends JpaRepository<Role, Long>{
	Optional<Role> findByName(ERole name);
}
