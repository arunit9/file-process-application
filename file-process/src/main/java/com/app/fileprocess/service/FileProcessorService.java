package com.app.fileprocess.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.app.fileprocess.dao.entity.Statistic;
import com.app.fileprocess.dao.entity.User;
import com.app.fileprocess.dao.entity.UserFile;
import com.app.fileprocess.dao.repository.UserFileJpaRepository;
import com.app.fileprocess.dao.repository.UserJpaRepository;
import com.app.fileprocess.queue.FileMetadata;
import com.app.fileprocess.queue.FileQueue;

@Component
public class FileProcessorService {

	@Autowired
    private UserFileJpaRepository userFileJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
	private FileQueue fileQueue;

    @PersistenceContext
    private EntityManager em;

    public boolean checkUserExists(String username) {
    	return userJpaRepository.existsByUsername(username);
    }

    public boolean checkUserFileExists(String filename) {
    	return userFileJpaRepository.existsByFilename(filename);
    }

    public Set<UserFile> listFilesByUser(String username) {
    	User user = userJpaRepository.findByUsername(username);
    	return user.getUserFiles();
    }

    @Transactional
    public void deleteFileByFilename(String username, String filename) {
    	UserFile userFile = userFileJpaRepository.findByFilename(filename);
    	if (userFile != null) {
	    	User user = userJpaRepository.findByUsername(username);
	    	user.getUserFiles().remove(userFile);
	    	user.getStatistics().removeAll(userFile.getStatistics());
	    	userJpaRepository.save(user);
    	}
    }

    @Transactional
    public void deleteFile(Long id) {
    	userFileJpaRepository.deleteById(id);
    }

    public boolean addToQueue(String filename, String username) {
    	FileMetadata fileMetadata = new FileMetadata(filename, username);
    	return fileQueue.add(fileMetadata);
    }

    @SuppressWarnings("unchecked")
    public Set<Statistic> getStatistics(String username) {
    	Set<Statistic> statistics = new HashSet<Statistic>();
    	List<Object[]>  groupedElements = em.createQuery(
    	        String.format("SELECT s.name, SUM(s.value) FROM User u "
    	        		+ "JOIN u.statistics s WHERE u.username = '%s' GROUP BY s.name", username))
    	        .getResultList();
    	groupedElements.forEach( (groupedElement) -> 
    		{ statistics.add(new Statistic((String)groupedElement[0], (Long)groupedElement[1])); } );
    	return statistics;
    }

    public Set<Statistic> getStatistics(Long id) {

    	UserFile userFile = userFileJpaRepository.findById(id).orElse(null);
    	
    	return userFile.getStatistics();
    }
 
    public Set<Statistic> getStatisticsByFilename(String filename) {

    	UserFile userFile = userFileJpaRepository.findByFilename(filename);
    	
    	return userFile.getStatistics();
    }

}
