package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.performance.AgentPerformanceDTO;
import com.itesm.panoptimize.model.AgentPerformance;
import com.itesm.panoptimize.repository.AgentPerformanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.connect.ConnectClient;
import software.amazon.awssdk.services.connect.model.DescribeUserRequest;
import software.amazon.awssdk.services.connect.model.DescribeUserResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for calculating agent performance.
 */
@Service
public class CalculatePerformanceService {

    private final ConnectClient connectClient;
    private final AgentPerformanceRepository agentPerformanceRepository;

    /**
     * Constructor with dependency injection.
     *
     * @param connectClient              AWS Connect client for interacting with Amazon Connect
     * @param agentPerformanceRepository Repository for accessing agent performance data
     */
    @Autowired
    public CalculatePerformanceService(ConnectClient connectClient, AgentPerformanceRepository agentPerformanceRepository) {
        this.connectClient = connectClient;
        this.agentPerformanceRepository = agentPerformanceRepository;
    }

    /**
     * Retrieves the performance data for agents between two dates.
     *
     * @param startDate Start date for the performance data
     * @param endDate   End date for the performance data
     * @param instanceId AWS Connect instance ID
     * @return List of AgentPerformanceDTO containing performance scores
     */
    public List<AgentPerformanceDTO> getPerformances(Date startDate, Date endDate, String instanceId) {
        Instant startInstant = startDate.toInstant();
        Instant endInstant = endDate.toInstant();

        List<AgentPerformance> performances = agentPerformanceRepository.findPerformancesBetweenDates(startInstant, endInstant);


        Map<String, List<AgentPerformance>> groupedByAgent = performances.stream()
                .collect(Collectors.groupingBy(performance -> performance.getAgent().getConnectId()));

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

    /**
     * Calculates the performance score for an agent based on various metrics.
     *
     * @param avgHandleTime              Average handle time
     * @param avgAfterContactWorkTime    Average after contact work time
     * @param avgHoldTime                Average hold time
     * @param avgAbandonTime             Average abandon time
     * @return Performance score as a double
     */
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

    /**
     * Retrieves the name of an agent by their ID from AWS Connect.
     *
     * @param agentId    ID of the agent
     * @param instanceId AWS Connect instance ID
     * @return Name of the agent as a String
     */
    private String getAgentNameById(String agentId, String instanceId) {
        DescribeUserRequest describeUserRequest = DescribeUserRequest.builder()
                .instanceId(instanceId)
                .userId(agentId)
                .build();

        DescribeUserResponse describeUserResponse = connectClient.describeUser(describeUserRequest);

        return describeUserResponse.user().username();
    }
}
