package com.app.fileprocess.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FileQueue {
	private static Logger logger = LoggerFactory.getLogger(FileQueue.class);
	private BlockingQueue<FileMetadata> fileQueue = new LinkedBlockingQueue<FileMetadata>();

	public synchronized boolean add(FileMetadata fileMetadata) {
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
