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

import com.itesm.panoptimize.dto.dashboard.DashboardDTO;
import com.itesm.panoptimize.dto.dashboard.metric.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.itesm.panoptimize.dto.dashboard.MetricsDTO;

import java.util.*;

@Service
public class DashboardService {
    private final WebClient webClient;

    @Autowired
    public DashboardService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8000").build();
    }

    private MetricsDTO callKPIs(RequestMetricDataV2 metricRequest) {

        return webClient.post()
                .uri("/metrics/data")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(metricRequest)
                .retrieve()
                .bodyToMono(MetricsDTO.class)
                .block();
    }

    public RequestMetricDataV2 getKPIs(DashboardDTO dashboardDTO) {

        // Set up the date to unix time
        long startTime = dashboardDTO.getStartDate().getTime();
        long endTime = dashboardDTO.getEndDate().getTime();

        RequestMetricDataV2 requestMetricData = new RequestMetricDataV2();

        // Set values for the fields
        requestMetricData.setEndTime(startTime);
        requestMetricData.setStartTime(endTime);
        requestMetricData.setMaxResults(100);
        requestMetricData.setNextToken("");
        requestMetricData.setResourceArn("arn:aws:lambda:us-west-1:123456789012:function:my-lambda-function");

        // Set up filters
        List<Filter> filters = new ArrayList<>();
        if (dashboardDTO.getAgents().length > 0) {
            Filter agentFilter = new Filter();
            agentFilter.setFilterKey("AGENT");
            agentFilter.setFilterValues(List.of(Arrays.toString(dashboardDTO.getAgents())));
            filters.add(agentFilter);
        }
        if (dashboardDTO.getWorkspaces().length > 0) {
            Filter workspaceFilter = new Filter();
            workspaceFilter.setFilterKey("ROUTING_PROFILE");
            workspaceFilter.setFilterValues(List.of(Arrays.toString(dashboardDTO.getWorkspaces())));
            filters.add(workspaceFilter);
        }
        requestMetricData.setFilters(filters);

        // Set up groupings
        requestMetricData.setGroupings(List.of("service"));

        // Set up interval
        Interval interval = new Interval();
        interval.setIntervalPeriod("1h");
        interval.setTimeZone("UTC");
        requestMetricData.setInterval(interval);

        // Set up metrics
        List<Metric> metrics = new ArrayList<>();
        metrics.add(createMetric("PERCENT_CASES_FIRST_CONTACT_RESOLVED", "status", List.of("success"), false, "greater_than", 90));
        metrics.add(createMetric("ABANDONMENT_RATE", null, null, false, "less_than", 5));
        metrics.add(createMetric("CONTACTS_HANDLED", null, null, false, "greater_than", 1000));
        metrics.add(createMetric("SUM_HOLD_TIME", null, null, false, "greater_than", 15000));
        metrics.add(createMetric("SERVICE_LEVEL", null, null, false, "greater_than", 80));
        metrics.add(createMetric("AVG_HOLD_TIME", null, null, false, "less_than", 120));
        metrics.add(createMetric("AGENT_SCHEDULE_ADHERENCE", null, null, false, "greater_than", 75));
        requestMetricData.setMetrics(metrics);

        return requestMetricData;
    }

    private Metric createMetric(String name, String filterKey, List<String> filterValues, boolean negate, String comparison, long thresholdValue) {
        Metric metric = new Metric();
        metric.setName(name);

        if (filterKey != null) {
            List<MetricFilter> metricFilters = new ArrayList<>();
            MetricFilter metricFilter = new MetricFilter();
            metricFilter.setMetricFilterKey(filterKey);
            metricFilter.setMetricFilterValues(filterValues);
            metricFilter.setNegate(negate);
            metricFilters.add(metricFilter);
            metric.setMetricFilters(metricFilters);
        } else {
            metric.setMetricFilters(new ArrayList<>());
        }

        List<Threshold> thresholds = new ArrayList<>();
        Threshold threshold = new Threshold();
        threshold.setComparison(comparison);
        threshold.setThresholdValue(thresholdValue);
        thresholds.add(threshold);
        metric.setThreshold(thresholds);

        return metric;
    }

    public Map<String, Double> getMetricsData(DashboardDTO dashboardDTO) {
        MetricsDTO metricsDTO = callKPIs(getKPIs(dashboardDTO));

        Map<String, Double> metricsData = new HashMap<>();

        metricsDTO.getMetricResults().forEach(metricResult -> {
            metricResult.getCollections().forEach(collection -> {
                metricsData.put(collection.getMetric().getName().getName(), collection.getValue());
            });
        });

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