package com.example.agent.application.service;

import com.example.agent.application.dto.ChatRequest;
import com.example.agent.application.dto.ChatResponse;
import com.example.agent.application.dto.ConversationResponse;
import com.example.agent.domain.service.AgentService;

import java.util.List;

/**
 * 应用服务：编排领域服务
 *
 * 职责：
 * 1. 协调领域对象完成业务用例
 * 2. 处理事务边界
 * 3. DTO 与领域模型之间的转换
 *
 * 注意：这个类不依赖 Spring，是纯 Java 类
 * Spring 的依赖注入在 infrastructure 模块通过配置实现
 */
public class ChatApplicationService {

    private final AgentService agentService;

    /**
     * 构造函数注入
     *
     * @param agentService 领域服务（由 infrastructure 层提供实现）
     */
    public ChatApplicationService(AgentService agentService) {
        if (agentService == null) {
            throw new IllegalArgumentException("agentService 不能为空");
        }
        this.agentService = agentService;
    }

    /**
     * 处理聊天请求用例
     */
    public ChatResponse handleChat(ChatRequest request) {
        // 1. 调用领域服务
        String response = agentService.chat(request.message());

        // 2. 返回 DTO
        return new ChatResponse(response);
    }

    /**
     * 获取对话历史用例
     */
    public List<ConversationResponse> getConversationHistory() {
        var messages = agentService.getHistory();
        return ConversationResponse.fromList(messages);
    }

    /**
     * 清空对话历史用例
     */
    public void clearConversationHistory() {
        agentService.clearHistory();
    }
}
