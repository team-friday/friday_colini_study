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

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ChannelMemberTests {

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper MAPPER = new ObjectMapper();

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                                                    MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));


    @Test
    public void post_channel_member() throws Exception {

        //given
        Channel channel = Channel.builder().channelName("java").userName("choi").build();
        channel.setChannelId(1l);

        Map map = new HashMap();
        map.put("channel", channel);
        map.put("authority", "W");
        map.put("userName", "kim");

        String content = MAPPER.writeValueAsString(map);

        //when
        MvcResult result =  mockMvc.perform(post("/channel/member/add").contentType(contentType).content(content))
                                   .andExpect(status().isOk())
                                   .andReturn();


        ChannelMember response = MAPPER.readValue(result.getResponse().getContentAsString(), ChannelMember.class);
        //then
        assertThat(response.getUserName(), is(map.get("userName")));
        assertThat(response.getAuthority(), is(map.get("authority")));
    }

    @Test
    public void get_channel_member() throws Exception {

        //given, when
        MvcResult result =  mockMvc.perform(get("/channel/member/1"))
                                   .andExpect(status().isOk())
                                   .andExpect(content().string(containsString("choi")))
                                   .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                                   .andDo(print())
                                   .andReturn();

        ChannelMember member = MAPPER.readValue(result.getResponse().getContentAsString(), ChannelMember.class);

        //then
        assertThat(member.getUserName(), is("choi"));
        assertThat(member.getAuthority(), is("M"));
    }

    @Test
    public void patch_channel_member() throws Exception {

        //given
        Channel channel = Channel.builder().channelName("java").userName("choi").build();
        channel.setChannelId(1l);

        Map map = new HashMap();
        map.put("channel", channel);
        map.put("authority", "W");
        map.put("userName", "kim");

        String content = MAPPER.writeValueAsString(map);

        // when
        MvcResult result =  mockMvc.perform(patch("/channel/member/1").contentType(contentType).content(content))
                                   .andExpect(status().isOk())
                                   .andExpect(content().string(containsString("kim")))
                                   .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                                   .andDo(print())
                                   .andReturn();

        ChannelMember member_get = MAPPER.readValue(result.getResponse().getContentAsString(), ChannelMember.class);

        //then
        assertThat(member_get.getUserName(), is("kim"));
        assertThat(member_get.getAuthority(), is("W"));

    }

    @Test
    public void delete_channel_member() throws Exception {

        Channel channel = Channel.builder().channelName("java").userName("choi").build();
        channel.setChannelId(1l);

        Map map = new HashMap();
        map.put("channel", channel);
        map.put("authority", "W");
        map.put("userName", "testUser");

        String content = MAPPER.writeValueAsString(map);

        //when
        MvcResult add_result = mockMvc.perform(post("/channel/member/add").contentType(contentType).content(content))
                                      .andExpect(status().isOk())
                                      .andReturn();

        ChannelMember readMember = MAPPER.readValue(add_result.getResponse().getContentAsString(), ChannelMember.class);


        //given, when
        MvcResult delete_result =  mockMvc.perform(delete("/channel/member/"+readMember.getId()))
                                   .andExpect(status().isOk())
                                   .andExpect(content().string(containsString("testUser")))
                                   .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                                   .andDo(print())
                                   .andReturn();

        ChannelMember member = MAPPER.readValue(delete_result.getResponse().getContentAsString(), ChannelMember.class);

        //then
        assertThat(member.getUserName(), is("testUser"));
        assertThat(member.getAuthority(), is("W"));

    }
}
