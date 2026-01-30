package com.example.agent.infrastructure.web;

import com.example.agent.application.dto.ChatRequest;
import com.example.agent.application.dto.ChatResponse;
import com.example.agent.application.dto.ConversationResponse;
import com.example.agent.application.service.ChatApplicationService;
import io.agentscope.core.ReActAgent;
import io.agentscope.core.hook.*;
import io.agentscope.core.message.Msg;
import io.agentscope.core.message.ToolResultBlock;
import io.agentscope.core.model.OpenAIChatModel;
import io.agentscope.core.skill.AgentSkill;
import io.agentscope.core.skill.SkillBox;
import io.agentscope.core.tool.*;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

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
    public void clearskillsHistory() {
        chatApplicationService.clearConversationHistory();
    }

    public static class Tool {

        @io.agentscope.core.tool.Tool(name = "agent-name",description = "agent-description")
        public void test(@ToolParam(name = "paramter")String value){

        }
    }
    @GetMapping("/test")
    public void test(){
        Toolkit toolkit = new Toolkit();
        SkillBox skillBox = new SkillBox(toolkit);

        AgentSkill dataSkill = AgentSkill.builder()
                .name("data_analysis")
                .description("Comprehensive data analysis capabilities")
                .skillContent("# Data Analysis\n...")
                .build();

        AgentTool loadDataTool = new AgentTool() {
            @Override
            public String getName() {
                return "agent-name";
            }

            @Override
            public String getDescription() {
                return "agent-description";
            }

            @Override
            public Map<String, Object> getParameters() {
                return Map.of("parameter", "String");
            }

            @Override
            public Mono<ToolResultBlock> callAsync(ToolCallParam param) {
                return null;
            }
        };

        skillBox.registration()
                .skill(dataSkill)
                .tool(new Tool())
                .apply();

        ReActAgent agent = ReActAgent.builder()
                .name("agent-skill")
                .model(OpenAIChatModel.builder()
                        .apiKey(System.getenv("OPENROUTER_API_KEY"))
                        .baseUrl("https://openrouter.ai/api/v1")
                        .modelName("z-ai/glm-4.7")
                        .build())
                .hook(new Hook() {
                    @Override
                    public <T extends HookEvent> Mono<T> onEvent(T event) {
                        switch (event){
                            case ActingEvent actingEvent -> {
                                switch (actingEvent){
                                    case ActingChunkEvent actingChunkEvent -> {
                                    }
                                    case PostActingEvent postActingEvent -> {
                                        System.out.println("test");
                                    }
                                    case PreActingEvent preActingEvent -> {
                                    }
                                }
                            }
                            case ErrorEvent errorEvent -> {
                            }
                            case PostCallEvent postCallEvent -> {
                            }
                            case PreCallEvent preCallEvent -> {
                            }
                            case ReasoningEvent reasoningEvent -> {
                            }
                        }
                        return Mono.just(event);
                    };

                })
                .toolkit(toolkit)
                .skillBox(skillBox)
                .build();
        Msg msg = Msg.builder().textContent("please analysis data").build();
        try {
            Msg response = agent.call(msg).block();
            System.out.println(response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
