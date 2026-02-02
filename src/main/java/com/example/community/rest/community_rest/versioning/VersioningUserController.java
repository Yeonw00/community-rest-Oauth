package com.example.community.rest.community_rest.versioning;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VersioningUserController {

	// URI로 관리
	@GetMapping("/v1/user")
	public UserV1 getFirstVersionOfUser() {
		return new UserV1("Charlie Barnes");
	}
	
	@GetMapping("/v2/user")
	public UserV2 getSecondVersionOfUser() {
		return new UserV2(new Name("Charlie", "Barnes"));
	}
	
	//파라미터로 관리
	@GetMapping(path="/user", params="version=1")
	public UserV1 getFirstVersionOfUserRequestParameter() {
		return new UserV1("Charlie Barnes");
	}
	
	@GetMapping(path="/user", params="version=2")
	public UserV2 getSecondVersionOfUserRequestParameter() {
		return new UserV2(new Name("Charlie", "Barnes"));
	}
	
	//헤더로 관리
	@GetMapping(path="/user/header", headers="X-API-Version=1")
	public UserV1 getFirstVersionOfUserRequestHeader() {
		return new UserV1("Charlie Barnes");
	}
	
	@GetMapping(path="/user/header", headers="X-API-Version=2")
	public UserV2 getSecondVersionOfUserRequestHeader() {
		return new UserV2(new Name("Charlie", "Barnes"));
	}
	
	//Accept 헤더로 관리
	@GetMapping(path="/user/accept", produces="application/vnd.company.app-v1+json")
	public UserV1 getFirstVersionOfUserAcceptHeader() {
		return new UserV1("Charlie Barnes");
	}
	
	@GetMapping(path="/user/accept", produces="application/vnd.company.app-v2+json")
	public UserV2 getSecondVersionOfUserAcceptHeader() {
		return new UserV2(new Name("Charlie", "Barnes"));
	}
}


	