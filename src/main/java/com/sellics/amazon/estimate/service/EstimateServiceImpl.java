package com.sellics.amazon.estimate.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

/**
 * Implementation of the incoming service requests.
 * 
 * @author Rohit
 *
 */
@Service("estimateService")
public class EstimateServiceImpl implements EstimateService {

	String baseUrl = "https://completion.amazon.com/search/complete?search-alias=aps&client=amazon-search-ui&mkt=1&q=";

	@Override
	public Integer getEstimate(String keyword) throws Exception {

		int finalScore = 0;

		// split the keyword into chunks
		List<String> parts = new ArrayList<String>();

		for (int i = 0; i <= keyword.length(); i++) {
			String currUrl = baseUrl + keyword.substring(0, i);
			currUrl = currUrl.replace(' ', '+');
			parts.add(currUrl);
		}

		// call each chunk, calculate score and add up
		// attach an executor to get the result of the API calls

		ExecutorService executor = Executors.newFixedThreadPool(10);
		List<Callable<Object>> tasks = new ArrayList<Callable<Object>>();

		for (String part : parts) {
			{
				tasks.add(new Callable<Object>() {
					public Object call() throws Exception {
						return getEstimateForSubstring(part, keyword);
					}
				});
			}
		}

		// collect the results as float values for more precise calculation
		try {
			List<Future<Object>> futureList = executor.invokeAll(tasks, 10, TimeUnit.SECONDS);
			for (Future<Object> future : futureList) {
				Object result = future.get();
				finalScore += (float) result;
			}
		} catch (InterruptedException e) {
			// handle the interrupts
		} catch (ExecutionException e) {
			// handle other exceptions
		}

		executor.shutdown();

		// normalize the score since only 10 values are returned by Amazon API
		float retVal = finalScore / 10;
		if (retVal > 100) {
			return 100;
		}
		return (int) retVal;
	}

	/**
	 * This method makes a call to Amazon AutoComplete API and retrieves the list of keyword search hits.
	 * @param urlStr
	 * @param keyword
	 * @return The score for the specific part of the keyword
	 * @throws Exception
	 */
	public float getEstimateForSubstring(String urlStr, String keyword) throws Exception {
		StringBuilder result = new StringBuilder();
		float score = 0;

		// Make an HTTP GET request to fetch the results
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();

		String finalStr = result.toString();

		// find out how many times the keyword was matched
		if (!finalStr.isEmpty()) {
			score = finalStr.split(keyword, -1).length - 1;
			//in case of complete match, adjust the score
			score = (float) (score == 11.0 ? 10.0 : score);
			// return the current score which is a weighted average
			return score / keyword.length() * 100;
		}

		//empty list returned, return 0
		return 0;
	}
}
