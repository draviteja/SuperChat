package info.supercode.superchat.service;

import info.supercode.superchat.model.Conversation;
import info.supercode.superchat.model.ConversationMessage;
import info.supercode.superchat.repository.ConversationRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;

@Service
public class ChatService {

    private final ConversationRepository conversationRepository;

    private final ChatClient chatClient;

    @Autowired
    public ChatService(ChatClient.Builder builder, ConversationRepository conversationRepository, VectorStore vectorStore) {
        this.conversationRepository = conversationRepository;
        this.chatClient = builder
                .defaultSystem("You are Tars, a smart, inquisitive and helpful personal assistant. Use your general knowledge and past conversation with the user as context to inform your responses.")
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(new MongoDBChatMemory(conversationRepository)),
                        new QuestionAnswerAdvisor(vectorStore, SearchRequest.builder().build()),
                        new SimpleLoggerAdvisor()
                )
                .build();
    }

    public Conversation createConversation(String prompt) {
        Conversation conversation = new Conversation();
        conversation.setTitle(prompt);
        conversation.setConversationId(UUID.randomUUID().toString());
        return conversationRepository.save(conversation);
    }

    public List<Conversation> getAllConversations() {
        return conversationRepository.findAll();
    }

    public Optional<Conversation> getConversationById(String conversationId) {
        return conversationRepository.findByConversationId(conversationId);
    }

    public ConversationMessage sendMessage(String conversationId, ConversationMessage conversationMessage) {

        var content = chatClient.prompt()
                .user(conversationMessage.getContent())
                .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, conversationId))
                .call()
                .content();

        ConversationMessage assistantConversationMessage = new ConversationMessage();
        assistantConversationMessage.setContent(content);
        assistantConversationMessage.setRole("assistant");
        assistantConversationMessage.setConversationId(conversationId);
        return assistantConversationMessage;
    }

    public void deleteConversation(String conversationId) {
        conversationRepository.deleteByConversationId(conversationId);
    }

}
