package com.app.fileprocess.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.fileprocess.dao.entity.UserFile;

@Repository
public interface UserFileJpaRepository extends JpaRepository<UserFile, Long>{

	public void deleteByFilename(String filename);

	public UserFile findByFilename(String filename);

	Boolean existsByFilename(String filename);

}
