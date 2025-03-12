package com.example.community.rest.community_rest.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.community.rest.community_rest.post.Post;

import jakarta.transaction.Transactional;

public interface PostRepository extends JpaRepository<Post, Integer>{
	
	List<Post> findPostsByUsername(String username);
	
	Post findPostById(int id);

	@Modifying
    @Transactional
    @Query("UPDATE Post p "
    		+ "SET p.description = :description, p.title = :title"
    		+ " WHERE p.id = :id")
    int updatePost(@Param("id") int id, @Param("description") String description, @Param("title") String title);

}
