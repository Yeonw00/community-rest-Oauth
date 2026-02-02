package com.example.community.rest.community_rest.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.community.rest.community_rest.exception.UserNotFoundException;
import com.example.community.rest.community_rest.jpa.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Component
public class UserService {
	
	private static List<User> users = new ArrayList<>();
	
	private static Long userId = 0L; 
	
	private UserRepository repository;
	private final BCryptPasswordEncoder passwordEncoder;
	
	public UserService(UserRepository repository, BCryptPasswordEncoder passwordEncoder) {
		super();
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
	}

	static {
		users.add(new User(++userId, "Hong", "hongpassword", LocalDate.now().minusYears(30), "hong@gmail.com", "010-1122-3344", false));
		users.add(new User(++userId, "Jungo","jungopassword", LocalDate.now().minusYears(31).minusMonths(5), "jungo@gmail.com", "010-3421-1234", true));
		users.add(new User(++userId, "Minjun","minjunpassword", LocalDate.now().minusYears(30).minusDays(4), "minjun@gmail.com", "010-5674-9988", true));
	}
	
	public User save(User user) {
		String email = user.getEmailAddress();
		if(users.stream().anyMatch(users -> users.getEmailAddress().equals(email))){
			user.setId(0L);
			return user; 
		}
		user.setId(++userId);
		// 비밀번호 암호화
		String encodedPassword = encodePassword(user);
		user.setPassword(encodedPassword);
		users.add(user);
		return user;
	}

	public List<User> findAllUsers() {
		return users;
	}
	
	public User findUserById(int id) {
		Predicate<? super User> predicate = user -> user.getId() == id;
		return users.stream().filter(predicate).findFirst().orElse(null);
	}
	
	public void delete(int id) {
		Predicate<? super User> predicate = user -> user.getId() == id;
		users.removeIf(predicate);
	}
	
	public User findUserByUsername(String username){
		User existingUser = repository.findUserByUsername(username);
		
		if(existingUser==null)
			throw new UserNotFoundException("username:" +username);
		
		return existingUser;
	}

	@Transactional
	public User updateJpaUser(int id, User user, String password) {
		// 1. 기존 유저 조회
	    User existingUser = repository.findById(id)
	            .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
	    
	    // 2. 필요한 필드 업데이트
	    existingUser.setUsername(user.getUsername());
	    existingUser.setEmailAddress(user.getEmailAddress());
	    existingUser.setPhoneNumber(user.getPhoneNumber());
	    existingUser.setBirthDate(user.getBirthDate());
	    existingUser.setMale(user.isMale());
	    existingUser.setPassword(password);

	    // 3. 저장
	    return repository.save(existingUser);
	}
	
	public String encodePassword(User user) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        return encodedPassword;
    }
	
	public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
	
}
