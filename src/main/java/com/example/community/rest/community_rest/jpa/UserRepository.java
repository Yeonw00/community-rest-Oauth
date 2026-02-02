package com.example.community.rest.community_rest.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.community.rest.community_rest.user.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	User findUserByUsername(String username);

	User findByUsernameAndPassword(String username, String password);

	Optional<User> findByGoogleId(String providerId);
}
