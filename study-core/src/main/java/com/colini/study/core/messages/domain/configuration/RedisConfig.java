package com.colini.study.core.messages.domain.configuration;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.codec.StringCodec;
import com.lambdaworks.redis.pubsub.StatefulRedisPubSubConnection;
import com.lambdaworks.redis.resource.ClientResources;
import com.lambdaworks.redis.resource.DefaultClientResources;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RedisConfig {
    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.lettuce.io.pool-size}")
    private int ioPoolSize;

    @Value("${spring.redis.default.expire-time-seconds}")
    private long expireTimeSeconds;

    @Bean
    public long getExpireTimeSeconds(){
        return expireTimeSeconds;
    }

    @Bean
    public StatefulRedisConnection<String, String> redisConnection() {
        RedisURI redisURI = RedisURI.create(host, port);
        return RedisClient.create(clientResources(), redisURI).connect(StringCodec.UTF8);
    }

    @Bean
    public StatefulRedisPubSubConnection<String, String> redisPubSubConnection(){
        RedisURI redisURI = RedisURI.create(host, port);
        return RedisClient.create(clientResources(), redisURI).connectPubSub(StringCodec.UTF8);
    }

    private ClientResources clientResources() {
        return DefaultClientResources.builder()
                .ioThreadPoolSize(ioPoolSize)
                .build();
    }
}
