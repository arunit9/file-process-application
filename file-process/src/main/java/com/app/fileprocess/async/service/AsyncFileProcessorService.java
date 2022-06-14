package com.app.fileprocess.async.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.app.fileprocess.dao.entity.Statistic;
import com.app.fileprocess.dao.entity.User;
import com.app.fileprocess.dao.entity.UserFile;
import com.app.fileprocess.dao.repository.UserFileJpaRepository;
import com.app.fileprocess.dao.repository.UserJpaRepository;
import com.app.fileprocess.queue.FileMetadata;
import com.app.fileprocess.storage.StorageService;

/**
 * AsyncFileProcessorService
 * 
 * <P>Process files asynchronously by parsing the xml and calculating the statistics
 * 
 * @author arunitillekeratne
 * @version 1.0
 *
 */
@Service
public class AsyncFileProcessorService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncFileProcessorService.class);

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private UserFileJpaRepository userFileJpaRepository;
 
    @Autowired
    private StorageService fileStorageServiceImplementor;

    /**
     * The main method responsible for asynchronous processing of files
     * that are polled from a queue
     * 
     * @param fileMetadata
     * @return
     * @throws Exception
     */
    @Async
    @Transactional
    public CompletableFuture<Set<Statistic>> saveUserFiles(FileMetadata fileMetadata) throws Exception {
        final long start = System.currentTimeMillis();

        User user = userJpaRepository.findByUsername(fileMetadata.getUsername());

        // Remove existing file and statistics of the same file name
        UserFile existingUserFile = userFileJpaRepository.findByFilename(fileMetadata.getFilename());
    	
        if (existingUserFile != null) {
	    	user.getUserFiles().remove(existingUserFile);
	    	user.getStatistics().removeAll(existingUserFile.getStatistics());
	    	userJpaRepository.save(user);
    	}

        UserFile userFile = new UserFile();
        userFile.setFilename(fileMetadata.getFilename());
        userFile.setUser(user);

        Set<Statistic> statistics = getStatistics(userFile, user);
        if (statistics != null) {
        	LOGGER.info("Saving a list of statistics of size {} records", statistics.size());
        	userFile.setStatistics(statistics);
        	user.getStatistics().addAll(statistics);
        }
        user.getUserFiles().add(userFile);
        userJpaRepository.save(user);

        LOGGER.info("Elapsed time: {}", (System.currentTimeMillis() - start));
        return CompletableFuture.completedFuture(statistics);
    }

    /**
     * Get the file on the physical directory using the file metadata
     * and calls method to extract statistics
     * 
     * if the xml cannot be parsed, a prefix of Error_ added to filename
     * 
     * @param userFile
     * @param user
     * @return
     */
    public Set<Statistic> getStatistics(UserFile userFile, User user) {
    	Set<Statistic> statistics = null;
 
    	Document doc = (Document)fileStorageServiceImplementor.convertToObject(userFile.getFilename());

    	if (doc == null) {
    		// Error occurred in parsing XML. Append Error_ in front of the file name
    		userFile.setFilename("Error_" + userFile.getFilename());
    		return statistics;
    	}
    	
    	return parseXML(userFile, user, doc);
    }

    private Set<Statistic> parseXML(UserFile userFile, User user, Document doc) {

		NodeList list = doc.getDocumentElement().getChildNodes();
        Map<String, Long> stats = new HashMap<String, Long>();
        String str = doc.getDocumentElement().getNodeName();
        stats.put(str, 1L);
        visitChildNodes(list, stats, str);
        Set<Statistic> statistics = new HashSet<>();
        for (Map.Entry mapElement : stats.entrySet()) {
            String key = ((String)mapElement.getKey());
            Long value = ((Long)mapElement.getValue());
            Statistic statistic = new Statistic();
            statistic.setName(key);
            statistic.setValue(value);
            statistic.setUser(user);
            statistic.setUserFile(userFile);
            statistics.add(statistic);
        }

		return statistics; 
    }

    /*
     * Recursively navigate the xml document to extract statistics of the node elements
     */
    private static void visitChildNodes(NodeList nList, Map<String, Long> stats, String str) {
    	for (int temp = 0; temp < nList.getLength(); temp++) {
          Node node = nList.item(temp);
          if (node.getNodeType() == Node.ELEMENT_NODE) {
        	  String newStr = str + "." + node.getNodeName();
        	  Long count = stats.get(newStr);
        	  stats.put(newStr, count == null ? 1 : ++count);

             if (node.hasChildNodes()) {
                 // Visit child nodes recursively
                 visitChildNodes(node.getChildNodes(), stats, newStr);
             }
          }
       }
    }
}
