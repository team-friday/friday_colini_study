package com.colini.study.messaging.test.handler;

import com.colini.study.messaging.handler.SessionManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.socket.CloseStatus;
import org.springframework.web.reactive.socket.HandshakeInfo;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;


@ActiveProfiles("local")
@RunWith(SpringRunner.class)
@ContextConfiguration(classes ={SessionManager.class})
public class SessionManagerTests {

    @Autowired
    private SessionManager sessionManager;

    private WebSocketSession testSession;

    @Before
    public void setTestWebsocketSession(){
        testSession = new WebSocketSession() {
            @Override
            public String getId() {
                return "testId";
            }

            @Override
            public HandshakeInfo getHandshakeInfo() {
                return null;
            }

            @Override
            public DataBufferFactory bufferFactory() {
                return null;
            }

            @Override
            public Map<String, Object> getAttributes() {
                return null;
            }

            @Override
            public Flux<WebSocketMessage> receive() {
                return null;
            }

            @Override
            public Mono<Void> send(Publisher<WebSocketMessage> messages) {
                return null;
            }

            @Override
            public Mono<Void> close(CloseStatus status) {
                return null;
            }

            @Override
            public WebSocketMessage textMessage(String payload) {
                return null;
            }

            @Override
            public WebSocketMessage binaryMessage(Function<DataBufferFactory, DataBuffer> payloadFactory) {
                return null;
            }

            @Override
            public WebSocketMessage pingMessage(Function<DataBufferFactory, DataBuffer> payloadFactory) {
                return null;
            }

            @Override
            public WebSocketMessage pongMessage(Function<DataBufferFactory, DataBuffer> payloadFactory) {
                return null;
            }
        };
    }

    @Test
    public void sessionGetTest(){
        long channelId = 1;
        sessionManager.put(channelId, testSession);

        WebSocketSession getSession = sessionManager.get(channelId, testSession);
        assertThat(testSession, is(getSession));
    }


    @Test
    public void sessionGetAllTest(){
        long channelId = 1;
        sessionManager.put(channelId, testSession);

        List<WebSocketSession> sessionList = sessionManager.getAll(channelId);

        assertNotNull(sessionList);
        assertThat(sessionList.size(), is(1));
        assertThat(sessionList.contains(testSession), is(true));
    }
}
