package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.performance.AgentPerformanceDTO;
import com.itesm.panoptimize.model.AgentPerformance;
import com.itesm.panoptimize.repository.AgentPerformanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.connect.ConnectClient;
import software.amazon.awssdk.services.connect.model.DescribeUserRequest;
import software.amazon.awssdk.services.connect.model.DescribeUserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CalculatePerformanceService {

    private static final Logger logger = LoggerFactory.getLogger(CalculatePerformanceService.class);

    private final ConnectClient connectClient;
    private final AgentPerformanceRepository agentPerformanceRepository;

    @Autowired
    public CalculatePerformanceService(ConnectClient connectClient, AgentPerformanceRepository agentPerformanceRepository) {
        this.connectClient = connectClient;
        this.agentPerformanceRepository = agentPerformanceRepository;
    }

    public List<AgentPerformanceDTO> getPerformances(LocalDate startDate, LocalDate endDate, String instanceId) {

        List<AgentPerformance> performances = agentPerformanceRepository.findByPerformanceDateBetween(startDate, endDate);

        Map<String, List<AgentPerformance>> groupedByAgent = performances.stream()
                .collect(Collectors.groupingBy(AgentPerformance::getAgentId));

        List<AgentPerformanceDTO> result = new ArrayList<>();

        for (Map.Entry<String, List<AgentPerformance>> entry : groupedByAgent.entrySet()) {
            String agentId = entry.getKey();
            List<AgentPerformance> agentPerformances = entry.getValue();

            List<Double> performanceScores = agentPerformances.stream()
                    .map(performance -> calculateAgentPerformanceScore(
                            performance.getAvgHandleTime(),
                            performance.getAvgAfterContactWorkTime(),
                            performance.getAvgHoldTime(),
                            performance.getAvgAbandonTime()))
                    .collect(Collectors.toList());

            String agentName = getAgentNameById(agentId, instanceId);
            result.add(new AgentPerformanceDTO(agentName, performanceScores));
        }

        return result;
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

    private String getAgentNameById(String agentId, String instanceId) {

        DescribeUserRequest describeUserRequest = DescribeUserRequest.builder()
                .instanceId(instanceId)
                .userId(agentId)
                .build();

        DescribeUserResponse describeUserResponse = connectClient.describeUser(describeUserRequest);

        String username = describeUserResponse.user().username();

        return username;
    }
}
