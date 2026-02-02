package com.example.community.rest.community_rest.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.community.rest.community_rest.file.Upload;

public interface FileRepository extends JpaRepository<Upload, Integer> {

	Upload getByUuid(String uuid);

	List<Upload> findByUsernameAndPostIsNull(String username);

	Upload findByUuid(String uuid);

}
