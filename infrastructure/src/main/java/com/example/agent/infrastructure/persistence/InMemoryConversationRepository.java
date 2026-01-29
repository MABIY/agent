package com.example.agent.infrastructure.persistence;

import com.example.agent.domain.model.Conversation;
import com.example.agent.domain.repository.ConversationRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对话仓储的内存实现
 *
 * 位于 Infrastructure 层，实现 Domain 层定义的接口
 * 可以替换为数据库实现而不影响 Domain 层
 *
 * 这就是一个具体的实现，可以随时替换
 */
@Repository
public class InMemoryConversationRepository implements ConversationRepository {

    private final ConcurrentHashMap<String, Conversation> storage = new ConcurrentHashMap<>();

    @Override
    public void save(Conversation conversation) {
        storage.put(conversation.getId(), conversation);
    }

    @Override
    public Optional<Conversation> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void delete(String id) {
        storage.remove(id);
    }

    @Override
    public boolean exists(String id) {
        return storage.containsKey(id);
    }
}
