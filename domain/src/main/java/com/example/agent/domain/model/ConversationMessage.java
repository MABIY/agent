package com.example.agent.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 领域模型：对话消息
 *
 * 这是纯业务实体，不依赖任何外部框架
 * 可以在没有任何框架的情况下独立运行和测试
 */
public class ConversationMessage {

    private final String content;
    private final MessageRole role;
    private final LocalDateTime timestamp;

    public ConversationMessage(String content, MessageRole role) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("消息内容不能为空");
        }
        if (role == null) {
            throw new IllegalArgumentException("消息角色不能为空");
        }
        this.content = content;
        this.role = role;
        this.timestamp = LocalDateTime.now();
    }

    public String getContent() {
        return content;
    }

    public MessageRole getRole() {
        return role;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public boolean isFromUser() {
        return role == MessageRole.USER;
    }

    public boolean isFromAssistant() {
        return role == MessageRole.ASSISTANT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversationMessage that = (ConversationMessage) o;
        return Objects.equals(content, that.content) &&
                role == that.role &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, role, timestamp);
    }

    @Override
    public String toString() {
        return "ConversationMessage{" +
                "content='" + content + '\'' +
                ", role=" + role +
                ", timestamp=" + timestamp +
                '}';
    }

    /**
     * 消息角色枚举
     * 纯领域概念，不依赖外部
     */
    public enum MessageRole {
        USER,
        ASSISTANT
    }
}
