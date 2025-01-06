package info.supercode.superchat.repository;

import info.supercode.superchat.model.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ConversationRepository extends MongoRepository<Conversation, String> {

    Optional<Conversation> findByConversationId(String conversationId);
    void deleteByConversationId(String conversationId);

}
