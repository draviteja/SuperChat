package info.supercode.superchat.openai;

import org.springframework.core.io.Resource;

public interface OpenAIService {

    byte[] getImage(Question question);

    byte[] getSpeech(Question question);

    String getText(String message, Resource resource);

    String uploadDoc(Resource resource);
}