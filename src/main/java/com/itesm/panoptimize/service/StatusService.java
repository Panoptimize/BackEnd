package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.agent.StatusDTO;
import software.amazon.awssdk.services.connect.ConnectClient;
import software.amazon.awssdk.services.connect.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.connect.paginators.ListUsersIterable;

import java.util.*;
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
    public List<StatusDTO> getActiveAgents(String instanceId) {
        List<String> queueIds = getAllQueueIds(instanceId);
        if (queueIds.isEmpty()) {
            throw new RuntimeException("No queues found for instance: " + instanceId);
        }

        List<CurrentMetricName> metricNames = List.of(
                CurrentMetricName.AGENTS_ONLINE,
                CurrentMetricName.AGENTS_AVAILABLE,
                CurrentMetricName.AGENTS_AFTER_CONTACT_WORK,
                CurrentMetricName.AGENTS_ON_CONTACT
        );

        List<CurrentMetric> metrics = metricNames.stream()
                .map(metricName -> CurrentMetric.builder().name(metricName).unit(Unit.COUNT).build())
                .collect(Collectors.toList());

        GetCurrentMetricDataRequest request = GetCurrentMetricDataRequest.builder()
                .instanceId(instanceId)
                .filters(Filters.builder().queues(queueIds).build())
                .currentMetrics(metrics)
                .build();

        try {
            GetCurrentMetricDataResponse response = connectClient.getCurrentMetricData(request);

            Map<String, Double> metricValues = metricNames.stream()
                    .collect(Collectors.toMap(
                            CurrentMetricName::toString,
                            metricName -> 0.0
                    ));

            response.metricResults().stream()
                    .flatMap(result -> result.collections().stream())
                    .forEach(collection -> metricValues.put(collection.metric().nameAsString(), collection.value()));

            List<StatusDTO> result = new ArrayList<>();
            double agents = getUserCount(instanceId);
            result.add(new StatusDTO("AGENTS", agents));
            result.add(new StatusDTO("AGENTS_ONLINE", metricValues.get(CurrentMetricName.AGENTS_ONLINE.toString())));
            result.add(new StatusDTO("AGENTS_AVAILABLE", metricValues.get(CurrentMetricName.AGENTS_AVAILABLE.toString())));
            result.add(new StatusDTO("AGENTS_OFFLINE", agents - metricValues.get(CurrentMetricName.AGENTS_ONLINE.toString())));

            return result;
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
     * Retrieves the count of users for the specified instance.
     *
     * @param instanceId the ID of the Amazon Connect instance
     * @return the count of users
     */
    private int getUserCount(String instanceId) {
        ListUsersRequest listUsersRequest = ListUsersRequest.builder()
                .instanceId(instanceId)
                .build();

        ListUsersIterable listUsersIterable = connectClient.listUsersPaginator(listUsersRequest);

        int userCount = 0;
        for (ListUsersResponse response : listUsersIterable) {
            userCount += response.userSummaryList().size();
        }

        return userCount;
    }
}
