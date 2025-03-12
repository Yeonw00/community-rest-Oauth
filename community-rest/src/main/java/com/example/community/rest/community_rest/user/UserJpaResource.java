package com.example.community.rest.community_rest.user;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.community.rest.community_rest.exception.EmailAlreadyExistsException;
import com.example.community.rest.community_rest.exception.UserNotFoundException;
import com.example.community.rest.community_rest.index.IndexService;
import com.example.community.rest.community_rest.jpa.PostRepository;
import com.example.community.rest.community_rest.jpa.UserRepository;
import com.example.community.rest.community_rest.post.Post;

import jakarta.validation.Valid;

@RestController
public class UserJpaResource {
	
	private UserService userService;
	private UserRepository userRepository;

	private PostRepository postRepository;
	
	String index = "user-index";
	
	public UserJpaResource(UserService userService,UserRepository userRepository, PostRepository postRepository) {
		this.userService = userService;
		this.userRepository = userRepository;
		this.postRepository = postRepository;
	}

	@GetMapping("/jpa/users")
	public List<UserDTO> getAllUsers() {
		List<User> users = userRepository.findAll();
		List<UserDTO> userDTOList = UserDTO.from(users); 
		return userDTOList;
	}
	
	/*
	 * hateoas 사용 위해 잠시 주석처리
	 * 
		@GetMapping("/jpa/users/{id}")
		public User getUserById(@PathVariable int id) {
			User user = service.findUserById(id);
			
			if(user==null) 
				throw new UserNotFoundException("id:" + id);
			
			return user;
		}
	*/

	@PostMapping("/jpa/users")
	public ResponseEntity<UserDTO> createUser(@Valid @RequestBody User user) {
		String encodedPassword = userService.encodePassword(user);
		user.setPassword(encodedPassword);
		User joinedUser = userRepository.save(user);
		
		if(joinedUser.getId()==0) 
			throw new EmailAlreadyExistsException("이미 사용중인 이메일 주소입니다:" + user.getEmailAddress());
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
						.path("/{id}")
						.buildAndExpand(joinedUser.getId())
						.toUri();
		
		UserDTO userDTO = new UserDTO(joinedUser);

		// elasticsearch에 인덱스데이터 저장
	    try {
			IndexService.indexUser(userDTO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		
		return ResponseEntity.created(location).body(userDTO);
	}
	
	@DeleteMapping("/jpa/users/{id}")
	public void deleteUser(@PathVariable int id) {
		userRepository.deleteById(id);
		IndexService.deleteIndexById(id, index);
	} 
	
	//HATEOAS 사용
//	@GetMapping("/jpa/users/{id}")
//	public EntityModel<User> retrieveUser(@PathVariable int id) {
//		Optional<User> user = userRepository.findById(id);
//		
//		if(user.isEmpty()) 
//			throw new UserNotFoundException("id:" + id);
//		
//		EntityModel<User> entityModel = EntityModel.of(user.get());
//		
//		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).getAllUsers());
//		entityModel.add(link.withRel("all-users"));
//		
//		return entityModel;
//	}
	
	@GetMapping("/jpa/users/{id}")
	public UserDTO retrieveUserById(@PathVariable int id) {
	    User user =  userRepository.findById(id)
	            .orElseThrow(() -> new UserNotFoundException("id: " + id));
	    
	    UserDTO userDTO = new UserDTO(user);
	    return userDTO;
	}
	
	@GetMapping("/jpa/user/{username}")
	public UserDTO retrieveUserByUsername(@PathVariable String username) {
		User user = userRepository.findUserByUsername(username);
		
		if(user == null)
			throw new UserNotFoundException("username:" +username);
		
		UserDTO userDTO = new UserDTO(user);	
		return userDTO;
	}
	
	@GetMapping("/jpa/users/{id}/posts")
	public List<Post> retrievePostsForUser(@PathVariable int id) {
		Optional<User> user = userRepository.findById(id);
		
		if(user.isEmpty()) 
			throw new UserNotFoundException("id:" + id);
		
		return user.get().getPosts();
	} 
	
	@PostMapping("/jpa/users/{id}/posts")
	public ResponseEntity<Object> createPostForUser(@PathVariable int id, @Valid @RequestBody Post post) {
		Optional<User> user = userRepository.findById(id);
		
		if(user.isEmpty()) 
			throw new UserNotFoundException("id:" + id);
		
		post.setUser(user.get());
		post.setUsername(user.get().getUsername());
		
		Post savedPost = postRepository.save(post);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedPost.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/jpa/users/{id}")
	public ResponseEntity<UserDTO> updateUser(@PathVariable int id, @RequestBody User user) {
		String encodedPassword = userService.encodePassword(user);
	    User updatedUser = userService.updateJpaUser(id, user, encodedPassword);
	    UserDTO userDTO = new UserDTO(updatedUser);
	    IndexService.deleteIndexById(id, index);
	    IndexService.indexUser(userDTO);
	    return ResponseEntity.ok(userDTO);
	}
}
