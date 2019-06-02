package com.colini.study.messaging.service;

import com.colini.study.core.messages.domain.model.ChatMessage;
import com.colini.study.core.messages.domain.repository.ChatMessageProvider;
import com.colini.study.messaging.handler.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketSession;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final SessionManager sessionManager;
    private final ChatMessageProvider messageProvider;

    public boolean addSession(long channelId, WebSocketSession webSocketSession) {
        if (!sessionManager.isExist(channelId, webSocketSession)) {
            sessionManager.put(channelId, webSocketSession);
        }
        return sessionManager.isExist(channelId, webSocketSession);
    }

    public boolean removeSession(WebSocketSession webSocketSession) {
        return sessionManager.remove(webSocketSession);
    }

    public boolean saveMessage(ChatMessage chatMessage) {
        boolean addCk = messageProvider.add(chatMessage);
        boolean setEx = false;
        if (addCk) {
            setEx = messageProvider.expireTimeSeconds(chatMessage.getChannelId());
        }
        return addCk && setEx;
    }
}
