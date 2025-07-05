package com.open.mcp.server;

import com.open.mcp.server.api.service.NewsService;
import com.open.mcp.server.api.service.StockService;
import com.open.mcp.server.api.service.VideoService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Open Mcp Server
 *
 * @author song chang
 */
@SpringBootApplication(exclude = {
    org.apache.dubbo.spring.boot.autoconfigure.DubboAutoConfiguration.class,
    org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration.class
})
@Configuration
public class OpenMcpServer {

    public static void main(String[] args) {
        SpringApplication.run(OpenMcpServer.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ToolCallbackProvider toolCallbackProvider(NewsService newsService, VideoService videoService, StockService stockService) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(newsService, videoService, stockService)
                .build();
    }

}

