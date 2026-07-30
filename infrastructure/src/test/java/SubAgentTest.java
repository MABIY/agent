import io.agentscope.core.agent.Agent;
import io.agentscope.core.agent.RuntimeContext;
import io.agentscope.core.event.AgentEvent;
import io.agentscope.core.message.Msg;
import io.agentscope.core.message.UserMessage;
import io.agentscope.core.middleware.*;
import io.agentscope.core.state.JsonFileAgentStateStore;
import io.agentscope.harness.agent.HarnessAgent;
import io.agentscope.harness.agent.subagent.SubagentDeclaration;
import io.agentscope.harness.agent.subagent.WorkspaceMode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author liu.hua
 */
public class SubAgentTest {
    @TempDir
    Path workspace;

    @AfterEach
    void cleanupTempDir() {
        if (workspace != null && Files.exists(workspace)) {
            try (var files = Files.walk(workspace)) {
                files.sorted(Comparator.reverseOrder())
                        .filter(p -> !p.equals(workspace))
                        .forEach(
                                p -> {
                                    try {
                                        Files.deleteIfExists(p);
                                    } catch (IOException ignored) {
                                    }
                                });
            } catch (IOException ignored) {
            }
        }
    }

    @Test
    void subAgentTest() throws InterruptedException {
        String model = "openai:gpt-5.4-2026-03-05";
        HarnessAgent testAgent = HarnessAgent.builder()
                .name("orchestrator")
                .model(model)
                .workspace(workspace.resolve(".agentscope/workspace"))
                .stateStore(new JsonFileAgentStateStore(workspace.resolve(".agentscope/state")))
                .sysPrompt("你是一个助手小陈。")
                .disableWorkspaceContext()
                .disableSubagents()
                .disableFilesystemTools()
                .disableShellTool()
                .disableMemoryTools()
                .disableCompaction()
                .disableDefaultWorkspaceSkills()
                .middleware(new MiddlewareBase() {
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
                        String name = agent.getName();
                        System.out.println("[act call start] for " + name);
                        Flux<AgentEvent> agentEventFlux = MiddlewareBase.super.onActing(agent, ctx, input, next);
                        return agentEventFlux.doOnComplete(() -> {
                            System.out.println("[act call done] for " + name);
                        });
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
                })
                .subagent(SubagentDeclaration.builder()
                        .name("reviewer")
                        .description("代码审核专家")
                        .workspace(workspace.resolve("./defs/reviewer"))
                        .workspaceMode(WorkspaceMode.ISOLATED)
                        .model(model)
                        .steps(8)
                        .tools(List.of("read_file", "grep_files"))
                        .build())
//                .subagent(SubagentDeclaration.builder()
//                        .name("remote-researcher")
//                        .description("远端调研子 agent")
//                        .url("xxx")
//                        .build())
                .build();
        AgentEvent lastBlock = testAgent.streamEvents(new UserMessage("启动代码审核专家帮我审核下代码"), RuntimeContext.builder().userId("lh").sessionId("1-session-1").build()).doOnNext(agentEvent -> {

            switch (agentEvent.getType()) {
                case AGENT_START -> {
                    System.out.print("");
                }
                case AGENT_END -> {
                    System.out.print("");
                }
                case AGENT_RESULT -> {
                    System.out.print("");
                }
                case MODEL_CALL_START -> {
                    System.out.print("");
                }
                case MODEL_CALL_END -> {
                    System.out.print("");
                }
                case TEXT_BLOCK_START -> {
                    System.out.print("");
                }
                case TEXT_BLOCK_DELTA -> {
                    System.out.print("");
                }
                case TEXT_BLOCK_END -> {
                    System.out.print("");
                }
                case THINKING_BLOCK_START -> {
                    System.out.print("");
                }
                case THINKING_BLOCK_DELTA -> {
                    System.out.print("");
                }
                case THINKING_BLOCK_END -> {
                    System.out.print("");
                }
                case DATA_BLOCK_START -> {
                    System.out.print("");
                }
                case DATA_BLOCK_DELTA -> {
                    System.out.print("");
                }
                case DATA_BLOCK_END -> {
                    System.out.print("");
                }
                case TOOL_CALL_START -> {
                    System.out.print("");
                }
                case TOOL_CALL_DELTA -> {
                    System.out.print("");
                }
                case TOOL_CALL_END -> {
                    System.out.print("");
                }
                case TOOL_RESULT_START -> {
                    System.out.print("");
                }
                case TOOL_RESULT_TEXT_DELTA -> {
                    System.out.print("");
                }
                case TOOL_RESULT_DATA_DELTA -> {
                    System.out.print("");
                }
                case TOOL_RESULT_END -> {
                    System.out.print("");
                }
                case EXCEED_MAX_ITERS -> {
                    System.out.print("");
                }
                case REQUIRE_USER_CONFIRM -> {
                    System.out.print("");
                }
                case REQUIRE_EXTERNAL_EXECUTION -> {
                    System.out.print("");
                }
                case USER_CONFIRM_RESULT -> {
                    System.out.print("");
                }
                case EXTERNAL_EXECUTION_RESULT -> {
                    System.out.print("");
                }
                case REQUEST_STOP -> {
                    System.out.print("");
                }
                case SUBAGENT_EXPOSED -> {
                    System.out.print("");
                }
                case HINT_BLOCK -> {
                    System.out.print("");
                }
                case ALL_TOOLS_DENIED -> {
                    System.out.print("");
                }
                case CUSTOM -> {
                    System.out.print("");
                }
            }
        }).blockLast();

        TimeUnit.SECONDS.sleep(20);
        Msg block1 = testAgent.call(new UserMessage("审核代码的结果如何"), RuntimeContext.builder().userId("lh").sessionId("1-session-1").build()).block();
    }
}
