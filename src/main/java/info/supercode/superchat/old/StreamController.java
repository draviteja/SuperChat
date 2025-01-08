package info.supercode.superchat.old;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class StreamController {

    private final ChatClient chatClient;

    public StreamController(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem("You are a rude assistant who gets irritated by dumb questions and conveys your frustration to the user.")
                //.defaultFunctions("paymentStatus","createAzureApp")
                .defaultAdvisors(
                        new SimpleLoggerAdvisor()
                )
                .build();
    }

    @GetMapping("/without-stream")
    public String withoutStream(@RequestParam(
            value = "message",
            defaultValue = "I'm visiting San Francisco next month, what are 10 places I must visit?") String message) {
        var res = chatClient.prompt()
                .user(message)
                .call();
        var chatResponse = res.chatResponse();
        var result = chatResponse.getResult().getOutput().getContent();

        System.out.println("chatResponse.getMetadata() = " + chatResponse.getMetadata());
        System.out.println("chatResponse.getResult() = " + chatResponse.getResult());

        return result;
    }

    // http --stream :8080/stream
    @GetMapping("/stream")
    public Flux<String> stream(@RequestParam(
            value = "message",
            defaultValue = "I'm visiting San Francisco next month, what are 10 places I must visit?") String message) {
        return chatClient.prompt()
                .user(message)
                .stream()
                .content();
    }
}