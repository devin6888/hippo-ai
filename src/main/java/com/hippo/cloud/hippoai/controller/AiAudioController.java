package com.hippo.cloud.hippoai.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;

@Tag(name = "AiChatController", description = "AI语音")
@RequestMapping("/ai/audio")
@RestController
public class AiAudioController {
    @Resource
    OpenAiAudioSpeechModel openAiAudioSpeechModel;


    @GetMapping("/generate")
    public String generate(@RequestParam(value = "msg", defaultValue = "Hello大家好，欢迎来到Hippo AI") String msg) {
        OpenAiAudioSpeechOptions speechOptions = OpenAiAudioSpeechOptions.builder()
                .voice(OpenAiAudioApi.SpeechRequest.Voice.ALLOY)
                .speed(1.0f)
                .responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                .model(OpenAiAudioApi.TtsModel.TTS_1.value)
                .build();

        SpeechPrompt speechPrompt = new SpeechPrompt(msg, speechOptions);

        SpeechResponse response = openAiAudioSpeechModel.call(speechPrompt);
        byte[] output = response.getResult().getOutput();

        //将output转为mp3文件 并返回文件路径
        // 指定输出MP3文件的路径
        String outputFilePath = System.getProperty("user.dir") + "/output.mp3";

        // 调用方法将byte数组写入文件
        try {
            writeByteArrayToFile(output, outputFilePath);
            System.out.println("MP3文件已成功创建: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("创建MP3文件时出错: " + e.getMessage());
        }

        return "ok";
    }

    /**
     * 将byte数组写入文件
     *
     * @param data         要写入的byte数组
     * @param filePath     目标文件路径
     * @throws IOException 如果写入过程中发生I/O错误
     */
    public static void writeByteArrayToFile(byte[] data, String filePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(data);
        }
    }
}
