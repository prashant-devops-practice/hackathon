package com.hackathon.codesearch;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("search")
@CrossOrigin
public class CodeSearchController {

	@ResponseBody
	@GetMapping
	public ApiResponse getResult(@RequestParam("keyword") String searchString, HttpServletRequest request) {
		HttpHeaders headers = new HttpHeaders();
		String url = "https://api.bitbucket.org/2.0/workspaces/zoomdata/search/code?search_query=" + searchString;
		
		HttpEntity<SearchResponse> request1 = new HttpEntity<SearchResponse>(headers);
		ResponseEntity<SearchResponse> response = new RestTemplate().exchange(url, HttpMethod.GET, request1,
				SearchResponse.class);
		SearchResponse searchResponse = response.getBody();
		String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request).replacePath(null).build().toUriString();
		String finalUrl = baseUrl + "/search/content?url=";
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setRepository("zoomdata");
		List<Result> results = new ArrayList<Result>();
		for (Values values : searchResponse.getValues()) {
			Result r = new Result();
			r.setLabel(values.getFile().getPath());
			r.setUrl(finalUrl+values.getFile().getLinks().getSelf().getHref());
			results.add(r);
		}
		apiResponse.setResults(results);
		return apiResponse;
	}

	@GetMapping("/content")
	public String getContents(@RequestParam("url") String content) {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> request1 = new HttpEntity<String>(headers);
		ResponseEntity<String> response = new RestTemplate().exchange(content, HttpMethod.GET, request1,
				String.class);
		return response.getBody();

	}

}
