package com.app.fileprocess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

	@InjectMocks // auto inject helloRepository
    private FileProcessorService fileProcessorService;

	@BeforeEach
    void setMockOutput() {
    	MockitoAnnotations.initMocks(this);
//    	List<String> statistics = new ArrayList<String>();
//    	statistics.add("element1.subelement=1");
//    	statistics.add("element2.subelement=1");
    	User user = new User();
    	user.setId(1L);
    	user.setUsername("arunit");
    	user.setPassword("password");

    	UserFile userFile = new UserFile();
    	userFile.setFilename("notes.opi");
    	userFile.setId(1L);
    	Set<UserFile> userFiles = new HashSet<>();
    	userFiles.add(userFile);
    	user.setUserFiles(userFiles);

    	Set<Statistic> stats = new HashSet<Statistic>();
    	Statistic stat1 = new Statistic();
    	stat1.setId(1L);
    	stat1.setUserFile(userFile);
    	stat1.setName("heading.body");
    	stat1.setValue(3L);
    	stats.add(stat1);

    	Statistic stat2 = new Statistic();
    	stat2.setId(1L);
    	stat2.setUserFile(userFile);
    	stat2.setName("heading.body.name");
    	stat2.setValue(6L);
    	stats.add(stat2);
//    	user.setStatistics(stats);

    	List<Statistic> statistics = new ArrayList<Statistic>();
    	statistics.add(stat1);
    	statistics.add(stat2);

    	List<Object[]> groupedElements =  new ArrayList<Object[]>();
    	Object[] objectArray1 = { "heading.body", 3 };
    	Object[] objectArray2 = { "heading.body.name", 6 };
    	groupedElements.add(objectArray1);
    	groupedElements.add(objectArray2);

    	Mockito.when(userJpaRepository.findByUsername(any(String.class))).thenReturn(user);
    	Mockito.when(userFileJpaRepository.findByFilename(any(String.class))).thenReturn(userFile);
//    	Mockito.when(userFileJpaRepository.deleteByFilename(any(String.class)))
//    	Mockito.when(statisticJpaRepository.findByFilename(any(String.class))).thenReturn(statistics);
 //   	Mockito.when(em.createQuery(any(String.class)).getResultList()).thenReturn(groupedElements);
    	
//        when(helloRepository.get()).thenReturn("Hello Mockito From Repository");
    }
	@Test
    public void listFilesByUser_Test()
      throws Exception {
		Set<UserFile> filenames = fileProcessorService.listFilesByUser("arunit");
//FIXME
		//		assertEquals("notes.opi", filenames.);
       //given(fileProcessorService.getAllElementsByFilename(any(String.class)).thenReturn(statistics);
    }

	@Test
	public void getAllElementsByFilename_Test() {
		// FIXME
//		List<String> stats = fileProcessorService.getStatistics("notes1.opi", "arunit");
//		assertEquals("heading.body = 3", stats.get(0));
//		assertEquals("heading.body.name = 6", stats.get(1));
	}

	@Test
	public void deleteFile_Test() {
//		fileProcessorService.deleteFileByFilename("notes1.opi");
//		assertTrue(true);
//		verify(userFileJpaRepository,times(1)).deleteByFilename("notes1.opi");
	}

	@Test
	public void addToQueue_Test() {
		String filename = "notes1.opi";
		String username = "arunit";
		FileMetadata fileMetadata = new FileMetadata(filename, username);
		fileProcessorService.addToQueue(filename, username);
		assertTrue(true);
		verify(fileQueue,times(1)).add(fileMetadata);
	}

//	@Test
//	public void getAllElementsByUser_Test() {
//		List<String> stats = fileProcessorService.getAllElementsByUser("arunit");
//		assertEquals("heading.body = 3", stats.get(0));
//		assertEquals("heading.body.name = 6", stats.get(1));
//	}

}
