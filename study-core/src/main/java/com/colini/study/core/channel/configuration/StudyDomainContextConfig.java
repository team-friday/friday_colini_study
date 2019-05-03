package com.colini.study.core.channel.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(value = "com.colini.study.core.chat.domain.model")
@EnableJpaRepositories(value = "com.colini.study.core.channel.domain.repository")
public class StudyDomainContextConfig {
}
