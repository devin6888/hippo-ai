package com.hippo.cloud.hippoai.controller;

import com.hippo.cloud.hippoai.model.deepseek.DeepSeekChatModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Tag(name = "AiChatController", description = "AI对话")
@RequestMapping("/ai/chat")
@RestController
public class AiChatController {
    @Resource
    ChatModel openAiChatModel;


    @GetMapping("/generate")
    public String chat(@RequestParam(value = "msg", defaultValue = "讲个笑话") String msg) {
        ChatResponse response = openAiChatModel.call(
                new Prompt(
                        msg,
                        OpenAiChatOptions.builder()
                                .model("gpt-4o")
                                .temperature(0.4)
                                .build()
                ));
        return response.getResult().getOutput().getText();
    }
    @GetMapping(value = "/stream/generate", produces = "text/html;charset=UTF-8")
    public Flux<String> streamChat(@RequestParam(value = "msg", defaultValue = "讲个笑话") String msg) {
        Flux<ChatResponse> chatResponse = openAiChatModel.stream(
                new Prompt(
                        msg,
                        OpenAiChatOptions.builder()
                                .model("gpt-4o")
                                .temperature(0.4)
                                .build()
                ));
        return  chatResponse.map(response -> response.getResult().getOutput().getText());
    }



}
