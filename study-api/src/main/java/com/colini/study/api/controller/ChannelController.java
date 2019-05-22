package com.colini.study.api.controller;

import com.colini.study.api.service.ChannelService;
import com.colini.study.core.channel.domain.model.Channel;
import com.colini.study.core.channel.domain.model.ChannelMember;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @PostMapping(path = "/channel/create")
    public Channel createChannel(@RequestBody @Valid Channel channel) {
        return channelService.createChannel(channel);
    }

    @GetMapping(path = "/channel/{channelId}")
    public Channel findMemberById(@PathVariable Long channelId)  {
        return channelService.findChannelById(channelId);
    }

    @GetMapping(path = "/channel/{channelId}/members")
    public List<ChannelMember> findChannelMembersById(@PathVariable Long channelId) {
        return channelService.findChannelMembers(channelId);
    }

    @PatchMapping(path = "/channel/{channelId}")
    public Channel updateChannel(@PathVariable Long channelId, @RequestBody @Valid Channel channel) {
        return channelService.updateChannel(channelId, channel);
    }

    @DeleteMapping(path = "/channel/{channelId}")
    public Channel deleteChannel(@PathVariable Long channelId) {
        return channelService.deleteChannel(channelId);
    }

}
