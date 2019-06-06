package com.colini.study.messaging.test.intergration;

import com.colini.study.core.messages.domain.model.ChatMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ReplayProcessor;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.UUID;


public class MessageClientTests {

    private final static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws URISyntaxException, InterruptedException {

        ChatMessage message = ChatMessage.builder().message("test").userName("choi")
                .channelId(1l).messageId(UUID.randomUUID().toString()).build();


        ReplayProcessor<ChatMessage> output = ReplayProcessor.create(2);
        WebSocketClient client = new ReactorNettyWebSocketClient();
        client.execute(
                URI.create("ws://localhost:8081/study/message/v1"),
                session -> {
                       Flux textFlux = Flux.generate(synchronousSink -> {
                            try {
                                synchronousSink.next(session.textMessage(mapper.writeValueAsString(message)));
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                        });

                        return session.send(
                                Flux.interval(Duration.ofSeconds(3))
                                        .zipWith(textFlux,(time, event) -> event).log())
                                .thenMany(session.receive().log()
                                        .map(WebSocketMessage::getPayloadAsText)
                                        .map(s -> {
                                            try {
                                                return mapper.readValue(s,ChatMessage.class);
                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }finally {

                                            }
                                        })
                                        .subscribeWith(output)
                                        .log())
                                .then();

                }).block(Duration.ofSeconds(5000));
    }
}
