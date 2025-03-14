package com.open.mcp.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
public class SseConfig {

    @Autowired
    private SseConnectionManager connectionManager;

    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        
        // 添加JVM关闭钩子，确保优雅关闭
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
            }
        }));
        
        return executorService;
    }

    @Scheduled(fixedRate = 15000) // 每15秒发送一次心跳
    public void sendHeartbeat() {
        connectionManager.sendToAll("heartbeat", System.currentTimeMillis());
    }
} 