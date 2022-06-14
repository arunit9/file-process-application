package com.app.fileprocess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.app.fileprocess.dao.entity.Statistic;
import com.app.fileprocess.dao.entity.User;
import com.app.fileprocess.dao.entity.UserFile;
import com.app.fileprocess.dao.repository.StatisticJpaRepository;
import com.app.fileprocess.dao.repository.UserFileJpaRepository;
import com.app.fileprocess.dao.repository.UserJpaRepository;
import com.app.fileprocess.queue.FileMetadata;
import com.app.fileprocess.queue.FileQueue;
import com.app.fileprocess.service.FileProcessorService;

/**
 * Test class for FileProcessorService
 * 
 * <P>Tests listing files and statistics, deleting files and associated statistics
 * and adding elements to the queue for asynchronous processing
 * 
 * @author arunitillekeratne
 * @version 2.0
 *
 */
public class FileProcessorServiceTest {
	@Mock
    private StatisticJpaRepository statisticJpaRepository;

	@Mock
    private UserJpaRepository userJpaRepository;

	@Mock
	private UserFileJpaRepository userFileJpaRepository;

	@Mock
	private FileQueue fileQueue;

	@Mock
	@PersistenceContext
    private EntityManager em;

	@InjectMocks
    private FileProcessorService fileProcessorService;

	@BeforeEach
    void setMockOutput() {
    	MockitoAnnotations.initMocks(this);

    	User user = new User();
    	user.setId(1L);
    	user.setUsername("arunit");
    	user.setPassword("password");

    	UserFile userFile1 = new UserFile();
    	userFile1.setUser(user);
    	userFile1.setFilename("notes1.opi");
    	userFile1.setId(1L);

    	UserFile userFile2 = new UserFile();
    	userFile2.setUser(user);
    	userFile2.setFilename("notes2.opi");
    	userFile2.setId(2L);

    	Set<UserFile> userFiles = new HashSet<>();
    	userFiles.add(userFile1);
    	userFiles.add(userFile2);
    	user.setUserFiles(userFiles);

    	Set<Statistic> stats = new HashSet<Statistic>();
    	Statistic stat1 = new Statistic();
    	stat1.setId(1L);
    	stat1.setUserFile(userFile1);
    	stat1.setName("heading.body");
    	stat1.setValue(3L);
    	stats.add(stat1);

    	Statistic stat2 = new Statistic();
    	stat2.setId(2L);
    	stat2.setUserFile(userFile1);
    	stat2.setName("heading.body.name");
    	stat2.setValue(6L);
    	stats.add(stat2);
    	user.setStatistics(stats);
    	userFile1.setStatistics(stats);

    	Mockito.when(userJpaRepository.findByUsername(any(String.class))).thenReturn(user);
    	Mockito.when(userFileJpaRepository.findByFilename("notes1.opi")).thenReturn(userFile1);
 	
	}

	/**
	 * Tests the files are retrieved by the username of the user that uploaded them
	 * @throws Exception
	 */
	@Test
    public void listFilesByUser_Test()
      throws Exception {
		Set<UserFile> files = fileProcessorService.listFilesByUser("arunit");
		Map<Long, String> expectedFiles = new HashMap<Long, String>();
		expectedFiles.put(1L, "notes1.opi");
		expectedFiles.put(2L, "notes2.opi");

		files.forEach( (file) -> { 
			assertEquals(expectedFiles.get(file.getId()), file.getFilename());
			assertEquals("arunit", file.getUser().getUsername());
		});
	}

	/**
	 * Tests that statistics linked to a file are returned
	 */
	@Test
	public void getAllElementsByFilename_Test() {
		Set<Statistic> statistics = fileProcessorService.getStatisticsByFilename("notes1.opi");

		Statistic stat1 = new Statistic();
    	stat1.setId(1L);
    	stat1.setName("heading.body");
    	stat1.setValue(3L);

    	Statistic stat2 = new Statistic();
    	stat2.setId(2L);
    	stat2.setName("heading.body.name");
    	stat2.setValue(6L);

		Map<Long, Statistic> expectedFiles = new HashMap<Long, Statistic>();
		expectedFiles.put(1L, stat1);
		expectedFiles.put(2L, stat2);

		statistics.forEach( (statistic) -> { 
			assertEquals(expectedFiles.get(statistic.getId()).getName(), statistic.getName());
			assertEquals(expectedFiles.get(statistic.getId()).getValue(), statistic.getValue());
		});

	}

	/**
	 * Tests file deletion
	 */
	@Test
	public void deleteFile_Test() {
		fileProcessorService.deleteFileByFilename("arunit","notes1.opi");
		
		verify(userJpaRepository,times(1)).findByUsername("arunit");
		verify(userJpaRepository,times(1)).save(any(User.class));

	}

	/**
	 * Tests file deletion logic when file does not exist
	 */
	@Test
	public void deleteFile_FileNotExists_Test() {
		fileProcessorService.deleteFileByFilename("arunit","notes2.opi");
		User user = verify(userJpaRepository,times(0)).findByUsername("arunit");
		verify(userJpaRepository,times(0)).save(user);
	}

	/**
	 * Tests adding metadata of files to be processed to an internal queue
	 */
	@Test
	public void addToQueue_Test() {
		String filename = "notes1.opi";
		String username = "arunit";
		FileMetadata fileMetadata = new FileMetadata(filename, username);
		fileProcessorService.addToQueue(filename, username);
		assertTrue(true);
		verify(fileQueue,times(1)).add(fileMetadata);
	}

}
