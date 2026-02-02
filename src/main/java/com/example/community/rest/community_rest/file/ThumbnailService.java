package com.example.community.rest.community_rest.file;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;

import net.coobird.thumbnailator.Thumbnails;

@Service
public class ThumbnailService {
	
	private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public CompletableFuture<Void> createThumbnailAsync(String inputFilePath, String outputFilePath) {
        return CompletableFuture.runAsync(() -> {
            try {
                createThumbnail(inputFilePath, outputFilePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, executorService);
    }
	
    public void createThumbnail(String inputFilePath, String outputFilePath) throws IOException {
        File inputFile = new File(inputFilePath);
        File outputFile = new File(outputFilePath);
        Thumbnails.of(inputFile)
                  .size(50, 50)
                  .toFile(outputFile);
    }
}
