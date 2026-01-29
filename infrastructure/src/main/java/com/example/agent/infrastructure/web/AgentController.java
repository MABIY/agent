package com.example.agent.infrastructure.web;

import com.example.agent.application.dto.ChatRequest;
import com.example.agent.application.dto.ChatResponse;
import com.example.agent.application.dto.ConversationResponse;
import com.example.agent.application.service.ChatApplicationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST 控制器
 *
 * 位于 Infrastructure 层（Web）
 * 职责：
 * 1. 处理 HTTP 请求/响应
 * 2. 调用应用服务
 * 3. 不包含业务逻辑
 */
@RestController
@RequestMapping("/api/agent")
public class AgentController {

    private final ChatApplicationService chatApplicationService;

    /**
     * 构造函数注入应用服务
     */
    public AgentController(ChatApplicationService chatApplicationService) {
        this.chatApplicationService = chatApplicationService;
    }

    /**
     * 聊天端点
     */
    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) {
        return chatApplicationService.handleChat(request);
    }

    /**
     * 获取对话历史端点
     */
    @GetMapping("/history")
    public List<ConversationResponse> getHistory() {
        return chatApplicationService.getConversationHistory();
    }

    /**
     * 清空对话历史端点
     */
    @DeleteMapping("/history")
    public void clearHistory() {
        chatApplicationService.clearConversationHistory();
    }
}
