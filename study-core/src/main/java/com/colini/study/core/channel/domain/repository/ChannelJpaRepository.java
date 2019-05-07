package com.colini.study.core.channel.domain.repository;

import com.colini.study.core.channel.domain.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelJpaRepository extends JpaRepository<Channel, Long> {
}
