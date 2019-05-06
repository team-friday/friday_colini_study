package com.colini.study.core.test.domain;

import com.colini.study.core.channel.EnableChannelDomain;
import com.colini.study.core.channel.configuration.StudyDomainContextConfig;
import com.colini.study.core.channel.domain.model.Channel;
import com.colini.study.core.channel.domain.model.ChannelMember;
import com.colini.study.core.channel.domain.repository.ChannelJpaRepository;
import com.colini.study.core.channel.domain.repository.ChannelMemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@DataJpaTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = StudyDomainContextConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class RepositoryTests {

    @Autowired
    ChannelJpaRepository channelJpaRepository;

    @Autowired
    ChannelMemberRepository channelMemberRepository;

    @Test
    @Transactional(readOnly = true)
    public void channelFindTest(){
        Channel channel =channelJpaRepository.findById(1l).get();

        assertThat(channel.getUserName(), is("choi"));
        assertThat(channel.getChannelId(), is(1l));
        assertThat(channel.getChannelName(), is("java"));
    }

    @Test
    @Transactional(readOnly = true)
    public void channelMemberFindTest(){
        ChannelMember member =channelMemberRepository.findById(1l).get();

        assertThat(member.getUserName(), is("choi"));
        assertThat(member.getAuthority(), is("M"));
        assertThat(member.getChannel().getChannelId(), is(1l));
        assertThat(member.getChannel().getChannelName(), is("java"));
    }

    @Test
    @Transactional
    public void channelInsertTest(){
        Channel channel = Channel.builder().channelName("C++")
                                           .userName("chul")
                                           .build();

        Channel getChannel = channelJpaRepository.save(channel);

        assertThat(getChannel.getUserName(), is("chul"));
        assertThat(getChannel.getChannelName(), is("C++"));
        assertThat(getChannel.getChannelId(), is(2l));
    }

    @Test
    @Transactional
    public void channelMemberInsertTest(){
        Channel channel =channelJpaRepository.findById(1l).get();

        ChannelMember channelMember = ChannelMember.builder().channel(channel)
                                                             .userName("KIM")
                                                             .authority("M")
                                                             .build();

        ChannelMember getChannelMember = channelMemberRepository.save(channelMember);

        assertThat(getChannelMember.getUserName(), is("KIM"));
        assertThat(getChannelMember.getAuthority(), is("M"));
        assertThat(getChannelMember.getChannel().getChannelId(), is(1l));
        assertThat(getChannelMember.getChannel().getChannelName(), is("java"));
        assertThat(getChannelMember.getChannel().getUserName(), is("choi"));
    }

}
