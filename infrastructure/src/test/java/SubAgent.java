import io.agentscope.harness.agent.HarnessAgent;
import io.agentscope.harness.agent.subagent.SubagentDeclaration;
import io.agentscope.harness.agent.subagent.WorkspaceMode;

import java.nio.file.Paths;
import java.util.List;

/**
 * @author liu.hua
 */
public class SubAgent {
    public static void main(String[] args) {
        String model = "openai:gpt-5.4-2026-03-05";
        HarnessAgent.builder()
                .name("orchestrator")
                .model(model)
                .workspace(Paths.get(".agentscope/workspace"))
                .subagent(SubagentDeclaration.builder()
                        .name("reviewer")
                        .description("代码审核专家")
                        .workspaceMode(WorkspaceMode.ISOLATED)
                        .model(model)
                        .steps(8)
                        .tools(List.of("read_file","grep_files"))
                        .build())
//                .subagent(SubagentDeclaration.builder()
//                        .name("remote-researcher")
//                        .description("远端调研子 agent")
//                        .url("xxx")
//                        .build())
                .build();
    }
}
