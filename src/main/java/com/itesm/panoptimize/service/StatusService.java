package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.agent.StatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
 * Service that manages agent statuses
 * This service is responsible for querying specific agent metrics from an Amazon Connect API simulator.
 * @version 1.2
 */
@Service
public class StatusService {

    private final WebClient webClient;

    @Autowired
    public StatusService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8000").build();
    }

    /**
     * This method creates a formatted time string
     * @return formatted time string
     */

    Instant now = Instant.now();
    LocalDateTime localDateTime = LocalDateTime.ofInstant(now, ZoneId.systemDefault());
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
    String formattedTime = localDateTime.format(formatter);


    /**
     * This method makes a POST request to the metrics endpoint
     * and returns the data in a StatusDTO object
     * @return StatusDTO object with the metrics data
     */


    public StatusDTO filterMetrics() {
        List<Map<String, String>> metrics = Arrays.asList(
                createMetric("AGENTS_AVAILABLE", "COUNT"),
                createMetric("AGENTS_AFTER_CONTACT_WORK", "COUNT"),
                createMetric("AGENTS_ON_CONTACT", "COUNT"),
                createMetric("AGENTS_ONLINE", "COUNT")
        );


        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("Metrics", metrics);
        requestBody.put("DataSnapshotTime", formattedTime);
        requestBody.put("Filters", new HashMap<String, List<String>>());
        requestBody.put("Message", null);



        StatusDTO statusDTO = webClient.post()
                .uri("/status")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(StatusDTO.class)
                .block();

        return statusDTO;
    }

    /**
     * This method creates a metric map
     * @param name metric name
     * @param unit metric unit
     * @return metric map
     */

    private Map<String, String> createMetric(String name, String unit) {
        Map<String, String> metric = new HashMap<>();
        metric.put("Name", name);
        metric.put("Unit", unit);
        return metric;
    }

}
