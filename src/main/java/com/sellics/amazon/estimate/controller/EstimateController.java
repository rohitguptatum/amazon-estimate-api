package com.sellics.amazon.estimate.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sellics.amazon.estimate.model.EstimateSearchVolume;
import com.sellics.amazon.estimate.service.EstimateService;

/**
 * Controller that handles all the incoming requests.
 * @author Rohit
 *
 */
@RestController
public class EstimateController {
	
	@Autowired
	EstimateService estimateService; // Autowire the service
	
	/**
	 * Provide a method for estimating.
	 * @param keyword
	 * @return keyword and it's score.
	 * @throws Exception
	 */
	@RequestMapping(value = "/estimate", method = RequestMethod.GET)
	public ResponseEntity<EstimateSearchVolume> estimate(@RequestParam String keyword) throws Exception {

		int score = estimateService.getEstimate(keyword);
		EstimateSearchVolume est = new EstimateSearchVolume(keyword, score);
		return new ResponseEntity<EstimateSearchVolume>(est,
				HttpStatus.OK);
	}
}
