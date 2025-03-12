package com.example.community.rest.community_rest.index;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;

public class IndexConfig {
	private static RestClient restClient;
    private static ElasticsearchClient elasticsearchClient;

    static {
        try {
            // RestClient 초기화
            restClient = RestClient.builder(
                    new HttpHost("localhost", 9200, "http")
            ).build();

            // ObjectMapper에 JavaTimeModule 등록
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            // ElasticsearchClient 초기화
            elasticsearchClient = new ElasticsearchClient(
                    new RestClientTransport(restClient, new JacksonJsonpMapper(objectMapper))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ElasticsearchClient getClient() {
        return elasticsearchClient;
    }

    public static void close() {
        try {
            if (restClient != null) {
                restClient.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
