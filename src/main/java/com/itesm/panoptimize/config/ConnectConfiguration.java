package com.itesm.panoptimize.config;

import com.itesm.panoptimize.util.AwsRequestSigner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.connect.ConnectClient;
import software.amazon.awssdk.core.retry.RetryPolicy;
import software.amazon.awssdk.core.retry.backoff.EqualJitterBackoffStrategy;
import java.time.Duration;

@Configuration
public class ConnectConfiguration {

    @Bean
    public ConnectClient connectClient() {
        return ConnectClient.builder()
                .region(Region.of(System.getenv("AWS_REGION")))
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
    }
    @Bean
    public AwsRequestSigner awsRequestSigner() {
        return new AwsRequestSigner();
    }
}
