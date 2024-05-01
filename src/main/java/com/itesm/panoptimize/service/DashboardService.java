package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.contact.CollectionDTO;
import com.itesm.panoptimize.dto.contact.MetricResultDTO;
import com.itesm.panoptimize.dto.contact.MetricResultsDTO;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;


@Service
public class DashboardService {

    private final WebClient webClient;

    public DashboardService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8000").build();
    }

    public Mono<MetricResultsDTO> getMetricResults() {
        String requestBody = "{"
                + "\"InstanceId\": \"example-instance-id\","
                + "\"Filters\": {},"
                + "\"Groupings\": [\"CHANNEL\"],"
                + "\"CurrentMetrics\": ["
                + "{ \"Name\": \"CONTACTS_IN_PROGRESS\", \"Unit\": \"COUNT\" }"
                + "]"
                + "}";

        return webClient.post()
                .uri("/metrics")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(MetricResultsDTO.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    System.err.println("Error al llamar a la API: " + ex.getStatusCode() + " - " + ex.getResponseBodyAsString());
                    return Mono.empty();
                });
    }



    public List<Integer> extractValues(MetricResultsDTO metricResults) {
        List<Integer> values = new ArrayList<>();
        if (metricResults == null || metricResults.getMetricResults() == null) {
            return values;
        }

        for (MetricResultDTO metricResult : metricResults.getMetricResults()) {
            if (metricResult.getCollections() != null) {
                for (CollectionDTO collection : metricResult.getCollections()) {
                    values.add(collection.getValue());
                }
            }
        }

        return values;
    }
}