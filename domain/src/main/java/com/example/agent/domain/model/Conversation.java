package com.example.agent.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 领域模型：对话（聚合根）
 *
 * 管理对话的生命周期和业务规则
 * 不依赖任何外部框架
 */
public class Conversation {

    private final String id;
    private final List<ConversationMessage> messages;
    private final LocalDateTime createdAt;

    public Conversation(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("对话 ID 不能为空");
        }
        this.id = id;
        this.messages = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
    }

    /**
     * 添加消息到对话
     * 业务规则：消息按时间顺序添加
     */
    public void addMessage(ConversationMessage message) {
        if (message == null) {
            throw new IllegalArgumentException("消息不能为空");
        }
        messages.add(message);
    }

    /**
     * 获取所有消息（不可变视图，保护封装）
     */
    public List<ConversationMessage> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    /**
     * 获取消息数量
     */
    public int getMessageCount() {
        return messages.size();
    }

    /**
     * 清空对话历史
     */
    public void clear() {
        messages.clear();
    }

    /**
     * 检查对话是否为空
     */
    public boolean isEmpty() {
        return messages.isEmpty();
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * 获取最后一条消息
     */
    public ConversationMessage getLastMessage() {
        if (messages.isEmpty()) {
            return null;
        }
        return messages.get(messages.size() - 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conversation that = (Conversation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "id='" + id + '\'' +
                ", messageCount=" + messages.size() +
                ", createdAt=" + createdAt +
                '}';
    }
}
