import io.agentscope.core.agent.RuntimeContext;
import io.agentscope.core.message.Msg;
import io.agentscope.core.message.UserMessage;
import io.agentscope.harness.agent.HarnessAgent;
import io.agentscope.harness.agent.memory.MemoryConfig;
import io.agentscope.harness.agent.memory.compaction.CompactionConfig;

import java.nio.file.Paths;

/**
 * @author liu.hua
 */
public class FirstAgent {
    public static void main(String[] args) {
//        Path absolutePath = Paths.get(".agentscope/workspace").toAbsolutePath();
        HarnessAgent agent = HarnessAgent.builder()
                .name("note-taker")
                .sysPrompt("你是一个帮助用户做笔记的助手。")
                .model("openai:gpt-5.4-2026-03-05")
                .workspace(Paths.get(".agentscope/workspace"))
                .disableMemoryHooks()
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
