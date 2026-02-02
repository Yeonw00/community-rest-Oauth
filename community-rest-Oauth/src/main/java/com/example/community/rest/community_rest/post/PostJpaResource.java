package com.example.community.rest.community_rest.post;

import java.net.URI;
import java.util.List;

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
import com.example.community.rest.community_rest.file.Upload;
import com.example.community.rest.community_rest.index.IndexService;
import com.example.community.rest.community_rest.jpa.FileRepository;
import com.example.community.rest.community_rest.jpa.PostRepository;
import com.example.community.rest.community_rest.jpa.UserRepository;
import com.example.community.rest.community_rest.user.User;

import jakarta.validation.Valid;

@RestController
public class PostJpaResource {
	
	private PostDaoService service;
	
	private PostRepository repository;
	private UserRepository userRepository;
	private FileRepository fileRepository;
	
	String index = "post-index";
	
	public PostJpaResource(PostDaoService service, PostRepository repository, UserRepository userRepository, FileRepository fileRepository) {
		this.service = service;
		this.repository = repository;
		this.userRepository = userRepository;
		this.fileRepository = fileRepository;
	}
	
	@GetMapping("/jpa/posts")
	public List<Post> retrieveAllPosts() {
		return repository.findAll();
	}
	
	@GetMapping("/jpa/posts/{username}")
	public List<Post> retrievePostsByUsername(@PathVariable String username) {
		List<Post> posts = repository.findPostsByUsername(username);
		
		if(posts.isEmpty())
			throw new UserNotFoundException("username:" +username);
			
		return posts;
	}
	
	@GetMapping("/jpa/post/{id}")
	public Post retrievePostById(@PathVariable int id) {
		Post post = repository.findPostById(id);
		
		if(post==null)
			throw new UserNotFoundException("id:" + id);
			
		return post;
	}
	
//	@PostMapping("/jpa/posts")
//	public ResponseEntity<Post> createPost(@Valid @RequestBody Post post) {
//		
//		Post savedPost = repository.save(post);
//		
//		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
//						.path("/{username}")
//						.buildAndExpand(savedPost.getUsername())
//						.toUri();
//		
//		return ResponseEntity.created(location).build();
//	}
	
	// Post도 같이 반환하도록 하는 코드 
	@PostMapping("/jpa/posts")
	public ResponseEntity<Post> createPost(@Valid @RequestBody Post post) {
		System.out.println(post);
		// User 엔티티를 userId로 조회
		try {
			User user = userRepository.findUserByUsername(post.getUsername());
			if (user == null) {
		        throw new RuntimeException("User not found");
		    }
			// Post 객체에 User 설정
		    post.setUser(user);
		} catch(Exception e) {
			e.printStackTrace();  // 예외 처리
			new RuntimeException("User not found", e);
		}
		// Post 엔티티 저장
		Post savedPost = repository.save(post);
		
		// Upload와 Post 연결
	    List<Upload> uploads = fileRepository.findByUsernameAndPostIsNull(post.getUsername());
	    if (!uploads.isEmpty()) {
	        Upload firstUpload = uploads.get(0);
	        String baseThumbUrl = "http://localhost:8080/upload/thumb?uuid=";
	        String publicThumbPath = baseThumbUrl + firstUpload.getUuid();
	        savedPost.setThumbFilePath(publicThumbPath);
		    for (Upload upload : uploads) {
		        upload.setPost(savedPost);
		        savedPost.addUpload(upload);
		        
		    }
	    }
	    
	    System.out.println(savedPost);
	    repository.saveAndFlush(savedPost);  // 연결 후 Post 저장
		

	    // Location URI 생성
	    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
	                    .path("/{username}")
	                    .buildAndExpand(savedPost.getUsername())
	                    .toUri();

	    // elasticsearch에 인덱스데이터 저장
	    try {
	    	String index = "post-index";
			IndexService.indexPost(post);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    // ResponseEntity로 Post와 Location 헤더 반환
	    return ResponseEntity.created(location).body(savedPost);
	}
	
	@DeleteMapping("/jpa/posts/{id}")
	public void deletePostsById(@PathVariable int id) {
		repository.deleteById(id);
		IndexService.deleteIndexById(id, index);
	}
	
//	@PutMapping("/jpa/posts/{id}")
//	public ResponseEntity<Post> updatePost(@PathVariable int id, @RequestBody Post post) {
//		String changedDescription = post.getDescription();
//		
//		// 레포지토리를 통한 업데이트 호출
//		int updatedRows = repository.updatePost(id, changedDescription);
//		
//	    // 업데이트 결과 확인
//	    if (updatedRows == 0) {
//	    	return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//	    }
//	    
//	    // 업데이트된 Post 객체를 반환
//	    Post updatedPost = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post not found with id " + id));
//
//	    return ResponseEntity.ok(updatedPost);
//	}
	
	@PutMapping("/jpa/posts/{id}")
	public ResponseEntity<Post> updatePost(@PathVariable int id, @RequestBody Post post) {
	    Post updatedPost = service.updateJpaPost(id, post);
	    IndexService.deleteIndexById(id, index);
	    IndexService.indexPost(updatedPost);
	    return ResponseEntity.ok(updatedPost);
	}
	
}
