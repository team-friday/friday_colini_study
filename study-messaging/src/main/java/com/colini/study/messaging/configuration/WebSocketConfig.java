package com.colini.study.messaging.configuration;

import com.colini.study.core.messages.domain.model.ChatMessage;
import com.colini.study.messaging.handler.StudyWebSocketHandler;
import com.colini.study.messaging.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebSocketConfig {

    @Autowired
    private ChatMessageService messageService;

    @Bean
    public HandlerMapping webSocketMapping(UnicastProcessor<ChatMessage> eventPublisher, Flux<ChatMessage> events) {
        Map<String, WebSocketHandler> map = new HashMap<>();
        StudyWebSocketHandler webSocketHandler = new StudyWebSocketHandler(messageService, eventPublisher, events);
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();

        map.put("/study/message/v1", webSocketHandler);
        mapping.setOrder(1);
        mapping.setUrlMap(map);
        // Cors 관련 처리 추가
        mapping.setCorsConfigurations(Collections.singletonMap("*", new CorsConfiguration().applyPermitDefaultValues()));
        return mapping;
    }

    @Bean
    public UnicastProcessor<ChatMessage> eventPublisher(){
        return UnicastProcessor.create();
    }

    /**
     * @{link} https://projectreactor.io/docs/core/release/api/reactor/core/publisher/UnicastProcessor.html
     */
    @Bean
    public Flux<ChatMessage> events() {
        return eventPublisher()
                .replay(20) // convert to ConnectableFlux(hot observable) - history values 30
                .autoConnect();
    }
    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}

