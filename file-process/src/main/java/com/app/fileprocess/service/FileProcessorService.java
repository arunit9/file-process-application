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

/**
 * FileProcessorService
 * 
 * <P>Logic for adding/listing/deleting files and statistics via REST methods
 * 
 * @author arunitillekeratne
 * @version 1.0
 *
 */
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

    /**
     * Returns true if user exists on the database
     * 
     * @param username
     * @return
     */
    public boolean checkUserExists(String username) {
    	return userJpaRepository.existsByUsername(username);
    }

    /**
     * Returns true if reference to a file exists on the database
     * 
     * @param filename
     * @return
     */
    public boolean checkUserFileExists(String filename) {
    	return userFileJpaRepository.existsByFilename(filename);
    }

    /**
     * Lists all files uploaded by a user
     * 
     * @param username
     * @return
     */
    public Set<UserFile> listFilesByUser(String username) {
    	User user = userJpaRepository.findByUsername(username);
    	return user.getUserFiles();
    }

    /**
     * Delete file and associated statistics from the database
     * 
     * @param username
     * @param filename
     */
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

    /**
     * Add new files to be uploaded to the queue to be processed asynchronously
     * by a separate thread later
     * 
     * @param filename
     * @param username
     * @return
     */
    public boolean addToQueue(String filename, String username) {
    	FileMetadata fileMetadata = new FileMetadata(filename, username);
    	return fileQueue.add(fileMetadata);
    }

    /**
     * Get statistics for a user
     * 
     * @param username
     * @return
     */
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

    /**
     * Get statistics by file id
     * 
     * @param id
     * @return
     */
    public Set<Statistic> getStatistics(Long id) {

    	UserFile userFile = userFileJpaRepository.findById(id).orElse(null);
    	
    	return userFile.getStatistics();
    }
 
    /**
     * Get statistics by filename
     * 
     * @param filename
     * @return
     */
    public Set<Statistic> getStatisticsByFilename(String filename) {

    	UserFile userFile = userFileJpaRepository.findByFilename(filename);
    	
    	return userFile.getStatistics();
    }

}
