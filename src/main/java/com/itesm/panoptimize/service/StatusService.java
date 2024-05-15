package com.itesm.panoptimize.service;

import software.amazon.awssdk.services.connect.ConnectClient;
import software.amazon.awssdk.services.connect.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatusService {

    private final ConnectClient connectClient;

    @Autowired
    public StatusService(ConnectClient connectClient) {
        this.connectClient = connectClient;
    }

    /**
     * Retrieves the active agent statuses for the specified instance.
     *
     * @param instanceId the ID of the Amazon Connect instance
     * @return a list of active agent statuses
     */
    public List<AgentStatus> getActiveAgents(String instanceId) {
        List<String> queueIds = getAllQueueIds(instanceId);
        if (queueIds.isEmpty()) {
            throw new RuntimeException("No queues found for instance: " + instanceId);
        }

        List<CurrentMetric> metrics = List.of(
                CurrentMetric.builder().name(CurrentMetricName.AGENTS_ONLINE).unit(Unit.COUNT).build(),
                CurrentMetric.builder().name(CurrentMetricName.AGENTS_AVAILABLE).unit(Unit.COUNT).build(),
                CurrentMetric.builder().name(CurrentMetricName.AGENTS_AFTER_CONTACT_WORK).unit(Unit.COUNT).build(),
                CurrentMetric.builder().name(CurrentMetricName.AGENTS_ON_CALL).unit(Unit.COUNT).build()

        );

        GetCurrentMetricDataRequest request = GetCurrentMetricDataRequest.builder()
                .instanceId(instanceId)
                .filters(Filters.builder().queues(queueIds).build())
                .currentMetrics(metrics)
                .build();

        try {
            GetCurrentMetricDataResponse response = connectClient.getCurrentMetricData(request);

            return response.metricResults().stream()
                    .flatMap(result -> result.collections().stream())
                    .filter(collection -> collection.metric().name().equals(CurrentMetricName.AGENTS_ONLINE) ||
                            collection.metric().name().equals(CurrentMetricName.AGENTS_AVAILABLE))
                    .map(collection -> new AgentStatus(collection.metric().nameAsString(), collection.value()))
                    .collect(Collectors.toList());
        } catch (ConnectException e) {
            // Log the exception and rethrow or handle it as needed
            throw new RuntimeException("Failed to retrieve agent metrics", e);
        }
    }

    /**
     * Retrieves the IDs of all queues for the specified instance.
     *
     * @param instanceId the ID of the Amazon Connect instance
     * @return a list of queue IDs
     */
    private List<String> getAllQueueIds(String instanceId) {
        ListQueuesRequest listQueuesRequest = ListQueuesRequest.builder()
                .instanceId(instanceId)
                .build();

        ListQueuesResponse listQueuesResponse = connectClient.listQueues(listQueuesRequest);

        return listQueuesResponse.queueSummaryList().stream()
                .map(QueueSummary::id)
                .collect(Collectors.toList());
    }

    /**
     * Inner class representing the status of an agent.
     */
    public static class AgentStatus {
        private final String metricName;
        private final Double metricValue;

        public AgentStatus(String metricName, Double metricValue) {
            this.metricName = metricName;
            this.metricValue = metricValue;
        }

        public String getMetricName() {
            return metricName;
        }

        public Double getMetricValue() {
            return metricValue;
        }
    }
}
