package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.performance.PerformanceDTO;
import com.itesm.panoptimize.model.AgentPerformance;
import com.itesm.panoptimize.repository.AgentPerformanceRepository;
import com.itesm.panoptimize.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.connect.ConnectClient;
import software.amazon.awssdk.services.connect.model.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class StorePerformanceData {

    private final ConnectClient connectClient;
    private final AgentPerformanceRepository agentPerformanceRepository;

    @Autowired
    public StorePerformanceData(ConnectClient connectClient, AgentPerformanceRepository agentPerformanceRepository) {
        this.connectClient = connectClient;
        this.agentPerformanceRepository = agentPerformanceRepository;
    }

    private GetMetricDataV2Response getKPIs(PerformanceDTO performanceDTO, List<MetricV2> metrics, String agentId) {
        String instanceId = performanceDTO.getInstanceId();
        ZonedDateTime startTime = ZonedDateTime.now(ZoneId.systemDefault()).minusHours(1); // Última hora
        ZonedDateTime endTime = ZonedDateTime.now(ZoneId.systemDefault()); // Tiempo actual

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
                .intervalPeriod("HOUR")
                .build();

        return connectClient.getMetricDataV2(GetMetricDataV2Request.builder()
                .startTime(startTime.toInstant())
                .endTime(endTime.toInstant())
                .interval(intervalDetails)
                .resourceArn(Constants.BASE_ARN + ":instance/" + instanceId)
                .filters(filters)
                .metrics(metrics)
                .build());
    }

    public void getMetricsData(PerformanceDTO performanceDTO) {
        List<MetricV2> metricList = new ArrayList<>(Arrays.asList(
                MetricV2.builder().name("AVG_HANDLE_TIME").build(),
                MetricV2.builder().name("AVG_AFTER_CONTACT_WORK_TIME").build(),
                MetricV2.builder().name("AVG_HOLD_TIME").build(),
                MetricV2.builder().name("AVG_ABANDON_TIME").build()
        ));

        Map<String, String> agentMap = getAgentIdsAndNames(performanceDTO.getInstanceId());
        List<AgentPerformance> agentPerformancesList = new ArrayList<>();

        for (Map.Entry<String, String> entry : agentMap.entrySet()) {
            String agentId = entry.getKey();

            GetMetricDataV2Response response = getKPIs(performanceDTO, metricList, agentId);

            double avgHandleTime = 0.0;
            double avgAfterContactWorkTime = 0.0;
            double avgHoldTime = 0.0;
            double avgAbandonTime = 0.0;

            for (MetricResultV2 result : response.metricResults()) {
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
            }

            AgentPerformance agentPerformance = new AgentPerformance();
            agentPerformance.setAgentId(agentId);
            agentPerformance.setAvgHandleTime(avgHandleTime);
            agentPerformance.setAvgAfterContactWorkTime(avgAfterContactWorkTime);
            agentPerformance.setAvgHoldTime(avgHoldTime);
            agentPerformance.setAvgAbandonTime(avgAbandonTime);
            agentPerformance.setPerformanceDate(LocalDate.now()); // Usar LocalDate

            agentPerformancesList.add(agentPerformance);
        }

        agentPerformanceRepository.saveAll(agentPerformancesList);
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
}
