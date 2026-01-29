# AgentScope + Spring Boot 智能体项目（洋葱架构 + 多模块）

基于 Gradle、Spring Boot 3 和 AgentScope Java 的智能体应用示例，采用**洋葱架构（Onion Architecture）** + **Gradle 多模块**设计。

---

## 架构图

```
                    ┌─────────────────────────────────────────┐
                    │         Infrastructure Module (外层)      │
                    │  ┌──────────┬──────────┬──────────────┐ │
                    │  │   Web    │   Config │ Persistence  │ │
                    │  │Controller│          │   Adapter    │ │
                    │  └──────────┴──────────┴──────────────┘ │
                    │                                          │
                    │  - 依赖: domain, application             │
                    │  - Spring 依赖注入                        │
                    │  - AgentScope SDK                        │
                    └─────────────────────────────────────────┘
                                      ↕ 依赖向内
                    ┌─────────────────────────────────────────┐
                    │       Application Module (中间层)         │
                    │  ┌────────────────────────────────────┐ │
                    │  │  Application Service                │ │
                    │  │  (用例编排、DTO 转换)                │ │
                    │  │  ┌──────────┬──────────────────┐  │ │
                    │  │  │   DTO    │ App Service      │  │ │
                    │  │  └──────────┴──────────────────┘  │ │
                    │  └────────────────────────────────────┘ │
                    │                                          │
                    │  - 依赖: domain                          │
                    │  - 纯 Java，无框架依赖                    │
                    └─────────────────────────────────────────┘
                                      ↕ 依赖向内
                    ┌─────────────────────────────────────────┐
                    │          Domain Module (核心层)          │
                    │  ┌──────────┬──────────┬──────────────┐ │
                    │  │  Model   │Repository│Domain Service│ │
                    │  │ (实体/值对象)│  (接口)  │   (接口)     │ │
                    │  └──────────┴──────────┴──────────────┘ │
                    │                                          │
                    │  - 零依赖                                │
                    │  - 纯业务逻辑                            │
                    └─────────────────────────────────────────┘


                    ┌─────────────────────────────────────────┐
                    │            Boot Module (启动层)           │
                    │                                          │
                    │  - 依赖: domain, application, infra      │
                    │  - Spring Boot 应用入口                   │
                    │  - 组装所有模块                           │
                    └─────────────────────────────────────────┘
```

---

## 模块依赖关系

```
┌─────────────────────────────────────────────────────────────┐
│                    Gradle 模块依赖图                          │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│    boot                                                    │
│      │                                                      │
│      ├──→ infrastructure ───→ application ───→ domain     │
│      │                                                     │
│      └──→ application ───→ domain                         │
│                                                             │
│    编译时强制执行依赖规则                                    │
│    - domain 无法引用 infrastructure 的类                    │
│    - application 无法引用 infrastructure 的类               │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## 项目结构

```
agent/
├── settings.gradle                  # Gradle 多模块配置
├── build.gradle                      # 根项目配置
│
├── domain/                           # 【核心模块】零依赖
│   ├── build.gradle                  # 零业务依赖
│   └── src/main/java/
│       └── com/example/agent/domain/
│           ├── model/
│           │   ├── Conversation.java           # 对话聚合根
│           │   └── ConversationMessage.java    # 消息实体
│           ├── repository/
│           │   └── ConversationRepository.java # 仓储接口
│           └── service/
│               └── AgentService.java          # 智能体服务接口
│
├── application/                      # 【应用模块】用例编排
│   ├── build.gradle                  # 依赖: domain
│   └── src/main/java/
│       └── com/example/agent/application/
│           ├── service/
│           │   └── ChatApplicationService.java
│           └── dto/
│               ├── ChatRequest.java
│               ├── ChatResponse.java
│               └── ConversationResponse.java
│
├── infrastructure/                   # 【外层模块】技术实现
│   ├── build.gradle                  # 依赖: domain, application
│   └── src/main/java/
│       └── com/example/agent/infrastructure/
│           ├── web/
│           │   └── AgentController.java        # REST 控制器
│           ├── persistence/
│           │   ├── AgentScopeAgentService.java # AgentScope 实现
│           │   └── InMemoryConversationRepository.java
│           └── config/
│               └── AgentScopeConfig.java       # Spring 配置
│
└── boot/                             # 【启动模块】应用入口
    ├── build.gradle                  # 依赖: 所有模块
    └── src/main/
        ├── java/com/example/agent/
        │   └── AgentApplication.java         # Spring Boot 入口
        └── resources/
            └── application.yml                # 应用配置
