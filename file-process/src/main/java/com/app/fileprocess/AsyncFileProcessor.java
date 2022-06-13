package com.app.fileprocess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.app.fileprocess.async.service.AsyncFileProcessorService;
import com.app.fileprocess.queue.FileMetadata;
import com.app.fileprocess.queue.FileQueue;
import com.app.fileprocess.storage.StorageService;

@Component
public class AsyncFileProcessor implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(AsyncFileProcessor.class);

	@Autowired
    private AsyncFileProcessorService asyncFileProcessorService;

	@Autowired
	private FileQueue fileQueue;

	@Autowired
	StorageService storageService;

	@Override
	public void run(String... args) throws Exception {
		storageService.deleteAll();
	    storageService.init();
		while(true) {
			FileMetadata fileMetadata = fileQueue.get();
			if (fileMetadata != null) {
				logger.debug(String.format("Start processing of file %s submitted by %s",
						fileMetadata.getFilename(), fileMetadata.getUsername()));
				asyncFileProcessorService.saveUserFiles(fileMetadata);
			}
		}
	
	}
}
