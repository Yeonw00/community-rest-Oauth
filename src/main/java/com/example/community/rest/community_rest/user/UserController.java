package com.example.community.rest.community_rest.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.community.rest.community_rest.exception.EmailAlreadyExistsException;
import com.example.community.rest.community_rest.exception.UserNotFoundException;

import jakarta.validation.Valid;

@RestController
public class UserController {
	
	private UserService service;
	
	public UserController(UserService service) {
		this.service = service;
	}

	@GetMapping("/users")
	public List<User> getAllUsers() {
		List<User> users = service.findAllUsers();
		return users;
	}
	
	/*
	 * hateoas 사용 위해 잠시 주석처리
	 * 
		@GetMapping("/users/{id}")
		public User getUserById(@PathVariable int id) {
			User user = service.findUserById(id);
			
			if(user==null) 
				throw new UserNotFoundException("id:" + id);
			
			return user;
		}
	*/

	@PostMapping("/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		User joinedUser = service.save(user);
		
		if(joinedUser.getId()==0) 
			throw new EmailAlreadyExistsException("이미 사용중인 이메일 주소입니다:" + user.getEmailAddress());
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
						.path("/{id}")
						.buildAndExpand(joinedUser.getId())
						.toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@DeleteMapping("/users/{id}")
	public void deleteUser(@PathVariable int id) {
		service.delete(id);
	} 
	
	//HATEOAS 사용
	@GetMapping("/users/{id}")
	public EntityModel<User> retrieveUser(@PathVariable int id) {
		User user = service.findUserById(id);
		
		if(user==null) 
			throw new UserNotFoundException("id:" + id);
		
		EntityModel<User> entityModel = EntityModel.of(user);
		
		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).getAllUsers());
		entityModel.add(link.withRel("all-users"));
		
		return entityModel;
	}
}
