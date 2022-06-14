package com.app.fileprocess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.app.fileprocess.async.service.AsyncFileProcessorService;
import com.app.fileprocess.dao.entity.Statistic;
import com.app.fileprocess.dao.entity.User;
import com.app.fileprocess.dao.entity.UserFile;
import com.app.fileprocess.dao.repository.StatisticJpaRepository;
import com.app.fileprocess.dao.repository.UserFileJpaRepository;
import com.app.fileprocess.dao.repository.UserJpaRepository;
import com.app.fileprocess.queue.FileMetadata;
import com.app.fileprocess.storage.StorageService;

/**
 * Test class for AsyncFileProcessorService
 * 
 * <P>Tests for XML parsing, creation of statistics and saving updating User
 * 
 * @author arunitillekeratne
 * @version 2.0
 *
 */
public class AsyncFileProcessorServiceTest {
	@Mock
    private StatisticJpaRepository statisticJpaRepository;

	@Mock
    private UserJpaRepository userJpaRepository;

	@Mock
    private UserFileJpaRepository userFileJpaRepository;

	@Mock
	private StorageService fileStorageServiceImplementor;

	@InjectMocks
    private AsyncFileProcessorService asyncFileProcessorService;

	final String xmlStr = "<employees>" + 
            " <employee id=\"101\">" + 
            "    <name>John Doe</name>" + 
            "     <title>Author</title>" + 
            " </employee>" + 
            " <employee id=\"102\">" + 
            "    <name>Jane Deer</name>" + 
            "     <title>Doctor</title>" + 
            " </employee>" + 
            "</employees>";

	@BeforeEach
    void setMockOutput() {
    	MockitoAnnotations.initMocks(this);

    	Document doc = convertStringToXMLDocument(xmlStr);

    	Mockito.when(fileStorageServiceImplementor.convertToObject("employees.opi")).thenReturn(doc);
    	
    	// invalid XML
    	Mockito.when(fileStorageServiceImplementor.convertToObject("bad.opi")).thenReturn(null);

    }

	/**
	 * Check XML Parsing
	 * @throws Exception
	 */
	@Test
	public void getStatistics_Test() throws Exception {
		User user = new User();
    	user.setId(1L);
    	user.setUsername("arunit");
    	user.setPassword("password");
   
    	UserFile userFile = new UserFile();
    	userFile.setFilename("employees.opi");
    	userFile.setId(1L);
    	Set<UserFile> userFiles = new HashSet<>();
    	userFiles.add(userFile);
    	user.setUserFiles(userFiles);
    	Map<String, Long> expectedStats = new HashMap<String, Long>();
    	expectedStats.put("employees", 1L);
    	expectedStats.put("employees.employee", 2L);
    	expectedStats.put("employees.employee.name", 2L);
    	expectedStats.put("employees.employee.title", 2L);

		Set<Statistic> statistics = asyncFileProcessorService.getStatistics(userFile, user);
		statistics.forEach( (statistic) -> { 
			assertEquals(expectedStats.get(statistic.getName()), statistic.getValue());
			assertEquals(user.getUsername(), statistic.getUser().getUsername());
			assertEquals(userFile.getFilename(), statistic.getUserFile().getFilename());
		});

	}

	/**
	 * Check saving file and statistics to database tables
	 * @throws Exception
	 */
	@Test
	public void saveUserFiles_Test() {
		User user = new User();
    	user.setId(1L);
    	user.setUsername("arunit");
    	user.setPassword("password");
   
    	UserFile userFile = new UserFile();
    	userFile.setFilename("employees.opi");
    	userFile.setId(1L);

    	Map<String, Long> expectedStats = new HashMap<String, Long>();
    	expectedStats.put("employees", 1L);
    	expectedStats.put("employees.employee", 2L);
    	expectedStats.put("employees.employee.name", 2L);
    	expectedStats.put("employees.employee.title", 2L);

    	Mockito.when(userJpaRepository.findByUsername(any(String.class))).thenReturn(user);
		Mockito.when(userFileJpaRepository.findByFilename(any(String.class))).thenReturn(null);
		FileMetadata fileMeta = new FileMetadata("employees.opi", "arunit");

		Set<Statistic> statistics = null;
		try {
			statistics = asyncFileProcessorService.saveUserFiles(fileMeta).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		verify(userJpaRepository,times(1)).save(user);
		statistics.forEach( (statistic) -> { 
			assertEquals(expectedStats.get(statistic.getName()), statistic.getValue());
			assertEquals(user.getUsername(), statistic.getUser().getUsername());
			assertEquals(userFile.getFilename(), statistic.getUserFile().getFilename());
		});
	}

	/**
	 * Check parsing invalid xml file. No statistics returned
	 * @throws Exception
	 */
	@Test
	public void saveUserFiles_BadXml_Test() {
		User user = new User();
    	user.setId(1L);
    	user.setUsername("arunit");
    	user.setPassword("password");

    	Mockito.when(userJpaRepository.findByUsername(any(String.class))).thenReturn(user);
		Mockito.when(userFileJpaRepository.findByFilename(any(String.class))).thenReturn(null);
		FileMetadata fileMeta = new FileMetadata("bad.opi", "arunit");

		Set<Statistic> statistics = null;
		try {
			statistics = asyncFileProcessorService.saveUserFiles(fileMeta).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		verify(userJpaRepository,times(1)).save(user);
		assertNull(statistics);
		
	}

	/**
	 * Check saving file and statistics to database tables when file already exists
	 * @throws Exception
	 */
	@Test
	public void saveUserFiles_FileExists_Test() {
		User user = new User();
    	user.setId(1L);
    	user.setUsername("arunit");
    	user.setPassword("password");
   
    	UserFile userFile = new UserFile();
    	userFile.setFilename("employees.opi");
    	userFile.setId(1L);

    	Map<String, Long> expectedStats = new HashMap<String, Long>();
    	expectedStats.put("employees", 1L);
    	expectedStats.put("employees.employee", 2L);
    	expectedStats.put("employees.employee.name", 2L);
    	expectedStats.put("employees.employee.title", 2L);

		UserFile userFileExisting = new UserFile();
    	userFile.setFilename("employees.opi");
    	userFile.setId(1L);

    	Mockito.when(userJpaRepository.findByUsername(any(String.class))).thenReturn(user);
		Mockito.when(userFileJpaRepository.findByFilename(any(String.class))).thenReturn(userFileExisting);

		FileMetadata fileMeta = new FileMetadata("employees.opi", "arunit");
		Set<Statistic> statistics = null;
		try {
			statistics = asyncFileProcessorService.saveUserFiles(fileMeta).get();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// user saved twice. 1 - delete existing file and statistics and link from user, 2 - add new file and statistics
		verify(userJpaRepository,times(2)).save(user);
		statistics.forEach( (statistic) -> { 
			assertEquals(expectedStats.get(statistic.getName()), statistic.getValue());
			assertEquals(user.getUsername(), statistic.getUser().getUsername());
			assertEquals(userFile.getFilename(), statistic.getUserFile().getFilename());
		});
    	
	}

	private static Document convertStringToXMLDocument(String xmlString) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
 
		DocumentBuilder builder = null;
		try
		{
			builder = factory.newDocumentBuilder();
		   
			Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
		    return doc;
		} catch (Exception e) {
	      e.printStackTrace();
	    }
	    return null;
	}
}
