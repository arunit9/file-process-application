package com.app.fileprocess.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.fileprocess.dao.entity.Statistic;
import com.app.fileprocess.response.StatisticResponse;
import com.app.fileprocess.service.FileProcessorService;

@Controller
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "*", maxAge = 3600)
public class StatisticsController {

	@Autowired
	private FileProcessorService fileProcessorService;

	@GetMapping(value = {"/{username}", "/{username}/{filename}"})
	public ResponseEntity<List<StatisticResponse>> getStatistics(@PathVariable @Min(6) @Max(32) String username, @PathVariable Optional<String> filename)
			throws IOException {
		List<StatisticResponse> statisticResponses = new ArrayList<StatisticResponse>();
		Set<Statistic> statistics = null;
		if (fileProcessorService.checkUserExists(username)) {
			if (filename.isPresent()) {
				if (fileProcessorService.checkUserFileExists(filename.get()))
				statistics = fileProcessorService.getStatisticsByFilename(filename.get());
			} else {
				statistics = fileProcessorService.getStatistics(username);
			}
			statistics.forEach( (statistic) -> { statisticResponses.add(new StatisticResponse(statistic.getName(), statistic.getValue().toString())); } );

		}
		return ResponseEntity.status(HttpStatus.OK).body(statisticResponses);
		
	}

}
