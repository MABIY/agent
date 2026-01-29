package com.example.agent.domain.service;

import com.example.agent.domain.model.ConversationMessage;

import java.util.List;

/**
 * 领域服务接口（在 Domain 层定义）
 *
 * 定义智能体的核心业务能力
 * 外层（infrastructure）将提供具体实现
 *
 * 注意：这是一个接口，具体实现在 infrastructure 模块
 */
public interface AgentService {

    /**
     * 处理用户输入并生成回复
     *
     * @param userInput 用户输入
     * @return 助手的回复
     */
    String chat(String userInput);

    /**
     * 获取对话历史
     *
     * @return 对话消息列表
     */
    List<ConversationMessage> getHistory();

    /**
     * 清空对话历史
     */
    void clearHistory();
}
