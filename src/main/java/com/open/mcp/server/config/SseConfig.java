package com.open.mcp.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * SSE配置类
 *
 * @author song.chang
 */
@Configuration
@EnableScheduling
public class SseConfig {

    private final SseConnectionManager connectionManager;

    public SseConfig(SseConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * 配置异步任务执行器
     */
    @Bean
    public Executor taskExecutor() {
        return Executors.newCachedThreadPool();
    }
    
    /**
     * 定期发送心跳，每15秒一次
     */
    @Scheduled(fixedRate = 15000)
    public void sendHeartbeat() {
        connectionManager.sendHeartbeat();
    }
} 