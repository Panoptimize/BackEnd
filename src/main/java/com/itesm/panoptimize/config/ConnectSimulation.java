package com.itesm.panoptimize.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ConnectSimulation {
    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl(Constants.CONNECT_SIMULATION_URL).build();
    }
}
