package com.app.fileprocess.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * StorageService
 * 
 * <P>Interface for accessing storage
 * 
 * @author arunitillekeratne
 * @version 1.0
 *
 */
public interface StorageService {
	void init();

	/**
	 * Store file
	 * 
	 * @param file
	 */
	void store(MultipartFile file);

	/**
	 * Load all files on the server
	 * 
	 * @return
	 */
	Stream<Path> loadAll();

	/**
	 * Load a file on the server
	 * 
	 * @param filename
	 * @return
	 */
	Path load(String filename);

	/**
	 * Load file as a resource
	 * 
	 * @param filename
	 * @return
	 */
	Resource loadAsResource(String filename);

	/**
	 * Delete a file on the server
	 * 
	 * @param filename
	 */
	void deleteFile(String filename);

	/**
	 * Delete all files on the server
	 */
	void deleteAll();

	/**
	 * Convert the file to an object understandable by the processing logic
	 * 
	 * @param filename
	 * @return
	 */
	public Object convertToObject(String filename);
}
