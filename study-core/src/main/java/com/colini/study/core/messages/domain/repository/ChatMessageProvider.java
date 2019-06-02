package com.colini.study.core.messages.domain.repository;

import com.colini.study.core.messages.domain.model.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ChatMessageProvider {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String NAMESPACE = "chat.message";
    private static final String VERSION = "v1";

    private final StatefulRedisConnection<String, String> redisConnection;
    private final long expireTimeSeconds;

    public String set(ChatMessage chatMessage) {
        try {
            String value = OBJECT_MAPPER.writeValueAsString(chatMessage);
            log.debug("Cache set: {} => {}", getKey(chatMessage.getChannelId()), value);
            return redisConnection.sync().setex(getKey(chatMessage.getChannelId()), expireTimeSeconds, value);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

    }

    public boolean add(ChatMessage chatMessage) {
        try {
            String value = OBJECT_MAPPER.writeValueAsString(chatMessage);

            log.debug("Cache hset add: {} => {}", getKey(chatMessage.getChannelId()), value);
            return redisConnection.sync().hset(getKey(chatMessage.getChannelId()), chatMessage.getMessageId(), value);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }


    public Observable<List<ChatMessage>> getAll(long id) {
        return redisConnection.reactive()
                .hgetall(getKey(id))
                .defaultIfEmpty(null)
                .map(value -> value.entrySet().stream().map(Map.Entry::getValue).collect(toList()))
                .map(list -> {
                    log.info("Cache {} => {} count :{}", (list != null) ? "hit" : "miss", getKey(id), (list != null) ? list.size() : null);
                    return convert(list,ChatMessage.class);
                });
    }

    public boolean expireTimeSeconds(long id){
        return redisConnection.sync().expire(getKey(id), expireTimeSeconds);
    }

    private String getKey(long id) {
        return String.format("%s.%s(%d)", NAMESPACE, VERSION, id);
    }

    private static List<ChatMessage> convert(List<String> list, Class<ChatMessage> type) {
       return list.stream().map(value -> {
            try {
                return OBJECT_MAPPER.readValue(value, type);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(toList());
    }

}
