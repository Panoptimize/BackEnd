package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.contact.CollectionDTO;
import com.itesm.panoptimize.dto.contact.MetricResultDTO;
import com.itesm.panoptimize.dto.contact.MetricResultsDTO;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.itesm.panoptimize.dto.dashboard.DashboardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import com.itesm.panoptimize.dto.dashboard.MetricsDTO;
import software.amazon.awssdk.services.connect.ConnectClient;
import software.amazon.awssdk.services.connect.model.*;

import java.util.*;

@Service
public class DashboardService {
    private final WebClient webClient;
    private final ConnectClient connectClient;

    @Autowired
    public DashboardService(WebClient.Builder webClientBuilder, ConnectClient connectClient){
        this.webClient = webClientBuilder.baseUrl("http://localhost:8000").build();
        this.connectClient = connectClient;
    }

    /**
     * Get KPIs from Amazon Connect. These KPIs are the following:
     * - Service Level
     * - Average Hold Time
     * - Average Speed of Answer
     * - Schedule Adherence
     * - First Contact Resolution
     * @param dashboardDTO is the DTO that contains the filters to get the KPIs
     * @return a list of KPIs
     */
    public GetMetricDataV2Response getKPIs(DashboardDTO dashboardDTO) {

        Instant startTime = dashboardDTO.getStartDate().toInstant();
        Instant endTime = dashboardDTO.getEndDate().toInstant();

        // Set up filters
        List<FilterV2> filters = new ArrayList<>();

        if (dashboardDTO.getRoutingProfiles().length > 0) {
            FilterV2 routingProfileFilter = FilterV2.builder()
                    .filterKey("ROUTING_PROFILE")
                    .filterValues(Arrays.asList(dashboardDTO.getRoutingProfiles()))
                    .build();
            filters.add(routingProfileFilter);
        }

        if (dashboardDTO.getQueues().length > 0) {
            FilterV2 queueFilter = FilterV2.builder()
                    .filterKey("QUEUE")
                    .filterValues(Arrays.asList(dashboardDTO.getQueues()))
                    .build();
            filters.add(queueFilter);
        }

        // Set up metrics
        List<MetricV2> metrics = new ArrayList<>();

        // Service Level
        MetricV2 serviceLevel = MetricV2.builder()
                .name("SERVICE_LEVEL")
                .threshold(ThresholdV2.builder()
                        .comparison("LT")
                        .thresholdValue(80.0)
                        .build())
                .build();

        metrics.add(serviceLevel);

        // Average Speed of Answer
        MetricV2 averageSpeedOfAnswer = MetricV2.builder()
                .name("ABANDONMENT_RATE")
                .build();

        metrics.add(averageSpeedOfAnswer);

        // Average Hold Time
        MetricV2 averageHoldTime = MetricV2.builder()
                .name("AVG_HOLD_TIME")
                .build();

        metrics.add(averageHoldTime);

        // Schedule Adherence
        MetricV2 scheduleAdherence = MetricV2.builder()
                .name("AGENT_SCHEDULE_ADHERENCE")
                .build();

        metrics.add(scheduleAdherence);

        // First Contact Resolution
        MetricV2 firstContactResolution = MetricV2.builder()
                .name("PERCENT_CASES_FIRST_CONTACT_RESOLVED")
                .build();

        metrics.add(firstContactResolution);


        return connectClient.getMetricDataV2(GetMetricDataV2Request.builder()
                .startTime(startTime)
                .endTime(endTime)
                .resourceArn("arn:aws:connect:us-east-1:674530197385:instance/7c78bd60-4a9f-40e5-b461-b7a0dfaad848")
                .filters(filters)
                .metrics(metrics)
                .build());
    }

    public Map<String, Double> getMetricsData(DashboardDTO dashboardDTO) {
        GetMetricDataV2Response response = getKPIs(dashboardDTO);

        Map<String, Double> metricsData = new HashMap<>();

        for (MetricResultV2 metricData : response.metricResults()) {
            List<MetricDataV2> metrics = metricData.collections();
            for (MetricDataV2 metric : metrics) {
                metricsData.put(metric.metric().name(), metric.value());
            }
        }

        return metricsData;
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