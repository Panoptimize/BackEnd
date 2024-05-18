package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.performance.PerformanceDTO;
import com.itesm.panoptimize.util.Constants;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.services.connect.ConnectClient;
import java.time.Duration;

import software.amazon.awssdk.services.connect.model.*;

import java.time.Instant;
import java.util.*;

@Service
public class CalculatePerformanceService {
    private final ConnectClient connectClient;

    @Autowired
    public CalculatePerformanceService(ConnectClient connectClient) {
        this.connectClient = connectClient;
    }


    private GetMetricDataV2Response getKPIs(@NotNull PerformanceDTO performanceDTO, List<MetricV2> metrics) {
        String instanceId = performanceDTO.getInstanceId();
        Instant startTime = performanceDTO.getStartDate().toInstant();
        Instant endTime = performanceDTO.getEndDate().toInstant();

        List<FilterV2> filters = new ArrayList<>();
        if (performanceDTO.getRoutingProfiles().length > 0) {
            filters.add(FilterV2.builder()
                    .filterKey("ROUTING_PROFILE")
                    .filterValues(Arrays.asList(performanceDTO.getRoutingProfiles()))
                    .build());
        }
        if (performanceDTO.getQueues().length > 0) {
            filters.add(FilterV2.builder()
                    .filterKey("QUEUE")
                    .filterValues(Arrays.asList(performanceDTO.getQueues()))
                    .build());
        }

        IntervalDetails intervalDetails = IntervalDetails.builder()
                .timeZone("UTC")
                .intervalPeriod("FIFTEEN_MIN")
                .build();

        return connectClient.getMetricDataV2(GetMetricDataV2Request.builder()
                .startTime(startTime)
                .endTime(endTime)
                .interval(intervalDetails)
                .groupings(Arrays.asList("AGENT"))
                .resourceArn(Constants.BASE_ARN + ":instance/" + instanceId)
                .filters(filters)
                .metrics(metrics)
                .build());
    }


    public Map<String, List<Map<String, Double>>> getMetricsData(PerformanceDTO performanceDTO) {
        List<MetricV2> metricList = new ArrayList<>(Arrays.asList(
                MetricV2.builder().name("AVG_HANDLE_TIME").build(),
                MetricV2.builder().name("AVG_TALK_TIME").build(),
                MetricV2.builder().name("AVG_AFTER_CONTACT_WORK_TIME").build(),
                MetricV2.builder().name("CONTACTS_HANDLED").build()
        ));

        GetMetricDataV2Response response = getKPIs(performanceDTO, metricList);

        Map<String, List<Map<String, Double>>> agentMetrics = new HashMap<>();

        for (MetricResultV2 result : response.metricResults()) {
            String agentId = result.dimensions().getOrDefault("AGENT", "Unknown Agent");

            agentMetrics.putIfAbsent(agentId, new ArrayList<>());

            Map<String, Double> metricsMap = new HashMap<>();
            for (MetricDataV2 data : result.collections()) {
                metricsMap.put(data.metric().name(), data.value() != null ? data.value() : 0.0);
            }

            agentMetrics.get(agentId).add(metricsMap);
        }

        System.out.println("Response received: " + response);
        return agentMetrics;
    }

}
