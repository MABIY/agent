package com.example.agent.application.dto;

/**
 * 应用层 DTO：聊天请求
 *
 * 用于 Web 层和应用层之间的数据传输
 * 不包含业务逻辑，只做数据校验
 */
public record ChatRequest(String message) {

    public ChatRequest {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("消息内容不能为空");
        }
    }
}
