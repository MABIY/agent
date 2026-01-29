package com.example.agent.infrastructure.config;

import com.example.agent.application.service.ChatApplicationService;
import com.example.agent.domain.service.AgentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring 配置类
 *
 * 位于 Infrastructure 层
 * 职责：组装各层组件，实现依赖注入
 *
 * 注意：AgentScopeAgentService 已通过 @Component 注解自动注册为 Bean
 */
@Configuration
public class AgentScopeConfig {

    /**
     * ChatApplicationService Bean
     *
     * 注入 AgentService 实现（由 Spring 自动注入 AgentScopeAgentService）
     */
    @Bean
    public ChatApplicationService chatApplicationService(AgentService agentService) {
        return new ChatApplicationService(agentService);
    }
}
