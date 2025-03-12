package com.example.community.rest.community_rest.login;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.community.rest.community_rest.user.User;
import com.example.community.rest.community_rest.user.UserDTO;
import com.example.community.rest.community_rest.user.UserService;

@RestController
public class LoginController {
	
	private UserService userService;
	
	public LoginController(UserService userService) {
		super();
		this.userService = userService;
	}

	@PostMapping("/login")
	public ResponseEntity<UserDTO> login(@RequestBody LoginRequest loginRequest) {
		String username = loginRequest.getUsername();
		String rawPassword = loginRequest.getPassword();
		User existingUser = userService.findUserByUsername(username);
		
		if (existingUser != null && userService.checkPassword(rawPassword, existingUser.getPassword())) {
	        UserDTO userDTO = new UserDTO(existingUser);  // DTO 변환
	        return ResponseEntity.ok(userDTO);  // 로그인 성공 시 DTO 반환
	    } else {
	        return ResponseEntity.status(400).build();  // 실패 시 400 반환
	    }
	}
}
