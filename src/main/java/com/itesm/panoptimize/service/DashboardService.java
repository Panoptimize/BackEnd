package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.contact.*;
import com.itesm.panoptimize.dto.dashboard.*;

import com.itesm.panoptimize.dto.dashboard.AWSObjDTO;
import com.itesm.panoptimize.dto.dashboard.DashboardFiltersDTO;
import com.itesm.panoptimize.dto.dashboard.MetricResponseDTO;

import com.itesm.panoptimize.dto.performance.AgentPerformanceDTO;
import com.itesm.panoptimize.model.Notification;
import com.itesm.panoptimize.repository.NotificationRepository;
import com.itesm.panoptimize.util.Constants;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.connect.ConnectClient;
import software.amazon.awssdk.services.connect.model.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.itesm.panoptimize.util.Constants.roundMetric;

@Service
public class DashboardService {
    private static final long INTERVAL_CHECK = 3;

    private final ConnectClient connectClient;
    private final NotificationRepository notificationRepository;

    private final CalculatePerformanceService calculatePerformanceService;

    public DashboardService(ConnectClient connectClient, NotificationRepository notificationRepository, CalculatePerformanceService calculatePerformanceService){
        this.connectClient = connectClient;
        this.notificationRepository = notificationRepository;
        this.calculatePerformanceService = calculatePerformanceService;
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
    private GetMetricDataV2Response getKPIs(String instanceId, @NotNull DashboardDTO dashboardDTO,
                                            @NotNull List<MetricV2> metrics,
                                            IntervalDetails intervalDetails) {

        Instant startTime = dashboardDTO.getStartDate().toInstant();
        Instant endTime = dashboardDTO.getEndDate().toInstant();

        // Set up filters
        List<FilterV2> filters = new ArrayList<>();

        if (!dashboardDTO.getRoutingProfiles().isEmpty()) {
            FilterV2 routingProfileFilter = FilterV2.builder()
                    .filterKey("ROUTING_PROFILE")
                    .filterValues(dashboardDTO.getRoutingProfiles())
                    .build();
            filters.add(routingProfileFilter);
        }


        return connectClient.getMetricDataV2(GetMetricDataV2Request.builder()
                .startTime(startTime)
                .endTime(endTime)
                .resourceArn(Constants.BASE_ARN + ":instance/" + instanceId)
                .filters(filters)
                .metrics(metrics)
                .interval(intervalDetails)
                .build());
    }
    public MetricResponseDTO getMetricsData(String instanceId, DashboardDTO dashboardDTO) {
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
        GetMetricDataV2Response response = getKPIs(instanceId, dashboardDTO, metricList, null);

        Map<String, Double> metricsData = new HashMap<>();

        for (MetricResultV2 metricData : response.metricResults()) {
            List<MetricDataV2> metrics = metricData.collections();
            for (MetricDataV2 metric : metrics) {
                metricsData.put(metric.metric().name(), metric.value());
            }
        }

        double averageSpeedOfAnswer;
        if(metricsData.get("CONTACTS_HANDLED") == null) {
            averageSpeedOfAnswer = 0.0;
        } else {
            averageSpeedOfAnswer = metricsData.get("SUM_HANDLE_TIME") / metricsData.get("CONTACTS_HANDLED");
        }
        Double averageHoldTimeRounded = roundMetric(metricsData.get("AVG_HOLD_TIME"));
        Double percentageFirstContactResolution = roundMetric(metricsData.get("PERCENT_CASES_FIRST_CONTACT_RESOLVED"));
        Double abandonmentRateRounded = roundMetric(metricsData.get("ABANDONMENT_RATE"));
        Double serviceLevelRounded = roundMetric(metricsData.get("SERVICE_LEVEL"));
        Double scheduleAdherenceRounded = roundMetric(metricsData.get("AGENT_SCHEDULE_ADHERENCE"));
        Double averageSpeedOfAnswerRounded = roundMetric(averageSpeedOfAnswer);

        return new
                MetricResponseDTO(
                averageHoldTimeRounded,
                percentageFirstContactResolution,
                abandonmentRateRounded,
                serviceLevelRounded,
                scheduleAdherenceRounded,
                averageSpeedOfAnswerRounded
        );
    }

    // Get the current number of agents on each channel
    public Mono<MetricResultsDTO> getMetricResults(String instanceId, @NotNull DashboardDTO dashboardDTO) {
        String routingProfile = dashboardDTO.getRoutingProfiles().get(0);
        List<Channel> channels = Arrays.asList(Channel.VOICE, Channel.CHAT);
        List<String> queueIds = getAllQueueIds(instanceId);
        Filters filters = Filters.builder()
                .queues(queueIds)
                .channels(channels)
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

    private List<String> getAllQueueIds(String instanceId) {
        ListQueuesRequest listQueuesRequest = ListQueuesRequest.builder()
                .instanceId(instanceId)
                .build();

        ListQueuesResponse listQueuesResponse = connectClient.listQueues(listQueuesRequest);

        return listQueuesResponse.queueSummaryList().stream()
                .map(QueueSummary::id)
                .collect(Collectors.toList());
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
        notificationToUpdate.setCreatedAt(notification.getCreatedAt());
        notificationToUpdate.setDescription(notification.getDescription());
        notificationToUpdate.setUser(notification.getUser());
        notificationRepository.save(notificationToUpdate);
        return notificationToUpdate;
    }

    public DashboardFiltersDTO getFilters(String instanceId) {
        // Get instance creation date
        Instant getCreationTime = connectClient.describeInstance(DescribeInstanceRequest.builder()
                .instanceId(instanceId)
                .build()).instance().createdTime();

        // Get routing profiles
        List<RoutingProfileSummary> routingProfiles = connectClient.listRoutingProfiles(ListRoutingProfilesRequest.builder()
                .instanceId(instanceId)
                .build())
                .routingProfileSummaryList();

        List<AWSObjDTO> routingProfilesDTO = new ArrayList<>();
        for (RoutingProfileSummary routingProfile : routingProfiles) {
            AWSObjDTO routingProfileDTO = new AWSObjDTO();
            routingProfileDTO.setId(routingProfile.id());
            routingProfileDTO.setName(routingProfile.name());
            routingProfilesDTO.add(routingProfileDTO);
        }

        // Convert instant to LocalDate
        LocalDate instanceCreationDate = getCreationTime.atZone(ZoneId.systemDefault()).toLocalDate();

        DashboardFiltersDTO dashboardFiltersDTO = new DashboardFiltersDTO();
        dashboardFiltersDTO.setWorkspaces(routingProfilesDTO);
        dashboardFiltersDTO.setInstanceCreationDate(instanceCreationDate);

        return dashboardFiltersDTO;
    }

    public ActivityResponseDTO getActivity(String instanceId, DashboardDTO dashboardDTO) {
        IntervalPeriod interval;

        if(ChronoUnit.DAYS.between(
                dashboardDTO.getStartDate().toInstant(),
                dashboardDTO.getEndDate().toInstant()) > INTERVAL_CHECK ) {
            interval = IntervalPeriod.DAY;
        } else {
            interval = IntervalPeriod.HOUR;
        }

        List<MetricV2> metricList = new ArrayList<>();
        MetricV2 totalContacts = MetricV2.builder()
                .name("CONTACTS_HANDLED")
                .build();
        metricList.add(totalContacts);

        GetMetricDataV2Response response = getKPIs(instanceId, dashboardDTO,
                metricList,
                IntervalDetails.builder().intervalPeriod(interval).build());

        ActivityResponseDTO activityResponseDTO = new ActivityResponseDTO();
        List<ActivityDTO> activities = new ArrayList<>();

        for (MetricResultV2 metricData : response.metricResults()) {
            ActivityDTO activityDTO = new ActivityDTO();
            MetricDataV2 metric = metricData.collections().get(0);

            activityDTO.setValue(metric.value());
            activityDTO.setStartTime(metricData.metricInterval().startTime());

            activities.add(activityDTO);
        }
        activityResponseDTO.setActivities(activities);

        return activityResponseDTO;
    }

    public CombinedMetricsDTO getDashboardData(String instanceId, DashboardDTO dashboardDTO) {
        MetricResultsDTO metricResults = getMetricResults(instanceId, dashboardDTO).block();
        Map<String, Integer> values = extractValues(metricResults);

        CombinedMetricsDTO combinedMetrics = new CombinedMetricsDTO();
        combinedMetrics.setVoice(values.get("voice"));
        combinedMetrics.setChat(values.get("chat"));

        MetricResponseDTO metricData = getMetricsData(instanceId, dashboardDTO);
        combinedMetrics.setMetrics(metricData);

        ActivityResponseDTO activity = getActivity(instanceId, dashboardDTO);
        combinedMetrics.setActivities(activity);

        List<AgentPerformanceDTO> performanceData = calculatePerformanceService.getPerformances(
                dashboardDTO.getStartDate(),
                dashboardDTO.getEndDate(),
                instanceId,
                dashboardDTO.getRoutingProfiles()
        );
        combinedMetrics.setPerformanceData(performanceData);

        return combinedMetrics;
    }




}