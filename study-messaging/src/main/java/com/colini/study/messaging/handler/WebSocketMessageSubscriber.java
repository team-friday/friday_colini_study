package com.colini.study.messaging.handler;


import com.colini.study.core.messages.domain.model.ChatMessage;
import reactor.core.publisher.UnicastProcessor;

import java.util.Optional;

public  class  WebSocketMessageSubscriber {
    private UnicastProcessor<ChatMessage> messagePublisher;
    private Optional<ChatMessage> lastReceivedMessage = Optional.empty();

    public WebSocketMessageSubscriber(UnicastProcessor<ChatMessage> messagePublisher) {
        this.messagePublisher = messagePublisher;
    }

    public void onNext(ChatMessage message) {
        lastReceivedMessage = Optional.of(message);
        messagePublisher.onNext(message);
    }

    public void onError(Throwable error) {
        error.printStackTrace();
    }

    public void onComplete() {
        lastReceivedMessage.ifPresent(messagePublisher::onNext);
    }
}