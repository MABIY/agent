import io.agentscope.core.agent.RuntimeContext;
import io.agentscope.core.message.UserMessage;
import io.agentscope.harness.agent.HarnessAgent;

import java.nio.file.Paths;

/**
 * @author liu.hua
 */
public class FirstAgent {
//    public static void main(String[] args) {
//        HarnessAgent agent = HarnessAgent.builder()
//                .name("note-taker")
//                .sysPrompt("你是一个帮助用户做笔记的助手。")
//                .model("openai:gpt-5.4-2026-03-05")
//                .workspace(Paths.get(".agentscope/workspace"))
//                .compaction(CompactionConfig.builder()
//                        .triggerMessages(30)
//                        .keepMessages(10)
//                        .build())
//                .build();
//        RuntimeContext ctx = RuntimeContext.builder()
//                .sessionId("demo-session")
//                .userId("alice")
//                .build();
//        Msg block = agent.call(new UserMessage("我叫天宇，今天准备一个关于React的技术分享。"), ctx).block();
//
//        Msg block1 = agent.call(new UserMessage("我叫什么？我今天要干什么?"), ctx).block();
//

    ////        agent.streamEvents(new UserMessage("帮我把今天的关键点列三条。"))
    ////                .doOnNext(event -> {
    ////                    if (event.getType() == AgentEventType.TEXT_BLOCK_DELTA) {
    ////                        System.out.print(((TextBlockDeltaEvent) event).getDelta());
    ////                    } else if (event.getType() == AgentEventType.TOOL_CALL_START) {
    ////                        System.out.println("\n[tool]" + ((ToolCallStartEvent) event).getToolCallName());
    ////                    }
    ////                }).blockLast();
//    }
    public static void main(String[] args) {
        HarnessAgent agent = HarnessAgent.builder()
                .name("note-taker")
                .sysPrompt("你是一个帮助用户做笔记的助手。")
                .model("openai:gpt-5.4-2026-03-05")
                .workspace(Paths.get(".agentscope/worksapce"))
                .disableCompaction()
                .build();

        agent.call(new UserMessage("你好"), RuntimeContext.builder()
                .sessionId("x2").userId("liu").build()).block();
    }
}
