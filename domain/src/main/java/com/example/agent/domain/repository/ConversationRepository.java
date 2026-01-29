package com.example.agent.domain.repository;

import com.example.agent.domain.model.Conversation;

import java.util.Optional;

/**
 * 仓储接口（在 Domain 层定义）
 *
 * 核心层定义接口，外层负责实现
 * 这符合依赖倒置原则（DIP）：高层模块不依赖低层模块，都依赖抽象
 *
 * 注意：这是一个接口，具体实现在 infrastructure 模块
 */
public interface ConversationRepository {

    /**
     * 保存对话
     */
    void save(Conversation conversation);

    /**
     * 根据 ID 查找对话
     */
    Optional<Conversation> findById(String id);

    /**
     * 删除对话
     */
    void delete(String id);

    /**
     * 检查对话是否存在
     */
    boolean exists(String id);
}
