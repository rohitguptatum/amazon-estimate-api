package com.sellics.amazon.estimate.service;

/**
 * Interface handling all incoming service requests.
 * @author Rohit
 *
 */
public interface EstimateService {
	
	public Integer getEstimate(String keyword) throws Exception;

}
