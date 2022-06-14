package com.app.fileprocess.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * The internal processing queue
 * 
 * <P>This queue is used to hold file metadata objects until they can be
 * asynchronously processed
 * 
 * <P>A file metadata being added to the queue having same filename as an existing
 * object in the queue will remove the existing object and add the new object
 * to the end of the queue
 * 
 * @author arunitillekeratne
 * @version 1.0
 *
 */
@Component
public class FileQueue {
	private static Logger logger = LoggerFactory.getLogger(FileQueue.class);
	private static BlockingQueue<FileMetadata> fileQueue = new LinkedBlockingQueue<FileMetadata>();

	public synchronized boolean add(FileMetadata fileMetadata) {
		// remove existing object if filename is same as in the new object
		boolean fileExists = fileQueue.remove(fileMetadata);
		if (fileExists) {
			logger.info("removed duplicate file before adding the latest");
		}
		return fileQueue.offer(fileMetadata);
	}

	public synchronized FileMetadata get() {

		return fileQueue.poll();

	}
}
