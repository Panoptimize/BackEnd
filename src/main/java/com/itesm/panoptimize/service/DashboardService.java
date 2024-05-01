package com.itesm.panoptimize.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itesm.panoptimize.dto.dashboard.metric.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.itesm.panoptimize.dto.dashboard.MetricsDTO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DashboardService {
    private final WebClient webClient;
    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public DashboardService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8000").build();
    }
    private MetricsDTO callKPIs(RequestMetricDataV2 metricRequest) {
        MetricsDTO metricsDTO= webClient.post()
            .uri("/metrics/data")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(metricRequest)
            .retrieve()
            .bodyToMono(MetricsDTO.class)
            .block();

        return metricsDTO;
    }

    public RequestMetricDataV2 getKPIs() {

        RequestMetricDataV2 requestMetricData = new RequestMetricDataV2();

        // Set values for the fields
        requestMetricData.setEndTime(168000);
        requestMetricData.setStartTime(167000);
        requestMetricData.setMaxResults(100);
        requestMetricData.setNextToken("");
        requestMetricData.setResourceArn("arn:aws:lambda:us-west-1:123456789012:function:my-lambda-function");

        // Set up filters
        List<Filter> filters = new ArrayList<>();
        Filter regionFilter = new Filter();
        regionFilter.setFilterKey("region");
        regionFilter.setFilterValues(Arrays.asList("us-west-1", "us-east-1"));
        filters.add(regionFilter);
        requestMetricData.setFilters(filters);

        // Set up groupings
        requestMetricData.setGroupings(Arrays.asList("service"));

        // Set up interval
        Interval interval = new Interval();
        interval.setIntervalPeriod("1h");
        interval.setTimeZone("UTC");
        requestMetricData.setInterval(interval);

        // Set up metrics
        List<Metric> metrics = new ArrayList<>();
        metrics.add(createMetric("AGENT_SCHEDULE_ADHERENCE", "status", Arrays.asList("success"), false, "greater_than", 90));
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
    public MetricsDTO getMetricsData()  {
        return callKPIs(getKPIs());
    }
}