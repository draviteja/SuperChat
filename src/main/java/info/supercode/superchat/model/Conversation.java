package info.supercode.superchat.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Document(collection = "conversations")
public class Conversation {
    @Id
    private String id;
    private String conversationId;
    private String userId;
    private String title;
    private List<ConversationMessage> conversationMessages = new ArrayList<>();
    private Context context;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    //private Metadata metadata;

    public Conversation() {
        conversationId = UUID.randomUUID().toString();
    }

    @Data
    public static class Context {
        private Object metadata;
        private Object modelParameters;

    }
}
