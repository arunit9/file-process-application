package com.app.fileprocess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.app.fileprocess.queue.FileMetadata;
import com.app.fileprocess.queue.FileQueue;

/**
 * Test class for FileQueue
 * 
 * <P>Tests for order of elements in the FileQueue and handling of duplicates
 * 
 * @author arunitillekeratne
 * @version 1.0
 *
 */
public class FileQueueTest {

	private FileQueue fileQueue;

	@BeforeEach
    void clearQueue() {
		fileQueue = null;
    }

	/**
	 * Test that the queue maintains the order of the elements added
	 */
	@Test
	public void queueOrder_Test() {
		fileQueue = new FileQueue();
		FileMetadata fileMetadata1 = new FileMetadata("notes1.opi", "arunit");
		FileMetadata fileMetadata2 = new FileMetadata("notes2.opi", "JohnD");
		FileMetadata fileMetadata3 = new FileMetadata("notes3.opi", "JaneD");
		fileQueue.add(fileMetadata1);
		fileQueue.add(fileMetadata2);
		fileQueue.add(fileMetadata3);

		FileMetadata actualFileMetadata1 = fileQueue.get();
		FileMetadata actualFileMetadata2 = fileQueue.get();
		FileMetadata actualFileMetadata3 = fileQueue.get();

		assertEquals(fileMetadata1.getFilename(), actualFileMetadata1.getFilename());
		assertEquals(fileMetadata1.getUsername(), actualFileMetadata1.getUsername());

		assertEquals(fileMetadata2.getFilename(), actualFileMetadata2.getFilename());
		assertEquals(fileMetadata2.getUsername(), actualFileMetadata2.getUsername());

		assertEquals(fileMetadata3.getFilename(), actualFileMetadata3.getFilename());
		assertEquals(fileMetadata3.getUsername(), actualFileMetadata3.getUsername());
		
	}

	/**
	 * Test adding a duplicate filename. The first element with same file name
	 * is removed and the elements in the queue move forward maintaining order
	 */
	@Test
	public void queueOrder_WithDuplicates_Test() {
		fileQueue = new FileQueue();
		FileMetadata fileMetadata1 = new FileMetadata("notes1.opi", "arunit");
		FileMetadata fileMetadata2 = new FileMetadata("notes2.opi", "JohnD");
		FileMetadata fileMetadata3 = new FileMetadata("notes3.opi", "JaneD");
		FileMetadata fileMetadata4 = new FileMetadata("notes1.opi", "JaneA");
		fileQueue.add(fileMetadata1);
		fileQueue.add(fileMetadata2);
		fileQueue.add(fileMetadata3);
		fileQueue.add(fileMetadata4);

		FileMetadata actualFileMetadata1 = fileQueue.get();
		FileMetadata actualFileMetadata2 = fileQueue.get();
		FileMetadata actualFileMetadata3 = fileQueue.get();
		FileMetadata actualFileMetadata4 = fileQueue.get();

		assertNotEquals(fileMetadata1.getFilename(), actualFileMetadata1.getFilename());
		assertNotEquals(fileMetadata1.getUsername(), actualFileMetadata1.getUsername());

		assertEquals(fileMetadata2.getFilename(), actualFileMetadata1.getFilename());
		assertEquals(fileMetadata2.getUsername(), actualFileMetadata1.getUsername());

		assertEquals(fileMetadata3.getFilename(), actualFileMetadata2.getFilename());
		assertEquals(fileMetadata3.getUsername(), actualFileMetadata2.getUsername());

		assertEquals(fileMetadata4.getFilename(), actualFileMetadata3.getFilename());
		assertEquals(fileMetadata4.getUsername(), actualFileMetadata3.getUsername());

		// Since an element was removed , there is no fourth element in the queue
		assertNull(actualFileMetadata4);
	}

}
