package com.sellics.amazon.estimate.model;

/**
 * Response Entity that is returned as a JSON object.
 * @author Rohit
 *
 */
public class EstimateSearchVolume {
	
	public String keyword;
	public Integer score;

	public String getKeyword() {
		return keyword;
	}

	public Integer getScore() {
		return score;
	}
	
	public EstimateSearchVolume(String keyword, Integer score) {
		this.score = score;
		this.keyword = keyword;
	}
}
