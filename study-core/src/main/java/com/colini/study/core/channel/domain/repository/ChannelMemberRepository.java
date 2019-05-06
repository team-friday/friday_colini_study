package com.colini.study.core.channel.domain.repository;

import com.colini.study.core.channel.domain.model.ChannelMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelMemberRepository extends JpaRepository<ChannelMember, Long> {
}
