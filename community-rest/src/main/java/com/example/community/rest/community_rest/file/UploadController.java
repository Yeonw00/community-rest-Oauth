package com.example.community.rest.community_rest.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.community.rest.community_rest.index.IndexService;
import com.example.community.rest.community_rest.jpa.FileRepository;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/upload")
public class UploadController {
	
	// 기본 업로드 경로 (application.properties에서 설정 가능)
    @Value("${file.upload-dir}")
    private String baseUploadDir;
    
    @Value("${file.thumb-dir}")
    private String thumbDir;
    
    private FileRepository fileRepository;
    
    private ThumbnailService thumbnailService;
    
    public UploadController(FileRepository fileRepository, ThumbnailService thumbnailService) {
		super();
		this.fileRepository = fileRepository;
		this.thumbnailService = thumbnailService;
	}

	@GetMapping("/{date}/{uuid}")
    public ResponseEntity<Resource> retrivePhoto(@PathVariable String date, @PathVariable String uuid) {
        try {
        	Path imagePath = Paths.get(baseUploadDir, date, uuid).normalize();
            Resource resource = new UrlResource(imagePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                // MIME 타입 설정
                String contentType = Files.probeContentType(imagePath);
                if (contentType == null) {
                    contentType = "application/octet-stream"; // 기본 MIME 타입
                }

                return ResponseEntity.ok()
                        .header("Content-Type", contentType)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
        	return ResponseEntity.badRequest().body(null);
        } catch (IOException e) {
        	return ResponseEntity.status(500).body(null);
		}
    }

	
	@GetMapping("/thumb")
	public ResponseEntity<Resource> retrieveThumbnail(@RequestParam String uuid) {
		 try {
			 	Upload uploadedFile = fileRepository.findByUuid(uuid);
	        	Path thumbPath = Paths.get(uploadedFile.getThumbFilePath()).normalize();
	        	Resource resource = new UrlResource(thumbPath.toUri());

	        	 if (resource.exists() && resource.isReadable()) {
	                 // MIME 타입 설정
	                 String contentType = Files.probeContentType(thumbPath);
	                 if (contentType == null) {
	                     contentType = "application/octet-stream"; // 기본 MIME 타입
	                 }

	                 return ResponseEntity.ok()
	                         .header("Content-Type", contentType)
	                         .body(resource);
	             } else {
	                 return ResponseEntity.notFound().build();
	             }
	           
		 	} catch (MalformedURLException e) {
	        	return ResponseEntity.badRequest().body(null);
	        } catch (IOException e) {
	        	return ResponseEntity.status(500).body(null);
			}
	}

    // 사진 업로드 처리
	@Transactional
    @PostMapping("/photo")
    public ResponseEntity<HashMap<String, String>> uploadPhoto(
    		@RequestParam("file") MultipartFile file,
            @RequestParam("fileName") String fileName,
            @RequestParam("uuid") String uuid,
            @RequestParam("username") String username,
            @RequestParam("type") String type,
            @RequestParam("size") long size,
            @RequestParam(value = "uploadInPost", required = false) boolean uploadInPost){
    	
    	 HashMap<String, String> response = new HashMap<>();
    	 
        if (file.isEmpty()) {
        	response.put("error", "No file uploaded");
            return ResponseEntity.badRequest().body(response);
        }

        try {
        	// 날짜별 디렉터리 생성 (예: 20241218)
            String dateDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            // 업로드 경로 생성
            Path uploadPath = Paths.get(baseUploadDir, dateDir);
            
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // 파일 이름에 UUID 추가
            String originalFileName = file.getOriginalFilename();
            String fileExtension = ""; // 파일 확장자 추출
            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            
            String newFileName = uuid + fileExtension; // UUID와 확장자로 새로운 파일명 생성

            // 파일 저장
            String filePath = uploadPath.resolve(newFileName).toString();
            file.transferTo(new File(filePath));
            
            // 반환할 HTTP 경로
            String publicPath = "http://localhost:8080/upload/" + dateDir + "/" + newFileName;
            
            LocalDateTime uploadDate = LocalDateTime.now();
            
            
       	 	Upload upload = new Upload(fileName, uuid, username, uploadDate,
       			 uploadDate, type, size, fileExtension, publicPath);
            
            upload.setFileName(fileName);
            upload.setUploadDate(uploadDate);
            upload.setModifiedDate(uploadDate);
            upload.setExtension(fileExtension);
            upload.setSize(size);
            upload.setType(type);
            upload.setUsername(username);
            upload.setUuid(uuid);
            
            try {
            	if (uploadInPost != false) {
                    Path thumbPath = Paths.get(thumbDir, dateDir);
                    
                    if (!Files.exists(thumbPath)) {
                        Files.createDirectories(thumbPath);
                    }
                    
                    String thumbFileName = uuid + "_thumb" + fileExtension;
                    String thumbFilePath = thumbPath.resolve(thumbFileName).toString();
                    
                    thumbnailService.createThumbnail(filePath, thumbFilePath);
                    // 반환할 HTTP 경로
                    upload.setThumbFilePath(thumbFilePath);

            	}
        		fileRepository.save(upload);
        		fileRepository.flush();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error during save: " + e.getMessage());
            }
            
            // elasticsearch에 인덱스데이터 저장
    	    try {
    	    	String index = "user-index";
    			IndexService.indexUpload(upload, index);
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
            
            response.put("fileNmae", fileName);
            response.put("filePath", publicPath);
            response.put("date", dateDir);
            response.put("uuid", uuid);
            response.put("extension", fileExtension);
            response.put("type", type);
            response.put("message", "File uploaded successfully");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            response.put("error", "Error uploading file: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    @DeleteMapping("/photo/{date}/{uuid}")
    public void deletePhoto(@PathVariable @DateTimeFormat(pattern = "yyyyMMdd") LocalDate date, @PathVariable String uuid) throws FileNotFoundException{
    	Upload fileData = fileRepository.getByUuid(uuid);
    	
    	// 파일 데이터를 찾지 못한 경우 예외 처리
        if (fileData == null) {
            throw new FileNotFoundException("File not found for UUID: " + uuid);
        }

    	int id = fileData.getId();
    	
    	// 파일의 메타 데이터 삭제
    	fileRepository.deleteById(id);
    	
    	// 실제 파일 경로의 파일 삭제
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    	String formattedDate = date.format(formatter);
    	String filePath = Paths.get(baseUploadDir, formattedDate, uuid + fileData.getExtension()).toString();
    	deletePhysicalFile(filePath);
    	fileRepository.deleteById(fileData.getId());
    }
    
    private void deletePhysicalFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (Files.exists(path)) {
                Files.delete(path); // 파일 삭제
            } else {
                throw new FileNotFoundException("Physical file not found: " + filePath);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete physical file: " + filePath, e);
        }
    }
}
