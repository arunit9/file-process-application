package com.app.fileprocess.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.app.fileprocess.dao.entity.UserFile;
import com.app.fileprocess.response.MessageResponse;
import com.app.fileprocess.response.UserFileResponse;
import com.app.fileprocess.service.FileProcessorService;
import com.app.fileprocess.storage.StorageFileNotFoundException;
import com.app.fileprocess.storage.StorageService;
import com.app.fileprocess.validator.UserFileValidator;

@Controller
@RequestMapping("/api/files")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FileUploadController {
	private final StorageService storageService;

	@Autowired
	private FileProcessorService fileProcessorService;

	@Autowired
	private UserFileValidator userFileValidator;

	@Autowired
	public FileUploadController(StorageService storageService) {
		this.storageService = storageService;
	}

	@GetMapping("/{username}")
	public ResponseEntity<List<UserFileResponse>> listUploadedFiles(@PathVariable @Min(6) @Max(32) String username) throws IOException {
		List<UserFileResponse> userFileResponses = new ArrayList<UserFileResponse>();

		if (!fileProcessorService.checkUserExists(username)) {
			userFileResponses.add(new UserFileResponse("Error: Username invalid!"));
			return ResponseEntity
					.badRequest()
					.body(userFileResponses);
		}
		
		Set<UserFile> userFiles = fileProcessorService.listFilesByUser(username);
		userFiles.forEach( (userFile) -> { userFileResponses.add(new UserFileResponse(userFile.getId(), userFile.getFilename())); } );

		return ResponseEntity.status(HttpStatus.OK).body(userFileResponses);
	}

	@DeleteMapping("/{username}/{filename}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteFile(@PathVariable @Min(6) @Max(32) String username, @PathVariable @Min(5) String filename) {
		storageService.deleteFile(filename.replaceFirst("Error_", ""));
		fileProcessorService.deleteFileByFilename(username, filename);
	}

	@PostMapping("/upload/multi/{username}")
	public ResponseEntity<MessageResponse> handleMultipleFileUpload(@PathVariable @Min(6) @Max(32) String username,
			@RequestParam("files") MultipartFile[] files) {
		if (!fileProcessorService.checkUserExists(username)) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username invalid!"));
		}
		List<String> fileNames = new ArrayList<>();
		List<String> failures = new ArrayList<>();
		for (MultipartFile file : files) {
			if (!userFileValidator.validate(file.getOriginalFilename())) {
				failures.add(file.getOriginalFilename());
				continue;
			}
			storageService.store(file);
			
			if (fileProcessorService.addToQueue(file.getOriginalFilename(), username)) {
				fileNames.add(file.getOriginalFilename());
			} else {
				failures.add(file.getOriginalFilename());
			}
		}

		String message = "Success: [" + fileNames + "], Fail: [" + failures + "]";
	    return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
	}

	@PostMapping("/upload/{username}")
	public ResponseEntity<MessageResponse> handleFileUpload(@PathVariable @Min(6) @Max(32) String username,
			@RequestParam("files") MultipartFile file) {
		if (!fileProcessorService.checkUserExists(username)) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username invalid!"));
		}
		List<String> fileNames = new ArrayList<>();
		List<String> failures = new ArrayList<>();
		if (!userFileValidator.validate(file.getOriginalFilename())) {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse("File upload failed"));
		}
		storageService.store(file);

		if (fileProcessorService.addToQueue(file.getOriginalFilename(), username)) {
			fileNames.add(file.getOriginalFilename());
		} else {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse("File upload failed"));
		}
		String message = "Success: [" + fileNames + "], Fail: [" + failures + "]";
	    return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}


}
