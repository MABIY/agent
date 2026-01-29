package com.example.agent.application.dto;

/**
 * 应用层 DTO：聊天响应
 *
 * 用于应用层和 Web 层之间的数据传输
 */
public record ChatResponse(String response) {

    public ChatResponse {
        if (response == null) {
            throw new IllegalArgumentException("响应内容不能为空");
        }
    }
}
