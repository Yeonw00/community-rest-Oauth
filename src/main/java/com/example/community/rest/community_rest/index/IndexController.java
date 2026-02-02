package com.example.community.rest.community_rest.index;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class IndexController {
	private IndexService indexService;
	
	public IndexController(IndexService indexService) {
		super();
		this.indexService = indexService;
	}

	@GetMapping("/index")
	public ResponseEntity<List<Object>> getSearch(@RequestParam String field, @RequestParam String query) {
		List<Object> results = indexService.searchPostByField(field, query);
		return ResponseEntity.ok(results);
	}
}
