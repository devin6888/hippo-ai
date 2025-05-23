package com.hippo.cloud.hippoai.model.deepseek;

import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.chat.client.autoconfigure.ChatClientAutoConfiguration;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * DeepSeek {@link ChatModel} 实现类
 *
 * @author fansili
 */
@Slf4j
@Component("deepSeekChatModel")
public class DeepSeekChatModel implements ChatModel {

    public static final String BASE_URL = "https://api.deepseek.com";

    public static final String MODEL_DEFAULT = "deepseek-chat";

    /**
     * 兼容 OpenAI 接口，进行复用
     */
    private final OpenAiChatModel openAiChatModel;

    public DeepSeekChatModel(OpenAiChatModel openAiChatModel){
        this.openAiChatModel = openAiChatModel;
    }

    @Override
    public ChatResponse call(Prompt prompt) {
        return openAiChatModel.call(prompt);
    }

    @Override
    public Flux<ChatResponse> stream(Prompt prompt) {
        return openAiChatModel.stream(prompt);
    }

    @Override
    public ChatOptions getDefaultOptions() {
        return openAiChatModel.getDefaultOptions();
    }

}
