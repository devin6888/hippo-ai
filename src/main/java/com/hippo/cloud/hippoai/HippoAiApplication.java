package com.hippo.cloud.hippoai;

import org.springframework.ai.model.openai.autoconfigure.OpenAiChatAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HippoAiApplication {

    public static void main(String[] args) {
        String proxyHost = "127.0.0.1";
        int proxyPort = 7890;

        // 设置全局 JVM 代理
//        System.setProperty("proxyType", "4");
//        System.setProperty("proxyPort", String.valueOf(proxyPort));
//        System.setProperty("proxyHost", proxyHost);
//        System.setProperty("proxySet", "true");

        SpringApplication.run(HippoAiApplication.class, args);
    }

}
