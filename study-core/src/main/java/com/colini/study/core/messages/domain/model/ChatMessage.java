package com.colini.study.core.messages.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private long channelId;
    private String userName;
    private String messageId;
    private String message;
    private String createdAt;
    private String expired;
}
