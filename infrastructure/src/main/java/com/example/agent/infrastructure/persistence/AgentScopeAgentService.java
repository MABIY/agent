package com.example.agent.infrastructure.persistence;

import com.example.agent.domain.model.ConversationMessage;
import com.example.agent.domain.service.AgentService;
import io.agentscope.core.ReActAgent;
import io.agentscope.core.message.Msg;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * AgentService 的 AgentScope 实现
 * 位于 Infrastructure 层，实现 Domain 层定义的接口
 * 职责：将 AgentScope SDK 适配到领域服务接口
 * 这是一个适配器（Adapter Pattern）
 */
@Component
public class AgentScopeAgentService implements AgentService {

    private final ReActAgent agent;
    private final List<Msg> conversationHistory = new ArrayList<>();

    /**
     * 构造函数，创建 ReActAgent
     */
    public AgentScopeAgentService() {
        this.agent = ReActAgent.builder()
                .name("Assistant")
                .sysPrompt("You are a helpful assistant.")
                .model("openai:gpt-5.4-2026-03-05")
                .build();
    }

    @Override
    public String chat(String userInput) {
        // 1. 创建用户消息
        Msg userMsg = Msg.builder()
                .textContent(userInput)
                .build();

        // 2. 调用 Agent 生成回复
        Msg response = agent.call(userMsg).block();

        return response != null ? response.getTextContent() : "No response";
    }

    @Override
    public List<ConversationMessage> getHistory() {
        return conversationHistory.stream()
                .map(this::toConversationMessage)
                .toList();
    }

    @Override
    public void clearHistory() {
        conversationHistory.clear();
    }

    /**
     * 将 AgentScope Msg 转换为领域模型
     * 这是适配器模式的体现
     */
    private ConversationMessage toConversationMessage(Msg msg) {
        // 根据 Msg 的类型确定角色
        // 简化处理：所有消息都作为 USER 角色
        // 实际应用中可以根据 Msg 的其他属性来判断
        return new ConversationMessage(msg.getTextContent(), ConversationMessage.MessageRole.USER);
    }
}
