package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.agent.StatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/*
 * Service that manages agent statuses
 * This service is responsible for querying specific agent metrics from an Amazon Connect API simulator.
 * @version 1.0
 */
@Service
public class StatusService {

    private final WebClient webClient;

    @Autowired
    public StatusService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8000").build();
    }

    /**
     * This method makes a POST request to the metrics endpoint
     * and returns the data in a StatusDTO object
     * @return StatusDTO object with the metrics data
     */
    public StatusDTO filterMetrics() {
        String requestBody = "{"
                + "\"Metrics\": ["
                + "{ \"Name\": \"AGENTS_AVAILABLE\", \"Unit\": \"COUNT\" },"
                + "{ \"Name\": \"AGENTS_AFTER_CONTACT_WORK\", \"Unit\": \"COUNT\" },"
                + "{ \"Name\": \"AGENTS_ON_CONTACT\", \"Unit\": \"COUNT\" },"
                + "{ \"Name\": \"AGENTS_ONLINE\", \"Unit\": \"COUNT\" }"
                + "],"
                + "\"DataSnapshotTime\": \"2024-04-29T03:35:15.787830\","
                + "\"Filters\": { \"Channels\": [], \"Queues\": [] },"
                + "\"Message\": null"
                + "}";

        StatusDTO statusDTO = webClient.post()
                .uri("/status")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(StatusDTO.class)
                .block();  // block is used to wait for the response mono to complete

        return statusDTO;
    }
}
