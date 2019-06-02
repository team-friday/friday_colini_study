package com.colini.study.core.test.domain;

import com.colini.study.core.messages.domain.configuration.RedisConfig;
import com.colini.study.core.messages.domain.model.ChatMessage;
import com.colini.study.core.messages.domain.repository.ChatMessageProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import rx.observers.TestSubscriber;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes ={RedisConfig.class, ChatMessageProvider.class})
@TestPropertySource(locations = "classpath:application-test_cache.properties")
public class MessageCachingTests {

    @Autowired
    private ChatMessageProvider provider;

    @Test
    public void messageSetTest(){

        ChatMessage message = ChatMessage.builder().channelId(1)
                                                   .message("test")
                                                   .createdAt(LocalDateTime.now().toString())
                                                   .messageId(UUID.randomUUID().toString())
                                                   .userName("choi")
                                                   .build();


       String result = provider.set(message);
       assertThat(result, is("OK"));

    }

    @Test
    public void addTest(){

        ChatMessage message = ChatMessage.builder().channelId(3)
                                                   .message("test")
                                                   .createdAt(LocalDateTime.now().toString())
                                                   .messageId(UUID.randomUUID().toString())
                                                   .userName("choi")
                                                   .build();
        //given
        boolean result = provider.add(message);

        // when then
        assertThat(result, is(true));
    }

    @Test
    public void expireTest(){

        ChatMessage message = ChatMessage.builder().channelId(99)
                .message("test")
                .createdAt(LocalDateTime.now().toString())
                .messageId(UUID.randomUUID().toString())
                .userName("choi")
                .build();
        //given
        boolean result = provider.add(message);
        boolean expireResult = provider.expireTimeSeconds(message.getChannelId());

        // when then
        assertThat(result, is(true));
        assertThat(expireResult, is(true));
    }

    @Test
    public void messageGetAllTest(){

        long channelId = 100;

        ChatMessage message = ChatMessage.builder().channelId(channelId)
                .message("echo")
                .createdAt(LocalDateTime.now().toString())
                .messageId(UUID.randomUUID().toString())
                .userName("choi")
                .build();

        boolean setResult = provider.add(message);


        TestSubscriber<List<ChatMessage>> subscriber = new TestSubscriber<>();

        provider.getAll(channelId).subscribe(subscriber);

        /*
            event가 complete 될 때까지 기다린다.
         */
        subscriber.awaitTerminalEvent();

        //then
        assertThat(setResult, is(true));

        subscriber.assertCompleted();
        subscriber.assertNoErrors();
        subscriber.assertValueCount(1); //list size
        assertThat(subscriber.getOnNextEvents().size(), is(1)); //values size
    }

}
