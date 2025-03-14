package com.open.mcp.server.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SseConnectionManager {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter createEmitter(String sessionId) {
        SseEmitter emitter = new SseEmitter(-1L);
        
        emitter.onCompletion(() -> {
            log.debug("SSE connection completed for session: {}", sessionId);
            removeEmitter(sessionId);
        });
        
        emitter.onTimeout(() -> {
            log.debug("SSE connection timeout for session: {}", sessionId);
            removeEmitter(sessionId);
        });
        
        emitter.onError(ex -> {
            log.error("SSE connection error for session: {}", sessionId, ex);
            removeEmitter(sessionId);
        });
        
        emitters.put(sessionId, emitter);
        return emitter;
    }

    public void removeEmitter(String sessionId) {
        emitters.remove(sessionId);
    }

    public void sendToAll(String event, Object data) {
        emitters.forEach((sessionId, emitter) -> {
            try {
                emitter.send(SseEmitter.event()
                        .name(event)
                        .data(data));
            } catch (IOException e) {
                log.error("Failed to send SSE event to session: {}", sessionId, e);
                removeEmitter(sessionId);
            }
        });
    }

    public boolean hasEmitter(String sessionId) {
        return emitters.containsKey(sessionId);
    }

    public void sendToSession(String sessionId, String event, Object data) {
        SseEmitter emitter = emitters.get(sessionId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name(event)
                        .data(data));
            } catch (IOException e) {
                log.error("Failed to send SSE event to session: {}", sessionId, e);
                removeEmitter(sessionId);
            }
        }
    }
} 