package info.supercode.superchat.openai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.function.Consumer;

@RestController
public class OpenAIController {

    private final OpenAIService openAIService;

    public OpenAIController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping(value = "/image", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getImage(@RequestBody Question question) {
        return openAIService.getImage(question);
    }

    @PostMapping(value ="/talk", produces = "audio/mpeg")
    public byte[] talkTalk(@RequestBody Question question) {
        return openAIService.getSpeech(question);
    }

    @PostMapping("/image-upload")
    public String uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("message") String message) throws IOException {
        Resource resource = new InputStreamResource(file.getInputStream());
        return openAIService.getText(message, resource);
    }

}
