package com.app.fileprocess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
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
import com.app.fileprocess.dao.repository.UserJpaRepository;
import com.app.fileprocess.storage.StorageService;

public class AsyncFileProcessorServiceTest {
	@Mock
    private StatisticJpaRepository statisticJpaRepository;

	@Mock
    private UserJpaRepository userJpaRepository;

	@Mock
	private StorageService fileStorageServiceImplementor;

	@InjectMocks // auto inject helloRepository
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
//    	User user = new User();
//    	user.setId(1L);
//    	user.setUsername("arunit");
//    	user.setPassword("password");
//    	Set<Statistic> stats = new HashSet<Statistic>();
//    	Statistic stat1 = new Statistic();
//    	stat1.setId(1L);
//    	stat1.setFilename("employees.opi");
//    	stat1.setName(".employee");
//    	stat1.setValue(2);
//    	stats.add(stat1);
//
//    	Statistic stat2 = new Statistic();
//    	stat2.setId(1L);
//    	stat2.setFilename("notes.opi");
//    	stat2.setName(".employee.name");
//    	stat2.setValue(2);
//    	stats.add(stat2);
//
//    	Statistic stat3 = new Statistic();
//    	stat3.setId(1L);
//    	stat3.setFilename("notes.opi");
//    	stat3.setName(".employee.title");
//    	stat3.setValue(2);
//    	stats.add(stat3);
//    	user.setStatistics(stats);

//    	List<Statistic> statistics = new ArrayList<Statistic>();
//    	statistics.add(stat1);
//    	statistics.add(stat2);

    	List<Object[]> groupedElements =  new ArrayList<Object[]>();
    	Object[] objectArray1 = { "heading.body", 3 };
    	Object[] objectArray2 = { "heading.body.name", 6 };
    	groupedElements.add(objectArray1);
    	groupedElements.add(objectArray2);

    	Document doc = convertStringToXMLDocument(xmlStr);

//    	Mockito.when(userJpaRepository.findByUsername(any(String.class))).thenReturn(user);
//    	Mockito.when(statisticJpaRepository.findByFilename(any(String.class))).thenReturn(statistics);
//    	Mockito.when(statisticJpaRepository.saveAll(ArgumentMatchers.<List<Statistic>>any())).thenReturn(statistics);
//    	Mockito.when(fileStorageServiceImplementor.convertToObject(any(String.class))).thenReturn(doc);

    }

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

//		Set<Statistic> statistics = asyncFileProcessorService.getStatistics(userFile, user);
//		assertEquals(".employee", statistics.get(0).getName());
//		assertEquals(".employee.name", statistics.get(1).getName());
//		assertEquals(".employee.title", statistics.get(2).getName());
//		assertEquals(new Integer(2), statistics.get(0).getValue());
//		assertEquals(new Integer(2), statistics.get(1).getValue());
//		assertEquals(new Integer(2), statistics.get(2).getValue());
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
