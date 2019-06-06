package com.colini.study.messaging.test.handler;

import com.colini.study.messaging.handler.StudyWebSocketHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class StudyWebSocketHandlerTests {

    @Spy
    @InjectMocks
    private StudyWebSocketHandler handler;


    @Test(expected = NullPointerException.class)
    public void handleExpectExceptionTest() {
        WebSocketSession session = mock(WebSocketSession.class);
        Mono<Void> voidMono = handler.handle(session);
        StepVerifier.create(voidMono).expectNoEvent(Duration.ofSeconds(1)).expectError().verify();
    }

}
