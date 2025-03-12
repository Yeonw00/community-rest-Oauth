package com.example.community.rest.community_rest.post;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.community.rest.community_rest.exception.UserNotFoundException;

import jakarta.validation.Valid;

@RestController
public class PostResource {
	
	private PostDaoService service;
	
	public PostResource(PostDaoService service) {
		this.service = service;
	}
	
	@GetMapping("/posts")
	public List<Post> retrieveAllPosts() {
		return service.findAll();
	}
	
	@GetMapping("/posts/{username}")
	public List<Post> retrievePostsByUsername(@PathVariable String username) {
		List<Post> posts = service.findPostsByUsername(username);
		
		if(posts.isEmpty())
			throw new UserNotFoundException("username:" +username);
			
		return posts;
	}
	
	@PostMapping("/posts")
	public ResponseEntity<Post> createPost(@Valid @RequestBody Post post) {
		
		Post savedPost = service.save(post);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
						.path("/{username}")
						.buildAndExpand(savedPost.getUsername())
						.toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@DeleteMapping("/posts/{id}")
	public ResponseEntity<Void> deletePostById(@PathVariable int id) {
		service.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/posts/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable int id, @RequestBody Post post) {
        Post updatedPost = service.updateJpaPost(id, post);

        if (updatedPost == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(updatedPost);
    }
	
}
