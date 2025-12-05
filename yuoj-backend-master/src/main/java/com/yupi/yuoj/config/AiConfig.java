package com.yupi.yuoj.config;

import com.volcengine.ark.runtime.service.ArkService;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@ConfigurationProperties("ai")
public class AiConfig {

    // ApiKey
    private String apiKey;

    // 必须添加 Getter（@ConfigurationProperties 靠 Getter 读取配置）
    public String getApiKey() {
        return apiKey;
    }

    // 必须添加 Setter（@ConfigurationProperties 靠 Setter 注入配置）
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * AI 请求客户端
     *
     * @return ArkService
     */
    @Bean
    public ArkService aiService() {
        String apiKey = this.apiKey;
        String baseUrl = "https://ark.cn-beijing.volces.com/api/v3";
        ConnectionPool connectionPool = new ConnectionPool(5, 1, TimeUnit.SECONDS);
        Dispatcher dispatcher = new Dispatcher();
        ArkService service = ArkService.builder().dispatcher(dispatcher).connectionPool(connectionPool).baseUrl(baseUrl).apiKey(apiKey).build();
        return service;
    }
}
