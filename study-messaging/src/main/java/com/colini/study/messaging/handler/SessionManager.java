package com.colini.study.messaging.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class SessionManager {
    private Map<Long, List<WebSocketSession>> sessionMap;

    protected Map<Long, List<WebSocketSession>> getSessionMap() {
        if (sessionMap == null) {
            sessionMap = Collections.synchronizedMap(new HashMap<>());
        }
        return sessionMap;
    }

    public void put(long channelId, WebSocketSession session){
        if(getSessionMap().containsKey(channelId)){
             getSessionMap().get(channelId).add(session);
        }
        getSessionMap().put(channelId, Stream.of(session).collect(Collectors.toList()));
    }

    public List<WebSocketSession> getAll(long channelId){
        return getSessionMap().get(channelId);
    }

    public WebSocketSession get(long channelId, WebSocketSession session){
        return getSessionMap().get(channelId).stream().filter(Predicate.isEqual(session)).findAny().get();
    }
}
