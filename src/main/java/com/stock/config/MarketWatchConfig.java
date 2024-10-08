package com.stock.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableJpaRepositories(basePackages = "com.stock.repositories")
@EntityScan(basePackages = "com.stock.model")
public class MarketWatchConfig {

	@Value("${fmp.addressservice.base.url}")
    private String addressBaseUrl;
	
	@Bean
    public WebClient webClient() {
        return WebClient.builder().baseUrl(addressBaseUrl).build();
    }
}
