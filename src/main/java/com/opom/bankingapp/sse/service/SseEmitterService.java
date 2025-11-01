package com.opom.bankingapp.sse.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SseEmitterService {

    private final Map<String, List<SseEmitter>> topicEmitters = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    public SseEmitter createEmitter(String topic) {
        SseEmitter emitter = new SseEmitter(0L);
        topicEmitters.computeIfAbsent(topic, t -> new CopyOnWriteArrayList<>()).add(emitter);
        
        Runnable cleanup = () -> {
            List<SseEmitter> emitters = topicEmitters.get(topic);
            if (emitters != null) {
                emitters.remove(emitter);
                if (emitters.isEmpty()) {
                    topicEmitters.remove(topic);
                }
            }
        };
        emitter.onCompletion(() -> topicEmitters.get(topic).remove(emitter));
        emitter.onTimeout(() -> topicEmitters.get(topic).remove(emitter));
        emitter.onError(e -> cleanup.run());
        return emitter;
    }
    
    public void broadcast(String topic, Object message) {
        List<SseEmitter> emitters = topicEmitters.get(topic);
        if (emitters != null) {
            String jsonMessage;
            try {
                jsonMessage = objectMapper.writeValueAsString(message);
            } catch (Exception e) {
                throw new RuntimeException("Failed to serialize SSE message", e);
            }
            for (SseEmitter emitter : new ArrayList<>(emitters)) {
                try {
                    emitter.send(SseEmitter.event().data(jsonMessage).name("message"));
                } catch (Exception e) {
                    emitters.remove(emitter);
                }
            }
            if (emitters.isEmpty()) {
                topicEmitters.remove(topic);
            }
        }
    }
}

