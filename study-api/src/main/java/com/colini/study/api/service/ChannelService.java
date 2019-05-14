package com.colini.study.api.service;

import com.colini.study.api.exception.EntityNotFoundException;
import com.colini.study.core.channel.domain.model.Channel;
import com.colini.study.core.channel.domain.model.ChannelMember;
import com.colini.study.core.channel.domain.repository.ChannelJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelService {

    @Autowired
    private final ChannelJpaRepository channelJpaRepository;

    public Channel createChannel(Channel channel){
        return channelJpaRepository.save(channel);
    }

    public Channel findChannelById(long channelId) throws EntityNotFoundException{
        return channelJpaRepository.findById(channelId).orElseThrow(EntityNotFoundException::new);
    }

    public Channel updateChannel(Long channelId, Channel channel) {
        channel.setChannelId(channelId);
        return channelJpaRepository.save(channel);
    }

    public Channel deleteChannel(Long channelId) throws EntityNotFoundException {
        Channel channel = channelJpaRepository.findById(channelId).orElseThrow(EntityNotFoundException::new);
        channelJpaRepository.delete(channel);
        return channel;
    }

    public List<ChannelMember> findChannelMembers(Long channelId) throws EntityNotFoundException{
        Channel channel = channelJpaRepository.findById(channelId).orElseThrow(EntityNotFoundException::new);
        return channel.getChannelMembers();
    }

//    public List<Message> channelMessages(){
//
//    }
}
