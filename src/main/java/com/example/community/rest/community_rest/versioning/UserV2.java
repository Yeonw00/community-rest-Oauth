package com.example.community.rest.community_rest.versioning;

public class UserV2 {
	private Name name;

	public UserV2(Name name) {
		super();
		this.name = name;
	}

	public Name getName() {
		return name;
	}

	@Override
	public String toString() {
		return "UserV2 [name=" + name + "]";
	}
}
