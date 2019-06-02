package com.colini.study.messaging.handler;

import com.colini.study.core.messages.domain.model.ChatMessage;
import com.colini.study.messaging.service.ChatMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.web.reactive.socket.CloseStatus;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
public class StudyWebSocketHandler implements WebSocketHandler {

    private final ChatMessageService chatMessageService;
    private final UnicastProcessor<ChatMessage> eventPublisher;
    private final Flux<ChatMessage> output;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {

        WebSocketMessageSubscriber subscriber = new WebSocketMessageSubscriber(eventPublisher);

        webSocketSession.receive().log()
                .map(WebSocketMessage::getPayloadAsText)
                .map(this::toChatMessage)
                .doOnNext(chatMessage -> chatMessageService.addSession(chatMessage.getChannelId(), webSocketSession))
                .doOnNext(chatMessageService::saveMessage)
                .doOnComplete(() -> {
                    webSocketSession.close(CloseStatus.GOING_AWAY);
                    chatMessageService.removeSession(webSocketSession);
                })
                .doOnError(throwable -> {
                    log.error(throwable.getMessage());
                    webSocketSession.close(CloseStatus.BAD_DATA);
                })
                .subscribeOn(Schedulers.parallel())
                .subscribe(subscriber::onNext, subscriber::onError, subscriber::onComplete);

        Flux<WebSocketMessage> pongFlux = Flux.generate(synchronousSink -> {
            synchronousSink.next(webSocketSession.pongMessage(DataBufferFactory::allocateBuffer));
        });

        return webSocketSession.send(
                Flux.from(output).map(this::toJSON)
                        .map(webSocketSession::textMessage)
                        .concatWith(Flux.interval(Duration.ofSeconds(3))
                                        .zipWith(pongFlux, (time, event) -> event).log()));
      }


    private ChatMessage toChatMessage(String payload){
        try {
            return mapper.readValue(payload, ChatMessage.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private String toJSON(ChatMessage chatMessage){
        try {
            return mapper.writeValueAsString(chatMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
