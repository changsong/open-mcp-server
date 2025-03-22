package com.open.mcp.server;

import com.open.mcp.server.service.NewsService;
import com.open.mcp.server.service.VideoService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Open Mcp Server
 *
 * @author song chang
 */
@SpringBootApplication
@Configuration
public class OpenMcpServer {

    public static void main(String[] args) {
        SpringApplication.run(OpenMcpServer.class, args);
    }

    @Bean
    public ToolCallbackProvider toolCallbackProvider(NewsService newsService, VideoService videoService) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(newsService, videoService)
                .build();
    }

}
