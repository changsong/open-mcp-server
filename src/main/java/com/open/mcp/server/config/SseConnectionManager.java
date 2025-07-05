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
    
    // Connection timeout (milliseconds)
    private static final long CONNECTION_TIMEOUT = 60000; // 60 seconds
    
    public SseConnectionManager() {
        // Start periodic cleanup task, check expired connections every 30 seconds
        scheduler.scheduleAtFixedRate(this::cleanupStaleConnections, 30, 30, TimeUnit.SECONDS);
    }
    
    /**
     * Clean up expired connections
     */
    private void cleanupStaleConnections() {
        long now = System.currentTimeMillis();
        Set<String> sessionIds = lastActivityTime.keySet();
        
        for (String sessionId : sessionIds) {
            Long lastActivity = lastActivityTime.get(sessionId);
            if (lastActivity != null && (now - lastActivity) > CONNECTION_TIMEOUT) {
                log.info("Cleaning up expired connection: {}", sessionId);
                removeEmitter(sessionId);
            }
        }
    }

    public SseEmitter createEmitter(String sessionId) {
        // Remove existing old connection first
        removeEmitter(sessionId);
        
        SseEmitter emitter = new SseEmitter(-1L);
        
        emitter.onCompletion(() -> {
            log.debug("SSE connection completed: {}", sessionId);
            removeEmitter(sessionId);
        });
        
        emitter.onTimeout(() -> {
            log.debug("SSE connection timeout: {}", sessionId);
            removeEmitter(sessionId);
        });
        
        emitter.onError(ex -> {
            log.debug("SSE connection error: {}, reason: {}", sessionId, ex.getMessage());
            removeEmitter(sessionId);
        });
        
        emitters.put(sessionId, emitter);
        updateLastActivityTime(sessionId);
        
        // Send initial heartbeat to ensure connection is valid
        try {
            emitter.send(SseEmitter.event().name("connect").data("连接已建立"));
        } catch (IOException e) {
            log.warn("Unable to send initial heartbeat: {}", e.getMessage());
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
                log.warn("Error occurred while closing SSE connection: {}", e.getMessage());
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
            log.debug("Attempting to send message to non-existent session: {}", sessionId);
        }
    }
    
    /**
     * Send message to specified SSE emitter
     */
    private void sendToEmitter(String sessionId, SseEmitter emitter, String event, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .name(event)
                    .data(data));
            updateLastActivityTime(sessionId);
        } catch (IOException e) {
            log.debug("Failed to send message, removing connection: {}, reason: {}", sessionId, e.getMessage());
            removeEmitter(sessionId);
        } catch (Exception e) {
            log.error("Unexpected error occurred while sending SSE message: {}", e.getMessage());
            removeEmitter(sessionId);
        }
    }
    
    /**
     * Update last activity time for session
     */
    private void updateLastActivityTime(String sessionId) {
        lastActivityTime.put(sessionId, System.currentTimeMillis());
    }
    
    /**
     * Send heartbeat to keep connection alive
     */
    public void sendHeartbeat() {
        sendToAll("heartbeat", System.currentTimeMillis());
    }
} 