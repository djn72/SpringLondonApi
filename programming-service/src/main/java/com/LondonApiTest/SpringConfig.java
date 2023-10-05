package com.LondonApiTest;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class SpringConfig {
    @Value("${london.api.url}")
    @Getter
    private String baseApi;

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
