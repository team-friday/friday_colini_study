package com.colini.study.core.test.domain;

import com.colini.study.core.channel.EnableChannelDomain;
import com.colini.study.core.channel.domain.model.Channel;
import com.colini.study.core.channel.domain.model.ChannelMember;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;



@DataJpaTest
@RunWith(SpringRunner.class)
@EnableChannelDomain
@TestPropertySource(locations = "classpath:application-test.properties")
public class EntityTests {

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void EntityBuilderTest() {

        String userName = "choi";
        String channelName = "java";

        Channel channel = Channel.builder().channelName("java")
                                           .userName("choi")
                                           .build();

        assertThat(channel.getUserName(), is(userName));
        assertThat(channel.getChannelName(), is(channelName));

        ChannelMember channelMember = ChannelMember.builder().channel(channel)
                                                             .userName("KIM")
                                                             .authority("M")
                                                             .build();

        assertThat(channelMember.getAuthority(), is("M"));
        assertThat(channelMember.getUserName(), is("KIM"));
        assertThat(channelMember.getChannel().getChannelName(), is(channelName));
    }

    @Test
    public void channelFindTest() {
        Channel getChannel = testEntityManager.find(Channel.class, 1l);

        assertThat(getChannel.getUserName(), is("choi"));
        assertThat(getChannel.getChannelName(), is("java"));
        assertThat(getChannel.getChannelId(), is(1l));
    }


    @Test
    public void channelMemberFindTest() {
        ChannelMember channelMember = testEntityManager.find(ChannelMember.class, 1l);

        assertThat(channelMember.getUserName(), is("choi"));
        assertThat(channelMember.getAuthority(), is("M"));
        assertThat(channelMember.getId(), is(1l));
    }

    @Test
    @Transactional
    public void CascadeOneToManyTest() {

        Channel channel = Channel.builder().channelName("C#")
                                           .userName("choi")
                                           .build();

        ChannelMember channelMember = ChannelMember.builder().channel(channel)
                                                             .userName("KIM")
                                                             .authority("W")
                                                             .build();

        ChannelMember channelMember2 = ChannelMember.builder().channel(channel)
                                                              .userName("LEE")
                                                              .authority("R")
                                                              .build();

        channel.getChannelMembers().add(channelMember);
        channel.getChannelMembers().add(channelMember2);

        Channel getChannel = testEntityManager.persist(channel);

        ChannelMember findMember = testEntityManager.find(ChannelMember.class, 2l);
        ChannelMember findMember2 = testEntityManager.find(ChannelMember.class, 3l);

        Iterator<ChannelMember> iter = getChannel.getChannelMembers().iterator();

        assertThat(iter.next().getUserName(), is(findMember.getUserName()));
        assertThat(iter.next().getUserName(), is(findMember2.getUserName()));
    }

    @Test
    @Transactional
    public void CascadeManyToOneTest() {

        Channel channel = Channel.builder().channelName("python")
                                            .userName("choi")
                                            .build();

        ChannelMember channelMember = ChannelMember.builder().channel(channel)
                                                             .userName("KIM")
                                                             .authority("W")
                                                             .build();

        ChannelMember channelMember2 = ChannelMember.builder().channel(channel)
                                                              .userName("LEE")
                                                              .authority("R")
                                                              .build();

        ChannelMember member = testEntityManager.persist(channelMember);
        ChannelMember member2 = testEntityManager.persist(channelMember2);

        Channel channel2 = testEntityManager.find(Channel.class, 2l);

        assertThat(member.getChannel(), is(channel2));
        assertThat(member2.getChannel(), is(channel2));
    }
}
