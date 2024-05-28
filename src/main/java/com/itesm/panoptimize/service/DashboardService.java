package com.itesm.panoptimize.service;


import com.itesm.panoptimize.dto.dashboard.AWSObjDTO;
import com.itesm.panoptimize.dto.dashboard.DashboardFiltersDTO;
import com.itesm.panoptimize.dto.dashboard.MetricResponseDTO;

import com.itesm.panoptimize.dto.contact.*;

import com.itesm.panoptimize.model.Notification;
import com.itesm.panoptimize.repository.NotificationRepository;
import com.itesm.panoptimize.util.Constants;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.itesm.panoptimize.dto.dashboard.DashboardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.services.connect.ConnectClient;
import software.amazon.awssdk.services.connect.model.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    private final WebClient webClient;
    private final ConnectClient connectClient;
    private final NotificationRepository notificationRepository;

    @Autowired
    public DashboardService(WebClient.Builder webClientBuilder, ConnectClient connectClient, NotificationRepository notificationRepository){
        this.webClient = webClientBuilder.baseUrl("http://localhost:8000").build();
        this.connectClient = connectClient;
        this.notificationRepository = notificationRepository;
    }

    /**
     * Get KPIs from Amazon Connect. These KPIs are the following:
     * - Service Level
     * - Average Hold Time
     * - Average Speed of Answer
     * - Schedule Adherence
     * - First Contact Resolution
     * @param dashboardDTO is the DTO that contains the filters to get the KPIs
     * @return a list of KPIs
     */
    private GetMetricDataV2Response getKPIs(@NotNull DashboardDTO dashboardDTO, List<MetricV2> metrics) {
        String instanceId = dashboardDTO.getInstanceId();

        Instant startTime = dashboardDTO.getStartDate().toInstant();
        Instant endTime = dashboardDTO.getEndDate().toInstant();

        // Set up filters
        List<FilterV2> filters = new ArrayList<>();

        if (dashboardDTO.getRoutingProfiles().length > 0) {
            FilterV2 routingProfileFilter = FilterV2.builder()
                    .filterKey("ROUTING_PROFILE")
                    .filterValues(Arrays.asList(dashboardDTO.getRoutingProfiles()))
                    .build();
            filters.add(routingProfileFilter);
        }

        if (dashboardDTO.getAgents().length > 0) {
            FilterV2 queueFilter = FilterV2.builder()
                    .filterKey("AGENT")
                    .filterValues(Arrays.asList(dashboardDTO.getAgents()))
                    .build();
            filters.add(queueFilter);
        }


        return connectClient.getMetricDataV2(GetMetricDataV2Request.builder()
                .startTime(startTime)
                .endTime(endTime)
                .resourceArn(Constants.BASE_ARN + ":instance/" + instanceId)
                .filters(filters)
                .metrics(metrics)
                .build());
    }
    public MetricResponseDTO getMetricsData(DashboardDTO dashboardDTO) {
        // Set up metrics
        List<MetricV2> metricList = new ArrayList<>();

        // Service Level
        MetricV2 serviceLevel = MetricV2.builder()
                .name("SERVICE_LEVEL")
                .threshold(ThresholdV2.builder()
                        .comparison("LT")
                        .thresholdValue(80.0)
                        .build())
                .build();

        metricList.add(serviceLevel);

        // Abandonment rate
        MetricV2 abandonmentRate = MetricV2.builder()
                .name("ABANDONMENT_RATE")
                .build();

        metricList.add(abandonmentRate);

        // Average Hold Time
        MetricV2 averageHoldTime = MetricV2.builder()
                .name("AVG_HOLD_TIME")
                .build();

        metricList.add(averageHoldTime);

        // Schedule Adherence
        MetricV2 scheduleAdherence = MetricV2.builder()
                .name("AGENT_SCHEDULE_ADHERENCE")
                .build();

        metricList.add(scheduleAdherence);

        // Average speed of answer
        // First we need total handle time and total contacts
        MetricV2 totalHandleTime = MetricV2.builder()
                .name("SUM_HANDLE_TIME")
                .build();
        metricList.add(totalHandleTime);

        MetricV2 totalContacts = MetricV2.builder()
                .name("CONTACTS_HANDLED")
                .build();
        metricList.add(totalContacts);

        // Occupancy
        MetricV2 occupancy = MetricV2.builder()
                .name("AGENT_OCCUPANCY")
                .build();
        metricList.add(occupancy);

        // First Contact Resolution
        MetricV2 firstContactResolution = MetricV2.builder()
                .name("PERCENT_CASES_FIRST_CONTACT_RESOLVED")
                .build();

        metricList.add(firstContactResolution);
        GetMetricDataV2Response response = getKPIs(dashboardDTO, metricList);

        Map<String, Double> metricsData = new HashMap<>();

        for (MetricResultV2 metricData : response.metricResults()) {
            List<MetricDataV2> metrics = metricData.collections();
            for (MetricDataV2 metric : metrics) {
                metricsData.put(metric.metric().name(), metric.value());
            }
        }

        double averageSpeedOfAnswer = metricsData.get("SUM_HANDLE_TIME") / metricsData.get("CONTACTS_HANDLED");

        MetricResponseDTO metricResponseDTO = new
                MetricResponseDTO(
                metricsData.get("AVG_HOLD_TIME"),
                metricsData.get("PERCENT_CASES_FIRST_CONTACT_RESOLVED"),
                metricsData.get("ABANDONMENT_RATE"),
                metricsData.get("SERVICE_LEVEL"),
                metricsData.get("AGENT_SCHEDULE_ADHERENCE"),
                averageSpeedOfAnswer
        );

        return metricResponseDTO;
    }

    // Get the current number of agents on each channel
    public Mono<MetricResultsDTO> getMetricResults(@NotNull DashboardDTO dashboardDTO) {
        String instanceId = dashboardDTO.getInstanceId();
        String routingProfile = dashboardDTO.getRoutingProfiles()[0];
        List<Channel> channels = Arrays.asList(Channel.VOICE, Channel.CHAT);
        Filters filters = Filters.builder()
                .routingProfiles(Collections.singletonList(routingProfile))
                .channels(channels)
                .routingStepExpressions(Collections.emptyList())
                .build();

        GetCurrentMetricDataRequest request = GetCurrentMetricDataRequest.builder()
                .instanceId(instanceId)
                .filters(filters)
                .currentMetrics(CurrentMetric.builder()
                        .name(CurrentMetricName.AGENTS_ONLINE)
                        .unit(Unit.COUNT)
                        .build())
                .groupings(Grouping.CHANNEL)
                .build();

        try {
            GetCurrentMetricDataResponse response = connectClient.getCurrentMetricData(request);
            MetricResultsDTO result = convertToDTO(response);
            return Mono.just(result);
        } catch (ConnectException e) {
            return Mono.empty();
        }
    }

    //Converts the raw channel response into the DTO
    private MetricResultsDTO convertToDTO(GetCurrentMetricDataResponse response) {
        MetricResultsDTO dto = new MetricResultsDTO();


        List<MetricResultDTO> metricResultDTOs = response.metricResults().stream().map(metricResult -> {
            MetricResultDTO metricResultDTO = new MetricResultDTO();


            DimensionDTO dimensionDTO = new DimensionDTO();
            dimensionDTO.setChannel(metricResult.dimensions().channel().toString());
            metricResultDTO.setDimensions(dimensionDTO);


            List<CollectionDTO> collectionDTOs = metricResult.collections().stream().map(collection -> {
                CollectionDTO collectionDTO = new CollectionDTO();


                MetricDTO metricDTO = new MetricDTO();
                metricDTO.setName(collection.metric().name().toString());
                metricDTO.setUnit(collection.metric().unit().toString());
                collectionDTO.setMetric(metricDTO);


                collectionDTO.setValue(collection.value().intValue());

                return collectionDTO;
            }).collect(Collectors.toList());

            metricResultDTO.setCollections(collectionDTOs);
            return metricResultDTO;
        }).collect(Collectors.toList());

        dto.setMetricResults(metricResultDTOs);
        dto.setNextToken(response.nextToken());
        return dto;
    }

    //Extracts the number of agents on each channel and returns it as a map with the desired format
    public Map<String, Integer> extractValues(MetricResultsDTO metricResults) {
        Map<String, Integer> values = new HashMap<>();
        if (metricResults == null || metricResults.getMetricResults() == null) {
            return values;
        }

        for (MetricResultDTO metricResult : metricResults.getMetricResults()) {
            if (metricResult.getCollections() != null) {
                for (CollectionDTO collection : metricResult.getCollections()) {
                    String channel = metricResult.getDimensions().getChannel().toLowerCase();
                    values.put(channel, collection.getValue());
                }
            }
        }

        return values;
    }
    public List<Notification> getNotifications() {
        return notificationRepository.findAll();
    }
    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }
    public Notification addNotification(Notification notification) {
        return notificationRepository.save(notification);
    }
    public boolean deleteNotification(Long id) {
        boolean exists = notificationRepository.existsById(id);
        notificationRepository.deleteById(id);
        return exists;
    }
    public Notification updateNotification(Long id, Notification notification) {
        Notification notificationToUpdate = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(
                        "Notification with id " + id + " does not exist"
                ));
        notificationToUpdate.setDateTime(notification.getDateTime());
        notificationToUpdate.setDescription(notification.getDescription());
        notificationToUpdate.setUser(notification.getUser());
        notificationToUpdate.setContact(notification.getContact());
        notificationRepository.save(notificationToUpdate);
        return notificationToUpdate;
    }

    public DashboardFiltersDTO getFilters(String instanceId) {
        // Get routing profiles
        List<RoutingProfileSummary> routingProfiles = connectClient.listRoutingProfiles(ListRoutingProfilesRequest.builder()
                .instanceId(instanceId)
                .build())
                .routingProfileSummaryList();

        // Get agents
        List<UserSummary> agents = connectClient.listUsers(ListUsersRequest.builder()
                .instanceId(instanceId)
                .build())
                .userSummaryList();

        List<AWSObjDTO> routingProfilesDTO = new ArrayList<>();
        for (RoutingProfileSummary routingProfile : routingProfiles) {
            AWSObjDTO routingProfileDTO = new AWSObjDTO();
            routingProfileDTO.setId(routingProfile.id());
            routingProfileDTO.setName(routingProfile.name());
            routingProfilesDTO.add(routingProfileDTO);
        }

        List<AWSObjDTO> agentsDTO = new ArrayList<>();
        for (UserSummary agent : agents) {
            AWSObjDTO agentDTO = new AWSObjDTO();
            agentDTO.setId(agent.id());
            agentDTO.setName(agent.username());
            agentsDTO.add(agentDTO);
        }

        DashboardFiltersDTO dashboardFiltersDTO = new DashboardFiltersDTO();
        dashboardFiltersDTO.setWorkspaces(routingProfilesDTO);
        dashboardFiltersDTO.setAgents(agentsDTO);

        return dashboardFiltersDTO;
    }
}