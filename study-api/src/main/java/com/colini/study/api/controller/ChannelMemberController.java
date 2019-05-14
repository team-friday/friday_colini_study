package com.colini.study.api.controller;

import com.colini.study.api.service.ChannelMemberService;
import com.colini.study.core.channel.domain.model.ChannelMember;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ChannelMemberController {

    private final ChannelMemberService memberService;

    @PostMapping(path = "/channel/member/add")
    public ChannelMember addMember(@RequestBody @Valid ChannelMember channelMember) {
        System.out.println(channelMember.toString());
        return memberService.addMember(channelMember);
    }

    @GetMapping(path = "/channel/member/{memberId}")
    public ChannelMember findMemberById(@PathVariable Long memberId) {
        return memberService.findChannelMemberById(memberId);
    }

    @PatchMapping(path = "/channel/member/{memberId}")
    public ChannelMember updateMember(@PathVariable Long memberId, @RequestBody @Valid ChannelMember member) {
        return memberService.updateMember(memberId, member);
    }

    @DeleteMapping(path = "/channel/member/{memberId}")
    public ChannelMember deleteMember(@PathVariable Long memberId) {
        return memberService.deleteMember(memberId);
    }
}
