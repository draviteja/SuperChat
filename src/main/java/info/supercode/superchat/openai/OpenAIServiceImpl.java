package info.supercode.superchat.openai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.model.Media;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.speech.SpeechModel;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import javax.print.Doc;
import javax.swing.event.DocumentEvent;
import java.util.Base64;
import java.util.List;
import java.util.function.Consumer;

@Service
public class OpenAIServiceImpl implements OpenAIService {

    private final ImageModel imageModel;
    private final SpeechModel speechModel;
    private final ChatModel chatModel;
    private final VectorStore vectorStore;

    public OpenAIServiceImpl(ImageModel imageModel, OpenAiAudioSpeechModel speechModel, ChatModel chatModel, VectorStore vectorStore) {
        this.imageModel = imageModel;
        this.speechModel = speechModel;
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
    }

    @Override
    public byte[] getImage(Question question) {

        var options = OpenAiImageOptions.builder()
                .height(1024)
                .width(1024)
                .responseFormat("b64_json")
                .model("dall-e-3")
                .build();

        ImagePrompt imagePrompt = new ImagePrompt(question.question(), options);

        var imageResponse = imageModel.call(imagePrompt);

        return Base64.getDecoder().decode(imageResponse.getResult().getOutput().getB64Json());
    }

    @Override
    public byte[] getSpeech(Question question) {
        OpenAiAudioSpeechOptions speechOptions = OpenAiAudioSpeechOptions.builder()
                .voice(OpenAiAudioApi.SpeechRequest.Voice.ECHO)
                .speed(1.0f)
                .responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                .model(OpenAiAudioApi.TtsModel.TTS_1.value)
                .build();

        SpeechPrompt speechPrompt = new SpeechPrompt(question.question(),
                speechOptions);

        SpeechResponse response = speechModel.call(speechPrompt);

        return response.getResult().getOutput();
    }

    @Override
    public String getText(String message, Resource resource) {
//        Consumer<ChatClient.PromptUserSpec> promptUserSpecConsumer = u -> u.text(message)
//                .media(MimeTypeUtils.IMAGE_PNG, resource);

        var userMessage = new UserMessage(
                "Explain what do you see in this picture?", // text content
                List.of(new Media(MimeTypeUtils.IMAGE_PNG, resource))); // image content

        return chatModel.call(new Prompt(userMessage)).getResult().getOutput().getContent();
    }

    @Override
    public String uploadDoc(Resource resource) {
//        if((resource.getFilename().toLowerCase()).endsWith(".pdf")) {
//            PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(resource, PdfDocumentReaderConfig.builder()
//                    .withPageTopMargin(0)
//                    .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
//                            .withNumberOfTopTextLinesToDelete(0)
//                            .build())
//                    .withPagesPerDocument(1)
//                    .build());
//            List<Document> documents =  pdfReader.read();
//            TextSplitter textSplitter = new TokenTextSplitter();
//            List<Document> splitDocuments = textSplitter.apply(documents);
//            vectorStore.add(splitDocuments);
//        } else if((resource.getFilename().toLowerCase()).endsWith(".txt")) {
//
//
//        }

        TextReader textReader = new TextReader(resource);
        List<Document> documents = textReader.read();
        TextSplitter textSplitter = new TokenTextSplitter();
        List<Document> splitDocuments = textSplitter.apply(documents);
        vectorStore.add(splitDocuments);
        return "";
    }
}