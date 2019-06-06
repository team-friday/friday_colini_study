package com.colini.study.messaging.test.service;

import com.colini.study.core.messages.domain.model.ChatMessage;
import com.colini.study.core.messages.domain.repository.ChatMessageProvider;
import com.colini.study.messaging.handler.SessionManager;
import com.colini.study.messaging.service.ChatMessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.reactive.socket.WebSocketSession;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ChatMessageServiceTests {
    @Mock
    private SessionManager sessionManager;
    @Mock
    private ChatMessageProvider messageProvider;
    @InjectMocks
    private ChatMessageService messageService;

    @Test
    public void addSessionTest(){
        long channelId= 1l;
        WebSocketSession session = mock(WebSocketSession.class);


        messageService.addSession(channelId,session);

        verify(sessionManager, times(1)).put(channelId, session);

//        given(sessionManager.get(channelId,session)).willReturn(session);
    }

    @Test
    public void saveMessageTest(){
        ChatMessage message = ChatMessage.builder().channelId(1)
                                                   .userName("choi")
                                                   .messageId(UUID.randomUUID().toString())
                                                   .message("test").build();

        given(messageProvider.add(message)).willReturn(true);
        given(messageProvider.expireTimeSeconds(message.getChannelId())).willReturn(true);

        boolean saves = messageService.saveMessage(message);

        verify(messageProvider, times(1)).add(message);
        verify(messageProvider, times(1)).expireTimeSeconds(message.getChannelId());

        assertThat(saves).isEqualTo(true);

    }
}
