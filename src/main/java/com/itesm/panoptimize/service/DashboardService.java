package com.itesm.panoptimize.service;

import com.itesm.panoptimize.config.Constants;
import com.itesm.panoptimize.dto.dashboard.DashboardDTO;
import com.itesm.panoptimize.dto.dashboard.metric.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import com.itesm.panoptimize.dto.dashboard.MetricsDTO;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
public class DashboardService {
    private final WebClient webClient;

    @Autowired
    public DashboardService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8000").build();
    }
    private double[] callKPIs(RequestMetricDataV2 metricRequest) {
        WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = webClient.post();
        WebClient.RequestBodySpec bodySpec = uriSpec.uri("/get_metric_data");
        WebClient.RequestHeadersSpec<?> headersSpec = bodySpec.bodyValue(metricRequest);
        WebClient.ResponseSpec responseSpec = headersSpec
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve();
        Mono<ResponseEntity<MetricsDTO>> responseEntityMono = responseSpec.toEntity(MetricsDTO.class);
        ResponseEntity<MetricsDTO> responseEntity = responseEntityMono.block();
        double[] doublesResponse = new double[3];
        assert responseEntity != null;
        return doublesResponse;
    }

    public RequestMetricDataV2 getKPIs() {

        RequestMetricDataV2 requestMetricData = new RequestMetricDataV2();

        // Set the provided values
        requestMetricData.setEndTime(168000);
        requestMetricData.setStartTime(167000);
        requestMetricData.setMaxResults(100);
        requestMetricData.setNextToken("");
        requestMetricData.setResourceArn("arn:aws:lambda:us-west-1:123456789012:function:my-lambda-function");

        // Set up filters
        Filter[] filters = new Filter[1];
        filters[0] = new Filter();
        filters[0].setFilterKey("region");
        filters[0].setFilterValues(new String[]{"us-west-1", "us-east-1"});
        requestMetricData.setFilters(filters);

        // Set up groupings
        requestMetricData.setGroupings(new String[]{"service"});

        // Set up interval
        Interval interval = new Interval();
        interval.setIntervalPeriod("1h");
        interval.setTimeZone("UTC");
        requestMetricData.setInterval(interval);

        // Set up metrics
        Metric[] metrics = new Metric[7];
        metrics[0] = createMetric("AGENT_SCHEDULE_ADHERENCE", "status", new String[]{"success"}, false, "greater_than", 90);
        metrics[1] = createMetric("ABANDONMENT_RATE", null, null, false, "less_than", 5);
        metrics[2] = createMetric("CONTACTS_HANDLED", null, null, false, "greater_than", 1000);
        metrics[3] = createMetric("SUM_HANDLE_TIME", null, null, false, "greater_than", 15000);
        metrics[4] = createMetric("SERVICE_LEVEL", null, null, false, "greater_than", 80);
        metrics[5] = createMetric("AVG_HOLD_TIME", null, null, false, "less_than", 120);
        metrics[6] = createMetric("OCCUPANCY", null, null, false, "greater_than", 75);
        requestMetricData.setMetrics(metrics);

        return requestMetricData;
    }
    private Metric createMetric(String name, String filterKey, String[] filterValues, boolean negate, String comparison, long thresholdValue) {
        Metric metric = new Metric();
        metric.setName(name);

        if (filterKey != null) {
            MetricFilter[] metricFilters = new MetricFilter[1];
            metricFilters[0] = new MetricFilter();
            metricFilters[0].setMetricFilterKey(filterKey);
            metricFilters[0].setMetricFilterValues(filterValues);
            metricFilters[0].setNegate(negate);
            metric.setMetricFilters(metricFilters);
        }

        Threshold[] thresholds = new Threshold[1];
        thresholds[0] = new Threshold();
        thresholds[0].setComparison(comparison);
        thresholds[0].setThresholdValue(thresholdValue);
        metric.setThreshold(thresholds);

        return metric;
    }

    public double[] getMetricsData()  {
        return callKPIs(getKPIs());
    }
}