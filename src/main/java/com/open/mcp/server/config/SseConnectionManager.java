package com.open.mcp.server.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class SseConnectionManager {
    private static final Logger log = LoggerFactory.getLogger(SseConnectionManager.class);
    
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, Long> lastActivityTime = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    
    // 连接超时时间（毫秒）
    private static final long CONNECTION_TIMEOUT = 60000; // 60秒
    
    public SseConnectionManager() {
        // 启动定期清理任务，每30秒检查一次过期连接
        scheduler.scheduleAtFixedRate(this::cleanupStaleConnections, 30, 30, TimeUnit.SECONDS);
    }
    
    /**
     * 清理过期的连接
     */
    private void cleanupStaleConnections() {
        long now = System.currentTimeMillis();
        Set<String> sessionIds = lastActivityTime.keySet();
        
        for (String sessionId : sessionIds) {
            Long lastActivity = lastActivityTime.get(sessionId);
            if (lastActivity != null && (now - lastActivity) > CONNECTION_TIMEOUT) {
                log.info("清理过期连接: {}", sessionId);
                removeEmitter(sessionId);
            }
        }
    }

    public SseEmitter createEmitter(String sessionId) {
        // 先移除可能存在的旧连接
        removeEmitter(sessionId);
        
        SseEmitter emitter = new SseEmitter(-1L);
        
        emitter.onCompletion(() -> {
            log.debug("SSE连接完成: {}", sessionId);
            removeEmitter(sessionId);
        });
        
        emitter.onTimeout(() -> {
            log.debug("SSE连接超时: {}", sessionId);
            removeEmitter(sessionId);
        });
        
        emitter.onError(ex -> {
            log.debug("SSE连接错误: {}, 原因: {}", sessionId, ex.getMessage());
            removeEmitter(sessionId);
        });
        
        emitters.put(sessionId, emitter);
        updateLastActivityTime(sessionId);
        
        // 发送初始心跳以确保连接有效
        try {
            emitter.send(SseEmitter.event().name("connect").data("连接已建立"));
        } catch (IOException e) {
            log.warn("无法发送初始心跳: {}", e.getMessage());
            removeEmitter(sessionId);
            return null;
        }
        
        return emitter;
    }

    public void removeEmitter(String sessionId) {
        SseEmitter emitter = emitters.remove(sessionId);
        lastActivityTime.remove(sessionId);
        
        if (emitter != null) {
            try {
                emitter.complete();
            } catch (Exception e) {
                log.warn("关闭SSE连接时出错: {}", e.getMessage());
            }
        }
    }

    public void sendToAll(String event, Object data) {
        emitters.forEach((sessionId, emitter) -> {
            sendToEmitter(sessionId, emitter, event, data);
        });
    }

    public boolean hasEmitter(String sessionId) {
        return emitters.containsKey(sessionId);
    }

    public void sendToSession(String sessionId, String event, Object data) {
        SseEmitter emitter = emitters.get(sessionId);
        if (emitter != null) {
            sendToEmitter(sessionId, emitter, event, data);
        } else {
            log.debug("尝试向不存在的会话发送消息: {}", sessionId);
        }
    }
    
    /**
     * 向指定的SSE发射器发送消息
     */
    private void sendToEmitter(String sessionId, SseEmitter emitter, String event, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .name(event)
                    .data(data));
            updateLastActivityTime(sessionId);
        } catch (IOException e) {
            log.debug("发送消息失败，移除连接: {}, 原因: {}", sessionId, e.getMessage());
            removeEmitter(sessionId);
        } catch (Exception e) {
            log.error("发送SSE消息时发生未预期的错误: {}", e.getMessage());
            removeEmitter(sessionId);
        }
    }
    
    /**
     * 更新会话的最后活动时间
     */
    private void updateLastActivityTime(String sessionId) {
        lastActivityTime.put(sessionId, System.currentTimeMillis());
    }
    
    /**
     * 发送心跳以保持连接活跃
     */
    public void sendHeartbeat() {
        sendToAll("heartbeat", System.currentTimeMillis());
    }
} 