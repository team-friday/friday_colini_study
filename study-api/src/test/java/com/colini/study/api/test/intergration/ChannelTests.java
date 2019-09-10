package com.colini.study.api.test.intergration;


import com.colini.study.core.channel.domain.model.Channel;
import com.colini.study.core.channel.domain.model.ChannelMember;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ChannelTests {

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper MAPPER = new ObjectMapper();

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                                                    MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));


    @Test
    public void get_channel() throws Exception {

        //given, when
        MvcResult result =  mockMvc.perform(get("/channel/1"))
                                   .andExpect(status().isOk())
                                   .andExpect(content().string(containsString("choi")))
                                   .andExpect(content().string(containsString("java")))
                                   .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                                   .andDo(print())
                                   .andReturn();

        Channel channel = MAPPER.readValue(result.getResponse().getContentAsString(), Channel.class);

        //then
        assertThat(channel.getUserName(), is("choi"));
        assertThat(channel.getChannelName(), is("java"));

        assertThat(channel.getChannelMembers().size(), is(1));
        assertThat(channel.getChannelMembers().get(0).getAuthority(), is("M"));
        assertThat(channel.getChannelMembers().get(0).getUserName(), is("choi"));
    }

    @Test
    public void post_channel() throws Exception {

        //given
        Channel channel = Channel.builder().channelName("test").userName("testName")
                                 .build();
        String content = MAPPER.writeValueAsString(channel);

        //when
        MvcResult result =  mockMvc.perform(post("/channel/create").contentType(contentType).content(content))
                                   .andExpect(status().isOk())
                                   .andReturn();


        Channel response = MAPPER.readValue(result.getResponse().getContentAsString(), Channel.class);
        //then
        assertThat(response.getUserName(), is(channel.getUserName()));
        assertThat(response.getChannelName(), is(channel.getChannelName()));
    }
    @Test
    public void patch_channel() throws Exception {

        //given
        Channel channel = Channel.builder().channelName("C++").userName("chum")
                .build();
        String content = MAPPER.writeValueAsString(channel);

        // when
        MvcResult result =  mockMvc.perform(patch("/channel/2").contentType(contentType).content(content))
                                   .andExpect(status().isOk())
                                   .andExpect(content().string(containsString("chum")))
                                   .andExpect(content().string(containsString("C++")))
                                   .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                                   .andDo(print())
                                   .andReturn();

        Channel channel_get = MAPPER.readValue(result.getResponse().getContentAsString(), Channel.class);

        //then
        assertThat(channel_get.getUserName(), is("chum"));
        assertThat(channel_get.getChannelName(), is("C++"));

    }

    @Test
    public void delete_channel() throws Exception {

        //given, when
        MvcResult result =  mockMvc.perform(delete("/channel/3"))
                                   .andExpect(status().isOk())
                                   .andExpect(content().string(containsString("test3")))
                                   .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                                   .andDo(print())
                                   .andReturn();

        Channel channel_get = MAPPER.readValue(result.getResponse().getContentAsString(), Channel.class);

        //then
        assertThat(channel_get.getUserName(), is("test3"));
        assertThat(channel_get.getChannelName(), is("test3"));

    }

    @Test
    public void get_members() throws Exception {

        //given, when
        MvcResult result =  mockMvc.perform(get("/channel/1/members"))
                                   .andExpect(status().isOk())
                                   .andExpect(content().string(containsString("choi")))
                                   .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                                   .andDo(print())
                                   .andReturn();

        ChannelMember[] members = MAPPER.readValue(result.getResponse().getContentAsString(), ChannelMember[].class);

        //then
        assertThat(members.length, is(1));
        assertThat(members[0].getUserName(), is("choi"));
        assertThat(members[0].getAuthority(), is("M"));

    }
}
