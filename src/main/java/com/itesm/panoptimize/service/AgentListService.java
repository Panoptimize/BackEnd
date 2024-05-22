package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.agent.AgentDetailsDTO;
import com.itesm.panoptimize.dto.agent.IdentityInfoDTO;
import com.itesm.panoptimize.dto.dashboard.AWSObjDTO;
import com.itesm.panoptimize.dto.dashboard.DashboardDTO;
import com.itesm.panoptimize.dto.dashboard.DashboardFiltersDTO;
import com.itesm.panoptimize.repository.NotificationRepository;
import com.itesm.panoptimize.util.Constants;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import software.amazon.awssdk.identity.spi.Identity;
import software.amazon.awssdk.services.connect.ConnectClient;
import software.amazon.awssdk.services.connect.model.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        dashboardFiltersDTO.setAgents(agentsDTO);

        return dashboardFiltersDTO;
    }

    public AgentDetailsDTO getAgentDetails(String agentId,String instanceId){

        DescribeUserResponse describeUserResponse = connectClient.describeUser(DescribeUserRequest.builder()
                .instanceId(instanceId)
                .userId(agentId).build());
        /**
         * AgentDetailConstructionForDelivery
         * */

        AgentDetailsDTO agentDetailsDTO = mapToAgentDTO(describeUserResponse.user());
        System.out.println(agentDetailsDTO);
        return agentDetailsDTO;
    }


}
