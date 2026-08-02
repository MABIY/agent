import io.agentscope.core.agent.Agent;
import io.agentscope.core.agent.RuntimeContext;
import io.agentscope.core.event.AgentEvent;
import io.agentscope.core.event.ToolResultTextDeltaEvent;
import io.agentscope.core.message.Msg;
import io.agentscope.core.message.ToolUseBlock;
import io.agentscope.core.message.UserMessage;
import io.agentscope.core.middleware.*;
import io.agentscope.harness.agent.HarnessAgent;
import io.agentscope.harness.agent.memory.MemoryConfig;
import io.agentscope.harness.agent.memory.compaction.CompactionConfig;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author liu.hua
 */
public class FirstAgent {
    public static void main(String[] args) {
//        Path absolutePath = Paths.get(".agentscope/workspace").toAbsolutePath();
        // Set HTTP and HTTPS proxy settings
        EnvConfUtil.setProxy();

        HarnessAgent agent = HarnessAgent.builder()
                .name("note-taker")
                .sysPrompt("你是一个帮助用户做笔记的助手。")
                .model("openai:gpt-5.4-2026-03-05")
                .workspace(Paths.get(".agentscope/workspace"))
//                .disableMemoryHooks()
                .middlewares(List.of(
                        new MiddlewareBase() {
                            @Override
                            public Flux<AgentEvent> onAgent(Agent agent, RuntimeContext ctx, AgentInput input, Function<AgentInput, Flux<AgentEvent>> next) {
                                String name = agent.getName();
                                System.out.println("[agent] start for " + name);
                                Flux<AgentEvent> agentEventFlux = MiddlewareBase.super.onAgent(agent, ctx, input, next);
                                return agentEventFlux.doOnComplete(() -> {
                                    System.out.println("[agent] end for " + name);
                                });
                            }

                            @Override
                            public Flux<AgentEvent> onReasoning(Agent agent, RuntimeContext ctx, ReasoningInput input, Function<ReasoningInput, Flux<AgentEvent>> next) {
                                String name = agent.getName();
                                System.out.println("[reasoning] start for " + name);
                                Flux<AgentEvent> agentEventFlux = MiddlewareBase.super.onReasoning(agent, ctx, input, next);
                                return agentEventFlux.doOnComplete(() -> {
                                    System.out.println("[reasoning] end for " + name);
                                });
                            }

                            @Override
                            public Flux<AgentEvent> onActing(Agent agent, RuntimeContext ctx, ActingInput input, Function<ActingInput, Flux<AgentEvent>> next) {
                                String toolNames =
                                        input.toolCalls().stream()
                                                .map(ToolUseBlock::getName)
                                                .collect(Collectors.joining(", "));
                                System.out.println("\n[MIDDLEWARE] onActing START — tools: " + toolNames);

                                return next.apply(input)
                                        .doOnNext(
                                                event -> {
                                                    if (event instanceof ToolResultTextDeltaEvent delta) {
                                                        System.out.println(
                                                                "[MIDDLEWARE] tool progress chunk: "
                                                                        + delta.getDelta());
                                                    }
                                                })
                                        .doOnComplete(
                                                () -> System.out.println("[MIDDLEWARE] onActing END — " + toolNames));
                            }

                            @Override
                            public Flux<AgentEvent> onModelCall(Agent agent, RuntimeContext ctx, ModelCallInput input, Function<ModelCallInput, Flux<AgentEvent>> next) {
                                System.out.println("[mode call start] for " + agent.getName());
                                Flux<AgentEvent> agentEventFlux = MiddlewareBase.super.onModelCall(agent, ctx, input, next);
                                return agentEventFlux.doOnComplete(() -> {
                                    System.out.println("[mode call done] for " + agent.getName());
                                });
                            }

                            @Override
                            public Mono<String> onSystemPrompt(Agent agent, RuntimeContext ctx, String currentPrompt) {
                                Mono<String> stringMono = MiddlewareBase.super.onSystemPrompt(agent, ctx, currentPrompt);
                                return stringMono;
                            }
                        }
                ))
                .disableMemoryTools()
//                .disableSubagents()  //diable system prompt section of subagent prompt
//                .abstractFilesystem(new LocalFilesystem(absolutePath))
                .compaction(CompactionConfig.builder()
                        .flushBeforeCompact(false) // memory flush before compact disabled
                        .triggerMessages(50)
                        .keepMessages(20)
                        .build())
                .build();
        RuntimeContext ctx = RuntimeContext.builder()
                .sessionId("demo-session")
                .userId("alice")
                .build();
        Msg block = agent.call(new UserMessage("我叫天宇，今天准备一个关于React的技术分享。"), ctx).block();

//        Msg block1 = agent.call(new UserMessage("我叫什么？我今天要干什么?"), ctx).block();


        //        agent.streamEvents(new UserMessage("帮我把今天的关键点列三条。"))
        //                .doOnNext(event -> {
        //                    if (event.getType() == AgentEventType.TEXT_BLOCK_DELTA) {
        //                        System.out.print(((TextBlockDeltaEvent) event).getDelta());
        //                    } else if (event.getType() == AgentEventType.TOOL_CALL_START) {
        //                        System.out.println("\n[tool]" + ((ToolCallStartEvent) event).getToolCallName());
        //                    }
        //                }).blockLast();
    }

//    public static void main(String[] args) {
//        HarnessAgent agent = HarnessAgent.builder()
//                .name("note-taker")
//                .sysPrompt("你是一个帮助用户做笔记的助手。")
//                .model("openai:gpt-5.4-2026-03-05")
//                .workspace(Paths.get(".agentscope/worksapce"))
//                .disableCompaction()
//                .build();
//
//        agent(new UserMessage("你好"), RuntimeContext.builder()
//                .sessionId("x2").userId("liu").build()).block();
//    }
}
