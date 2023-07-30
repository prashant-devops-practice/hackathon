package com.hackathon.codesearch;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("search")
@CrossOrigin
public class CodeSearchController {
	
	@GetMapping("/{searchString}")
	@ResponseBody
    public ApiResponse getBook(@PathVariable("searchString") String searchString) {
		HttpHeaders headers = new HttpHeaders();
		String url="https://api.bitbucket.org/2.0/workspaces/zoomdata/search/code?search_query="+searchString;
		headers.add("Authorization", "Basic " + "cHJhc2hhbnRJbnNpZ2h0OkFUQkJ4eE00dlhrRHdtNGsyWGZKYzlTR3NrN2I0MTU2MURCRg==");
		HttpEntity<SearchResponse> request = new HttpEntity<SearchResponse>(headers);
		ResponseEntity<SearchResponse> response = new RestTemplate().exchange(url, HttpMethod.GET, request, SearchResponse.class);
		SearchResponse searchResponse=response.getBody();
		
		ApiResponse apiResponse=new ApiResponse();
		apiResponse.setRepository("zoomdata");
		List<Result> results=new ArrayList<Result>();
		for (Values values:searchResponse.getValues()) {
			 Result r=new Result();
			 r.setLabel(values.getFile().getPath());
			 r.setUrl(values.getFile().getLinks().getSelf().getHref());
			 results.add(r);
		}
		apiResponse.setResults(results);
        return apiResponse;
    }
	
	 @PostMapping("/content")
	 public String getContents(@RequestBody Content content) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Basic " + "cHJhc2hhbnRJbnNpZ2h0OkFUQkJ4eE00dlhrRHdtNGsyWGZKYzlTR3NrN2I0MTU2MURCRg==");
			HttpEntity<String> request = new HttpEntity<String>(headers);
			ResponseEntity<String> response = new RestTemplate().exchange(content.getUrl(), HttpMethod.GET, request, String.class);
			return response.getBody();
			
	    }

}
