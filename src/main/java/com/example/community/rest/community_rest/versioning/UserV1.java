package com.example.community.rest.community_rest.versioning;

public class UserV1 {
	private String name;

	public UserV1(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "UserV1 [name=" + name + "]";
	}
	
}
