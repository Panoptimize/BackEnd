package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.performance.PerformanceDTO;
import com.itesm.panoptimize.util.Constants;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.services.connect.ConnectClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
                .intervalPeriod("HOUR")
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

    public Map<String, List<Double>> getMetricsData(PerformanceDTO performanceDTO) {
        List<MetricV2> metricList = new ArrayList<>(Arrays.asList(
                MetricV2.builder().name("AVG_HANDLE_TIME").build(),
                MetricV2.builder().name("AVG_AFTER_CONTACT_WORK_TIME").build(),
                MetricV2.builder().name("AVG_HOLD_TIME").build(),
                MetricV2.builder().name("AVG_ABANDON_TIME").build()
        ));

        GetMetricDataV2Response response = getKPIs(performanceDTO, metricList);
        Map<String, List<Double>> agentPerformances = new HashMap<>();

        for (MetricResultV2 result : response.metricResults()) {
            String agentId = result.dimensions().getOrDefault("AGENT", "Unknown Agent");
            if (!agentPerformances.containsKey(agentId)) {
                agentPerformances.put(agentId, new ArrayList<>());
            }

            double avgHandleTime = 0.0;
            double avgAfterContactWorkTime = 0.0;
            double avgHoldTime = 0.0;
            double avgAbandonTime = 0.0;

            for (MetricDataV2 data : result.collections()) {
                double value = getValue(data, data.metric().name());

                switch (data.metric().name()) {
                    case "AVG_HANDLE_TIME":
                        avgHandleTime += value;
                        break;
                    case "AVG_AFTER_CONTACT_WORK_TIME":
                        avgAfterContactWorkTime += value;
                        break;
                    case "AVG_HOLD_TIME":
                        avgHoldTime += value;
                        break;
                    case "AVG_ABANDON_TIME":
                        avgAbandonTime += value;
                        break;
                }
            }

            double performanceScore = calculateAgentPerformanceScore(avgHandleTime, avgAfterContactWorkTime, avgHoldTime, avgAbandonTime);
            agentPerformances.get(agentId).add(performanceScore);
        }

        return agentPerformances;
    }

    private double getValue(MetricDataV2 data, String metricName) {
        return data.metric().name().equals(metricName) && data.value() != null ? data.value() : 0.0;
    }

    private double calculateAgentPerformanceScore(double avgHandleTime, double avgAfterContactWorkTime, double avgHoldTime, double avgAbandonTime) {
        double normalizedHandleTime = avgHandleTime < 100 ? 1 : 100 / avgHandleTime;
        double normalizedAfterContactWorkTime = avgAfterContactWorkTime < 10 ? 1 : 10 / avgAfterContactWorkTime;
        double normalizedHoldTime = avgHoldTime == 0 ? 1 : 1 / avgHoldTime;
        double normalizedAbandonTime = avgAbandonTime == 0 ? 1 : 1 / avgAbandonTime;

        double score = (
                0.25 * normalizedHandleTime +
                        0.25 * normalizedAfterContactWorkTime +
                        0.25 * normalizedHoldTime +
                        0.25 * normalizedAbandonTime) * 100;

        BigDecimal bd = new BigDecimal(score);
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
