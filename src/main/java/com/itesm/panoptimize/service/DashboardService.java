package com.itesm.panoptimize.service;

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
        metrics.add(createMetric("AGENT_SCHEDULE_ADHERENCE", "status", List.of("success"), false, "greater_than", 90));
        metrics.add(createMetric("ABANDONMENT_RATE", null, null, false, "less_than", 5));
        metrics.add(createMetric("CONTACTS_HANDLED", null, null, false, "greater_than", 1000));
        metrics.add(createMetric("SUM_HANDLE_TIME", null, null, false, "greater_than", 15000));
        metrics.add(createMetric("SERVICE_LEVEL", null, null, false, "greater_than", 80));
        metrics.add(createMetric("AVG_HOLD_TIME", null, null, false, "less_than", 120));
        metrics.add(createMetric("OCCUPANCY", null, null, false, "greater_than", 75));
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
    public MetricsDTO getMetricsData(DashboardDTO dashboardDTO) {
        return callKPIs(getKPIs(dashboardDTO));
    }
}