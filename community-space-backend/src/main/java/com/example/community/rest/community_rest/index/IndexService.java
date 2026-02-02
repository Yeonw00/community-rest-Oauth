package com.example.community.rest.community_rest.index;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.community.rest.community_rest.file.Upload;
import com.example.community.rest.community_rest.post.Post;
import com.example.community.rest.community_rest.user.UserDTO;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.DeleteByQueryRequest;
import co.elastic.clients.elasticsearch.core.DeleteByQueryResponse;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

@Service
public class IndexService {	
	public static void indexPost(Post post) {
		ElasticsearchClient client = IndexConfig.getClient();
		String index = "post-index";
		try {
	        IndexRequest<Object> request = IndexRequest.of(i -> i
	                .index(index) // 색인 이름
	                .document(post) // 변환된 문서 삽입
	        );
			
			IndexResponse response = client.index(request);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static List<Object> searchPostByField(String field, String query) {
        ElasticsearchClient client = IndexConfig.getClient();
        String indexName = "post-index";
        List<Object> results = new ArrayList<>();

        try {
            // 검색 요청 생성
            SearchRequest request = SearchRequest.of(s -> s
                .index(indexName)
                .query(q -> q
            		.bool(b -> b
                        .should(sh -> sh
                    		.match(m -> m  // match 쿼리를 사용하여 대소문자 구분 없이 검색
                                .field(field)
                                .query(query)
                            )
                        )
                    )
                )
            );

            // 검색 요청 실행 및 응답 처리
            SearchResponse<Map> response = client.search(request, Map.class);
            System.out.println(response);

            // 검색 결과 출력
            List<Hit<Map>> hits = response.hits().hits();
            for (Hit<Map> hit : hits) {
            	Map<String, Object> source = hit.source();
            	// Retrieve and format uploadDate
                if (source != null && source.get("uploadDate") instanceof List) {
                    List<Integer> uploadDate = (List<Integer>) source.get("uploadDate");
                    if(uploadDate.size() >= 6) {
                    	LocalDateTime dateTime = LocalDateTime.of(
                    			uploadDate.get(0), uploadDate.get(1), uploadDate.get(2),
                    			uploadDate.get(3), uploadDate.get(4), uploadDate.get(5)
                    			);
                    	String formattedDate = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                    	source.put("uploadDate", formattedDate);  // Replace with ISO formatted date
                    } else {
                    	System.out.println("Invalid uploadDate format: " + uploadDate);
                    }
                }
                results.add(source);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }
	

	
	public static void indexUser(UserDTO user) {
		ElasticsearchClient client = IndexConfig.getClient();
		LocalDate localDate = user.getBirthDate();
		String index = "user-index";
		
		try {
			// 예제 데이터 삽입
			IndexRequest<Object> request = IndexRequest.of(i -> i
					.index(index) // 색인 이름
					.document(user)
					);
			
			IndexResponse response = client.index(request);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static void indexUpload(Upload upload, String index) {
		ElasticsearchClient client = IndexConfig.getClient();
		
		try {
			IndexRequest<Object> request = IndexRequest.of(i -> i
					.index(index) // 색인 이름
					.document(upload)
					);
			
			IndexResponse response = client.index(request);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static void deleteIndexById(int id, String index) {
		// Elasticsearch 클라이언트 가져오기
		ElasticsearchClient client = IndexConfig.getClient(); // Client 생성 메서드
		
		try {
			// Delete By Query 요청 생성
			DeleteByQueryRequest request = DeleteByQueryRequest.of(d -> d
					.index(index) // 삭제할 인덱스
					.query(q -> q
							.term(t -> t
									.field("id") // 필드 이름 (예: "id")
									.value(id) // 삭제 기준 값
									)
							)
					);
			
			// 요청 실행
			DeleteByQueryResponse response = client.deleteByQuery(request);
			
		} catch (ElasticsearchException e) {
			System.err.println("ElasticsearchException: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
