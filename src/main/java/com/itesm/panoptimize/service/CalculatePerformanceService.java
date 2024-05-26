package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.performance.AgentPerformanceDTO;
import com.itesm.panoptimize.dto.performance.PerformanceDTO;
import com.itesm.panoptimize.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.connect.ConnectClient;
import software.amazon.awssdk.services.connect.model.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.*;

@Service
public class CalculatePerformanceService {
    private final ConnectClient connectClient;

    @Autowired
    public CalculatePerformanceService(ConnectClient connectClient) {
        this.connectClient = connectClient;
    }

    private GetMetricDataV2Response getKPIs(PerformanceDTO performanceDTO, List<MetricV2> metrics, String agentId) {
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
        if (agentId != null && !agentId.isEmpty()) {
            filters.add(FilterV2.builder()
                    .filterKey("AGENT")
                    .filterValues(Collections.singletonList(agentId))
                    .build());
        }

        IntervalDetails intervalDetails = IntervalDetails.builder()
                .timeZone("UTC")
                .intervalPeriod("DAY")
                .build();

        return connectClient.getMetricDataV2(GetMetricDataV2Request.builder()
                .startTime(startTime)
                .endTime(endTime)
                .interval(intervalDetails)
                .resourceArn(Constants.BASE_ARN + ":instance/" + instanceId)
                .filters(filters)
                .metrics(metrics)
                .build());
    }

    public List<AgentPerformanceDTO> getMetricsData(PerformanceDTO performanceDTO) {
        List<MetricV2> metricList = new ArrayList<>(Arrays.asList(
                MetricV2.builder().name("AVG_HANDLE_TIME").build(),
                MetricV2.builder().name("AVG_AFTER_CONTACT_WORK_TIME").build(),
                MetricV2.builder().name("AVG_HOLD_TIME").build(),
                MetricV2.builder().name("AVG_ABANDON_TIME").build()
        ));

        Map<String, String> agentMap = getAgentIdsAndNames(performanceDTO.getInstanceId());
        List<AgentPerformanceDTO> agentPerformancesList = new ArrayList<>();

        for (Map.Entry<String, String> entry : agentMap.entrySet()) {
            String agentId = entry.getKey();
            String agentName = entry.getValue();

            GetMetricDataV2Response response = getKPIs(performanceDTO, metricList, agentId);
            Map<String, Double> metricsMap = new HashMap<>();

            for (MetricResultV2 result : response.metricResults()) {
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
                metricsMap.put(agentName, performanceScore);
            }

            for (Map.Entry<String, Double> metricEntry : metricsMap.entrySet()) {
                agentPerformancesList.add(new AgentPerformanceDTO(metricEntry.getKey(), Collections.singletonList(metricEntry.getValue())));
            }
        }

        return agentPerformancesList;
    }

    private Map<String, String> getAgentIdsAndNames(String instanceId) {
        Map<String, String> agentMap = new HashMap<>();
        String nextToken = null;

        do {
            ListUsersRequest request = ListUsersRequest.builder()
                    .instanceId(instanceId)
                    .nextToken(nextToken)
                    .maxResults(100)
                    .build();

            ListUsersResponse response = connectClient.listUsers(request);
            for (UserSummary userSummary : response.userSummaryList()) {
                agentMap.put(userSummary.id(), userSummary.username());
            }

            nextToken = response.nextToken();
        } while (nextToken != null);

        return agentMap;
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
