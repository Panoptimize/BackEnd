package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.agent.AgentDetailsDTO;
import com.itesm.panoptimize.dto.agent.IdentityInfoDTO;
import com.itesm.panoptimize.dto.dashboard.AWSObjDTO;
import com.itesm.panoptimize.dto.agent.AgentListDTO;
import com.itesm.panoptimize.dto.dashboard.DashboardDTO;
import com.itesm.panoptimize.dto.dashboard.DashboardFiltersDTO;
import com.itesm.panoptimize.dto.performance.PerformanceDTO;
import com.itesm.panoptimize.service.CalculatePerformanceService;
import com.itesm.panoptimize.repository.NotificationRepository;
import com.itesm.panoptimize.util.Constants;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.identity.spi.Identity;
import software.amazon.awssdk.services.connect.ConnectClient;
import software.amazon.awssdk.services.connect.model.*;


import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AgentListService {
    private final ConnectClient connectClient;


    @Autowired
    public AgentListService(ConnectClient connectClient) {
        this.connectClient = connectClient;
    }

    private AgentDetailsDTO mapToAgentDTO(User user){
        if (user == null){
            return null;
        }
        AgentDetailsDTO userDTO = new AgentDetailsDTO();
        userDTO.setId(user.id());
        userDTO.setLastModifiedTime(user.lastModifiedTime());
        userDTO.setRoutingProfileId(user.routingProfileId());
        userDTO.setUsername(user.username());

        IdentityInfoDTO identityInfoDTO = new IdentityInfoDTO();
        identityInfoDTO.setEmail(user.identityInfo().email());
        identityInfoDTO.setMobile(user.identityInfo().mobile());
        identityInfoDTO.setFirstName(user.identityInfo().firstName());
        identityInfoDTO.setLastName(user.identityInfo().lastName());
        userDTO.setIdentityInfo(identityInfoDTO);

        return userDTO;
    }


    /**
     * Similar to Dashboard Filter.
     * */
    public DashboardFiltersDTO getAgentList(String instanceId) {

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

        return dashboardFiltersDTO;
    }

    public AgentDetailsDTO getAgentDetails(String agentId,String instanceId){

        DescribeUserResponse describeUserResponse = connectClient.describeUser(DescribeUserRequest.builder()
                .instanceId(instanceId)
                .userId(agentId).build());
        return mapToAgentDTO(describeUserResponse.user());
    }



    public Mono<List<AgentListDTO>> getAllAgents(String instanceId) {
        ListUsersRequest request = ListUsersRequest.builder()
                .instanceId(instanceId)
                .build();

        try {
            ListUsersResponse response = connectClient.listUsers(request);
            List<AgentListDTO> agents = response.userSummaryList().stream()
                    .map(userSummary -> getAgentListDetails(instanceId, userSummary.id()))
                    .collect(Collectors.toList());

            return Mono.just(agents);
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Failed to retrieve agents", e));
        }
    }

    private AgentListDTO getAgentListDetails(String instanceId, String userId) {
        DescribeUserRequest describeRequest = DescribeUserRequest.builder()
                .instanceId(instanceId)
                .userId(userId)
                .build();

        DescribeUserResponse describeResponse = connectClient.describeUser(describeRequest);
        User user = describeResponse.user();

        System.out.println("User Details:");
        System.out.println(user.toString());




        AgentListDTO agent = new AgentListDTO(user.id(), user.username(),getAgentCurrentState(instanceId, user.routingProfileId()),getWorkspaceInfo(instanceId, user.routingProfileId()), instanceId);

        return agent;
    }

    private String getWorkspaceInfo(String instanceId, String routingProfileId) {
        if (routingProfileId == null || routingProfileId.isEmpty()) {
            return "No routing profile assigned";
        }

        DescribeRoutingProfileRequest routingProfileRequest = DescribeRoutingProfileRequest.builder()
                .instanceId(instanceId)
                .routingProfileId(routingProfileId)
                .build();

        DescribeRoutingProfileResponse routingProfileResponse = connectClient.describeRoutingProfile(routingProfileRequest);
        if (routingProfileResponse.routingProfile() != null) {
            System.out.println("Routing Profile Details:");
            System.out.println(routingProfileResponse.routingProfile().toString());

            return routingProfileResponse.routingProfile().name();
        } else {
            return "Unknown routing profile";
        }
    }

    private String getAgentCurrentState(String instanceId, String routingProfileId) {
        if (routingProfileId == null || routingProfileId.isEmpty()) {
            return "No routing profile assigned";
        }

        Filters filters = Filters.builder()
                .routingProfiles(Collections.singletonList(routingProfileId))
                .build();

        GetCurrentMetricDataRequest metricDataRequest = GetCurrentMetricDataRequest.builder()
                .instanceId(instanceId)
                .filters(filters)
                .currentMetrics(
                        List.of(
                                CurrentMetric.builder().name(CurrentMetricName.AGENTS_AVAILABLE).unit(Unit.COUNT).build(),
                                CurrentMetric.builder().name(CurrentMetricName.AGENTS_ON_CALL).unit(Unit.COUNT).build(),
                                CurrentMetric.builder().name(CurrentMetricName.AGENTS_AFTER_CONTACT_WORK).unit(Unit.COUNT).build()
                        )
                )
                .build();

        try {
            GetCurrentMetricDataResponse metricDataResponse = connectClient.getCurrentMetricData(metricDataRequest);

            // Imprime los detalles de las métricas actuales del agente
            System.out.println("Metric Data Request:");
            System.out.println(metricDataRequest);
            System.out.println("Agent Metric Data:");
            System.out.println(metricDataResponse);



            if (metricDataResponse.metricResults().isEmpty()) {
                return "Unknown state";
            } else {
                for (CurrentMetricResult result : metricDataResponse.metricResults()) {
                    for (CurrentMetricData metricData : result.collections()) {
                        if (metricData.metric().name().equals(CurrentMetricName.AGENTS_AVAILABLE)) {
                            if (metricData.value() > 0) {
                                return "Available";
                            }
                        } else if (metricData.metric().name().equals(CurrentMetricName.AGENTS_ON_CALL)) {
                            if (metricData.value() > 0) {
                                return "On Call";
                            }
                        } else if (metricData.metric().name().equals(CurrentMetricName.AGENTS_AFTER_CONTACT_WORK)) {
                            if (metricData.value() > 0) {
                                return "After Contact Work";
                            }
                        }
                    }
                }
                return "Offline";
            }
        } catch (Exception e) {
            return "Error retrieving agent state";
        }
    }
}
