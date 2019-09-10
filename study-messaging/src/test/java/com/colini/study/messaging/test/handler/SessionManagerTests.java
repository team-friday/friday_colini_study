package com.colini.study.messaging.test.handler;

import com.colini.study.messaging.handler.SessionManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.reactive.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;


@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes ={SessionManager.class})
public class SessionManagerTests {

    @Spy
    private SessionManager sessionManager;
    private WebSocketSession testSession;

    @Before
    public void setTestWebsocketSession(){
        testSession = mock(WebSocketSession.class);
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

    @Test
    public void isExistTest(){
        long channelId = 1;
        sessionManager.put(channelId, testSession);

        boolean isExist = sessionManager.isExist(channelId,testSession);

        Assert.assertTrue(isExist);
    }

    @Test
    public void isNotExistTest(){
        long channelId = 1;
        sessionManager.getSessionMap().put(channelId, new ArrayList<>());
        boolean isExist = sessionManager.isExist(channelId,testSession);

        Assert.assertFalse(isExist);
    }

    @Test
    public void manySessionGetTest(){
        WebSocketSession socketSession = mock(WebSocketSession.class);
        WebSocketSession socketSession2 = mock(WebSocketSession.class);

        long channelId = 1;
        sessionManager.put(channelId, testSession);
        sessionManager.put(channelId, socketSession);
        sessionManager.put(channelId, socketSession2);

        List<WebSocketSession> sessionList = sessionManager.getAll(channelId);

        sessionList.stream().forEach(System.out::println);

        Assert.assertEquals(sessionList.size(), 3);
        Assert.assertTrue(sessionList.contains(testSession));
        Assert.assertTrue(sessionList.contains(socketSession));
        Assert.assertTrue(sessionList.contains(socketSession2));
    }
}