```

---

## 各模块详解

### 1. domain 模块（核心层）

**职责**：定义业务模型和业务规则

| 特性 | 说明 |
|------|------|
| 依赖 | **零依赖** |
| 是否依赖 Spring | ❌ 否 |
| 是否依赖 AgentScope | ❌ 否 |
| 可独立测试 | ✅ 是 |

**build.gradle**：
```gradle
dependencies {
    // 零业务依赖
}
```

**包含内容**：
- `Conversation.java` - 对话聚合根
- `ConversationMessage.java` - 消息实体
- `ConversationRepository.java` - 仓储接口
- `AgentService.java` - 智能体服务接口

---

### 2. application 模块（应用层）

**职责**：编排业务用例

| 特性 | 说明 |
|------|------|
| 依赖 | domain |
| 是否依赖 Spring | ❌ 否（纯 Java） |
| 是否依赖 AgentScope | ❌ 否 |

**build.gradle**：
```gradle
dependencies {
    implementation project(':domain')
}
```

**包含内容**：
- `ChatApplicationService.java` - 聊天用例编排
- `ChatRequest.java` - 请求 DTO
- `ChatResponse.java` - 响应 DTO
- `ConversationResponse.java` - 历史记录 DTO

---

### 3. infrastructure 模块（外层）

**职责**：提供技术实现

| 特性 | 说明 |
|------|------|
| 依赖 | domain, application |
| 是否依赖 Spring | ✅ 是（依赖注入） |
| 是否依赖 AgentScope | ✅ 是（外部 SDK） |

**build.gradle**：
```gradle
dependencies {
    implementation project(':domain')
    implementation project(':application')
    implementation 'org.springframework.boot:spring-boot-starter-web:3.2.0'
    implementation 'com.alibaba:agentscope-java:0.0.1'
}
```

**包含内容**：
- `AgentController.java` - REST 控制器
- `AgentScopeAgentService.java` - AgentScope 适配器
- `InMemoryConversationRepository.java` - 仓储实现
- `AgentScopeConfig.java` - Spring 配置

---

### 4. boot 模块（启动层）

**职责**：启动 Spring Boot 应用

| 特性 | 说明 |
|------|------|
| 依赖 | domain, application, infrastructure |
| 是否依赖 Spring Boot | ✅ 是 |

**build.gradle**：
```gradle
plugins {
    id 'org.springframework.boot' version '3.2.0'
}

dependencies {
    implementation project(':domain')
    implementation project(':application')
    implementation project(':infrastructure')
    implementation 'org.springframework.boot:spring-boot-starter'
}
```

**包含内容**：
- `AgentApplication.java` - Spring Boot 入口
- `application.yml` - 应用配置

---

## 架构优势

### 1. 编译时依赖检查

```
❌ 错误示例（编译不通过）：

// domain 模块
import org.springframework.stereotype.Component;  // ❌ 编译错误
import com.alibaba.agentscope.core.AgentScope;    // ❌ 编译错误

✅ 正确示例：

// domain 模块
public interface AgentService { }  // ✅ 纯 Java 接口

// infrastructure 模块
import org.springframework.stereotype.Component;  // ✅ 可以
import com.alibaba.agentscope.core.AgentScope;    // ✅ 可以
@Component
public class AgentScopeAgentService implements AgentService { }
```

### 2. 可测试性

```java
// domain 模块测试：无需任何框架
@Test
void testConversation() {
    Conversation conversation = new Conversation("123");
    ConversationMessage message = new ConversationMessage(
        "Hello", MessageRole.USER
    );
    conversation.addMessage(message);
    assertEquals(1, conversation.getMessageCount());
}
```

### 3. 可替换性

```
替换 AgentScope 为其他 AI 框架：

1. 创建新的实现类
   OpenAIAgentService implements AgentService

2. 修改配置
   @Bean
   public AgentService agentService() {
       return new OpenAIAgentService();
   }

3. domain 和 application 模块无需任何修改 ✅
```

---

## 环境要求

- JDK 17+
- Gradle 8.0+

---

## 快速开始

### 1. 设置环境变量

```bash
export AGENTSCOPE_API_KEY="your-api-key-here"
```

### 2. 构建项目

```bash
./gradlew build
```

### 3. 运行应用

```bash
./gradlew :boot:bootRun
```

应用将在 `http://localhost:8080` 启动。

---

## API 接口

### 发送聊天消息

```bash
curl -X POST http://localhost:8080/api/agent/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "你好，请介绍一下你自己"}'
```

### 获取对话历史

```bash
curl http://localhost:8080/api/agent/history
```

### 清空对话历史

```bash
curl -X DELETE http://localhost:8080/api/agent/history
```

---

## 架构对比

| 方面 | 单模块 + 包分层 | 多模块架构 |
|------|----------------|-----------|
| 依赖规则 | 运行时检查 | **编译时检查** ✅ |
| 代码隔离 | 可能误用 | **物理隔离** ✅ |
| domain 零依赖 | 理论上 | **强制执行** ✅ |
| 可测试性 | 需要整个项目 | **独立测试模块** ✅ |
| 构建速度 | 全量构建 | **按需构建** ✅ |
| 模块复用 | 困难 | **轻松复用** ✅ |

---

## 主要依赖

| 模块 | Spring Boot | AgentScope | 其他 |
|------|-------------|------------|------|
| domain | ❌ | ❌ | - |
| application | ❌ | ❌ | - |
| infrastructure | ✅ | ✅ | Spring Web |
| boot | ✅ | - | Spring Boot Starter |
