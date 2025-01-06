package info.supercode.superchat.controller;

import info.supercode.superchat.model.Conversation;
import info.supercode.superchat.model.ConversationMessage;
import info.supercode.superchat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:5173")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // Create new conversation
    @PostMapping("/conversations")
    public ResponseEntity<Conversation> createConversation(@RequestBody ConversationMessage message) {
        return ResponseEntity.ok(chatService.createConversation(message.getContent()));
    }

    // Get all conversations for sidebar
    @GetMapping("/conversations")
    public ResponseEntity<List<Conversation>> getAllConversations() {
        return ResponseEntity.ok(chatService.getAllConversations());
    }

    // Get specific conversation with its messages
    @GetMapping("/conversations/{conversationId}")
    public ResponseEntity<Conversation> getConversationById(@PathVariable String conversationId) {
        return chatService.getConversationById(conversationId).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Send message in a conversation
    @PostMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<ConversationMessage> sendMessage(
            @PathVariable String conversationId,
            @RequestBody ConversationMessage conversationMessage) {
        return ResponseEntity.ok(chatService.sendMessage(conversationId, conversationMessage));
    }

    @DeleteMapping("/conversations/{conversationId}")
    public ResponseEntity<Void> deleteConversation(@PathVariable String conversationId) {
        chatService.deleteConversation(conversationId);
        return ResponseEntity.noContent().build();
    }

}
