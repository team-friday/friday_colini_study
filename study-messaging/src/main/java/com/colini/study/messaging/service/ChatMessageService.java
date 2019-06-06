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

    //exist login 제거
    public void addSession(long channelId, WebSocketSession webSocketSession) {
        sessionManager.put(channelId, webSocketSession);
    }

    public boolean removeSession(long channelId, WebSocketSession webSocketSession) {
        return sessionManager.remove(channelId, webSocketSession);
    }

    // TODO: multi로 변경
    public boolean saveMessage(ChatMessage chatMessage) {
        boolean isSaved = messageProvider.add(chatMessage);
        boolean isSuccess = false;
        if (isSaved) {
            isSuccess = messageProvider.expireTimeSeconds(chatMessage.getChannelId());
        }
        return isSaved && isSuccess;
    }
}
