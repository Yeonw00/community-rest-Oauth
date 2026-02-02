package com.example.community.rest.community_rest.post;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.community.rest.community_rest.jpa.PostRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Component
public class PostDaoService {
	private PostRepository repository;
	
	public PostDaoService(PostRepository repository) {
        this.repository = repository;
    }
	
	private static List<Post> posts = new ArrayList<>();
	
	private static int postsCount = 0;
	
	static {
		posts.add(new Post(++postsCount, "Hong", "Hong is", "she is a korean publisher", LocalDateTime.now().minusMonths(1)));
		posts.add(new Post(++postsCount, "Jungo", "Jungo is", "he is a japanese author", LocalDateTime.now().minusWeeks(2)));
		posts.add(new Post(++postsCount, "Minjun","Minjun is", "he is a doctor", LocalDateTime.now().minusYears(5)));
		posts.add(new Post(++postsCount, "Hong","Hong is an entertainer" ,"she can play the guitar", LocalDateTime.now().minusDays(13)));
	}
	
	

	public List<Post> findAll() {
		return posts;
	}
	
	public Post save(Post post) {
		post.setId(++postsCount);
		post.setUploadDate(LocalDateTime.now());
		posts.add(post);
		return post;
	}

	public List<Post> findPostsByUsername(String username) {
		Predicate<? super Post> predicate = post -> post.getUsername().equals(username);
		return posts.stream().filter(predicate).collect(Collectors.toList());
	}
	
	public Post findPostById(int id) {
		Predicate<? super Post> predicate = post -> post.getId()==id;
		return posts.stream().filter(predicate).findFirst().get();
	}
	
	public void deleteById(int id) {
		Predicate<? super Post> predicate = post -> post.getId().equals(id);
		posts.removeIf(predicate);
	}
	
	public Post updatePost(int id, Post post) {
		deleteById(id);
		posts.add(post);
		return post;
	}
	
	@Transactional
    public Post updateJpaPost(int id, Post post) {
		String changedDescription = post.getDescription();
		String changedTitle = post.getTitle();
		// 업데이트 수행
        int updatedRows = repository.updatePost(id, changedDescription, changedTitle);

        if (updatedRows == 0) {
            throw new EntityNotFoundException("Post not found with id " + id);
        }

        // 업데이트된 Post 반환
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found after update with id " + id));
    }
}
