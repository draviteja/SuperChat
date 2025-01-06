package info.supercode.superchat.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "message")
public class ConversationMessage {

    private String messageId;
    private String role; // "user" or "assistant"
    private String content;
    private String conversationId;
    private LocalDateTime timestamp = LocalDateTime.now();

}