package com.hippo.cloud.hippoai.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.ai.openai.api.OpenAiImageApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Tag(name = "AiChatController", description = "AI图片")
@RequestMapping("/ai/image")
@RestController
public class AiImageController {
    @Resource
    OpenAiImageModel openaiImageModel;


    @GetMapping("/generate")
    public String generate(@RequestParam(value = "msg", defaultValue = "画一只猫") String msg) {
        ImageResponse response = openaiImageModel.call(
                new ImagePrompt(msg,
                        OpenAiImageOptions.builder()
                                .model(OpenAiImageApi.DEFAULT_IMAGE_MODEL)
                                .quality("hd")
                                .N(1)
                                .height(1024)
                                .width(1024).build())

        );
        return response.getResult().getOutput().getUrl();
    }
}
