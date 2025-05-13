package com.hippo.cloud.hippoai.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.hippo.cloud.hippoai.model.deepseek.DeepSeekChatModel;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.observation.ChatClientObservationConvention;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.model.chat.client.autoconfigure.ChatClientBuilderConfigurer;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AiConfiguration {
    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultOptions(OpenAiChatOptions.builder().model("gpt-4o").build())
                .defaultSystem("你是一个专业的电商系统客服，请严格按照电商客服的标准回答用户的问题，不要回答非法的问题。回答问题时使用特朗普的口吻回答")
                .build();
    }

    @Bean
    @Scope("prototype")
    @ConditionalOnMissingBean
    ChatClient.Builder chatClientBuilder(ChatClientBuilderConfigurer chatClientBuilderConfigurer, ChatModel openAiChatModel, ObjectProvider<ObservationRegistry> observationRegistry, ObjectProvider<ChatClientObservationConvention> observationConvention) {
        ChatClient.Builder builder = ChatClient.builder(openAiChatModel, (ObservationRegistry)observationRegistry.getIfUnique(() -> {
            return ObservationRegistry.NOOP;
        }), (ChatClientObservationConvention)observationConvention.getIfUnique(() -> {
            return null;
        }));
        return chatClientBuilderConfigurer.configure(builder);
    }

    @Bean
    @ConditionalOnProperty(value = "hippo.ai.deepseek.enable", havingValue = "true")
    public DeepSeekChatModel deepSeekChatModel(HippoAiProperties hippoAiProperties) {
        HippoAiProperties.DeepSeekProperties properties = hippoAiProperties.getDeepseek();
        return buildDeepSeekChatModel(properties);
    }

    public DeepSeekChatModel buildDeepSeekChatModel(HippoAiProperties.DeepSeekProperties properties) {
        if (StrUtil.isEmpty(properties.getModel())) {
            properties.setModel(DeepSeekChatModel.MODEL_DEFAULT);
        }
        OpenAiChatModel openAiChatModel = OpenAiChatModel.builder()
                .openAiApi(OpenAiApi.builder()
                        .baseUrl(DeepSeekChatModel.BASE_URL)
                        .apiKey(properties.getApiKey())
                        .build())
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(properties.getModel())
                        .temperature(properties.getTemperature())
                        .maxTokens(properties.getMaxTokens())
                        .topP(properties.getTopP())
                        .build())
                .toolCallingManager(getToolCallingManager())
                .build();
        return new DeepSeekChatModel(openAiChatModel);
    }
    private static ToolCallingManager getToolCallingManager() {
        return SpringUtil.getBean(ToolCallingManager.class);
    }
}

