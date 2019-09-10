package com.colini.study.api.service;

import com.colini.study.api.exception.EntityNotFoundException;
import com.colini.study.core.channel.domain.model.ChannelMember;
import com.colini.study.core.channel.domain.repository.ChannelMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelMemberService {

    @Autowired
    private final ChannelMemberRepository memberRepository;

    public ChannelMember addMember(ChannelMember member){
        return memberRepository.save(member);
    }

    public ChannelMember findChannelMemberById(long id) throws RuntimeException{
        return memberRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public ChannelMember updateMember(Long id, ChannelMember member) {
        member.setId(id);
        return memberRepository.save(member);
    }

    public ChannelMember deleteMember(Long id) throws RuntimeException {
        ChannelMember member = memberRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        memberRepository.delete(member);
        return member;
    }
}
