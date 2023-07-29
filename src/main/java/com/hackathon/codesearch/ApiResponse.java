package com.hackathon.codesearch;

import java.util.List;

public class ApiResponse {

	String repository;

	List<Result> results;

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public List<Result> getResults() {
		return results;
	}

	public void setResults(List<Result> results) {
		this.results = results;
	}

	

	

}
