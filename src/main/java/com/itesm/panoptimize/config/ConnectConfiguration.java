package com.itesm.panoptimize.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.core.retry.RetryPolicy;
import software.amazon.awssdk.core.retry.backoff.EqualJitterBackoffStrategy;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.connect.ConnectClient;

import java.time.Duration;

@Configuration
public class ConnectConfiguration {

    private static final int NUMBER_OF_RETRIES = 3;

    private static final RetryPolicy RETRY_POLICY = RetryPolicy.builder()
            .numRetries(NUMBER_OF_RETRIES)
            .backoffStrategy(EqualJitterBackoffStrategy.builder()
                    .baseDelay(Duration.ofSeconds(1))
                    .maxBackoffTime(Duration.ofSeconds(5))
                    .build())
            .build();

    @Bean
    public ConnectClient connectClient() {
        return ConnectClient.builder()
                .region(Region.US_WEST_2)
                .overrideConfiguration(b -> b.retryPolicy(RETRY_POLICY))
                .build();
    }
}
