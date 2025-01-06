package info.supercode.superchat.service;

import info.supercode.superchat.model.ConversationMessage;
import info.supercode.superchat.repository.ConversationRepository;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.UserMessage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class MongoDBChatMemory implements ChatMemory {

    private final ConversationRepository conversationRepository;

    public MongoDBChatMemory(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        List<ConversationMessage> conversationMessages = messages.stream()
                .map(message -> {
                    ConversationMessage conversationMessage = new ConversationMessage();
                    conversationMessage.setConversationId(conversationId);
                    conversationMessage.setContent(message.getText());
                    conversationMessage.setMessageId(UUID.randomUUID().toString());
                    conversationMessage.setRole(message.getMessageType().getValue());
                    return conversationMessage;
                })
                .toList();

        conversationRepository.findByConversationId(conversationId)
                .ifPresent(
                        conversation -> {
                            conversation.getConversationMessages().addAll(conversationMessages);
                            conversation.setUpdatedAt(LocalDateTime.now());
                            conversationRepository.save(conversation);
                        }
                );
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        return conversationRepository
                .findByConversationId(conversationId)
                .map(conversation -> {
                    List<Message> messageList = conversation.getConversationMessages().stream()
                            .map(conversationMessage -> {
                                String messageType = conversationMessage.getRole();
                                Message message = messageType.equals(MessageType.USER.getValue()) ?
                                                new UserMessage(conversationMessage.getContent()) :
                                                new AssistantMessage(conversationMessage.getContent());
                                return message;
                            }).toList();

                    return messageList.stream().skip(Math.max(0, messageList.size() - lastN)).toList();
                }).orElse(List.of());
    }

    @Override
    public void clear(String conversationId) {
        conversationRepository.deleteById(conversationId);
    }

}
