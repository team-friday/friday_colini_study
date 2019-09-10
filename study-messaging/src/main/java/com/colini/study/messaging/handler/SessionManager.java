package com.colini.study.messaging.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;

import java.util.*;
import java.util.function.Predicate;

@Slf4j
@Component
public class SessionManager {
    private final Map<Long, List<WebSocketSession>> sessionMap = Collections.synchronizedMap(new HashMap<>());

    public Map<Long, List<WebSocketSession>> getSessionMap() {
        return sessionMap;
    }

    public void put(long channelId, WebSocketSession session) {
        log.info("websocket session add += {}", session);
        if (getSessionMap().containsKey(channelId)) {
            getSessionMap().get(channelId).add(session);
        } else {
            getSessionMap().put(channelId, new ArrayList<>(Arrays.asList(session)));
        }
    }

    /*
     * 로직 변경 필요 key set.
     */
    public boolean remove(long channelId, WebSocketSession session) {
        log.info("websocket session remove -= {}", session);
        return getSessionMap().get(channelId).removeIf(Predicate.isEqual(session));
    }

    public List<WebSocketSession> getAll(long channelId) {
        return getSessionMap().get(channelId);
    }

    public WebSocketSession get(long channelId, WebSocketSession session) {
        return getSessionMap().get(channelId).stream()
                .filter(Predicate.isEqual(session))
                .findAny()
                .orElseThrow(RuntimeException::new);
    }

    public boolean isExist(long channelId, WebSocketSession session) {
        return getSessionMap().get(channelId) != null &&
                getSessionMap().get(channelId).contains(session);
    }

}
