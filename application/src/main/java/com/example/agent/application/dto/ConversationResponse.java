package com.example.agent.application.dto;

import com.example.agent.domain.model.ConversationMessage;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 应用层 DTO：对话历史响应
 *
 * 负责将领域模型转换为 DTO
 */
public record ConversationResponse(
        String content,
        String role,
        LocalDateTime timestamp
) {

    public static ConversationResponse from(ConversationMessage message) {
        return new ConversationResponse(
                message.getContent(),
                message.getRole().name(),
                message.getTimestamp()
        );
    }

    public static List<ConversationResponse> fromList(List<ConversationMessage> messages) {
        return messages.stream()
                .map(ConversationResponse::from)
                .toList();
    }
}
