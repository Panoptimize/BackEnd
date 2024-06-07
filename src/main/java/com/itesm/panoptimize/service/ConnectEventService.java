package com.itesm.panoptimize.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.itesm.panoptimize.dto.agent.AgentCreateDTO;
import com.itesm.panoptimize.dto.agent.AgentUpdateDTO;
import com.itesm.panoptimize.dto.agent.AgentUserDTO;
import com.itesm.panoptimize.dto.contact.ContactDTO;
import com.itesm.panoptimize.dto.contact.CreateContactDTO;
import com.itesm.panoptimize.dto.contactevent.ContactEventDTO;
import com.itesm.panoptimize.dto.event.CreatedUsersDTO;
import com.itesm.panoptimize.dto.event.SyncRequestDTO;
import com.itesm.panoptimize.dto.queue.CreateQueueDTO;
import com.itesm.panoptimize.dto.queue.QueueDTO;
import com.itesm.panoptimize.dto.routingprofile.CreateRoutingProfileDTO;
import com.itesm.panoptimize.dto.routingprofile.RoutingProfileDTO;
import com.itesm.panoptimize.dto.supervisor.SupervisorCreateDTO;
import com.itesm.panoptimize.dto.supervisor.SupervisorUpdateDTO;
import com.itesm.panoptimize.dto.supervisor.SupervisorUserDTO;
import com.itesm.panoptimize.dto.usertype.UserTypeCreateDTO;
import com.itesm.panoptimize.dto.usertype.UserTypeDTO;
import com.itesm.panoptimize.model.Instance;
import com.itesm.panoptimize.repository.InstanceRepository;
import com.itesm.panoptimize.util.Constants;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.connect.ConnectClient;
import software.amazon.awssdk.services.connect.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.google.firebase.auth.AuthErrorCode.USER_NOT_FOUND;

@Service
public class ConnectEventService {
    private final ContactService contactService;
    private final ConnectClient connectClient;
    private final UserService userService;
    private final InstanceRepository instanceRepository;
    private final UserTypeService userTypeService;
    private final RoutingProfileService routingProfileService;
    private final QueueService queueService;
    private final FirebaseAuth firebaseAuth;

    public ConnectEventService(ContactService contactService,
                               ConnectClient connectClient,
                               UserService userService,
                               UserTypeService userTypeService,
                               InstanceRepository instanceRepository,
                               RoutingProfileService routingProfileService,
                               QueueService queueService,
                               FirebaseAuth firebaseAuth) {
        this.contactService = contactService;
        this.connectClient = connectClient;
        this.userService = userService;
        this.userTypeService = userTypeService;
        this.instanceRepository = instanceRepository;
        this.routingProfileService = routingProfileService;
        this.queueService = queueService;
        this.firebaseAuth = firebaseAuth;
    }

    private boolean validateEvent(ContactEventDTO event) {
        return event.getDetail().getAgentInfo() != null;
    }

    public ContactDTO handleConnectEvent(ContactEventDTO event) {
        if (validateEvent(event)) {
            GetContactAttributesResponse responseAttributes = connectClient.getContactAttributes(
                    GetContactAttributesRequest
                            .builder()
                            .instanceId(Constants.extractId(event.getDetail().getInstanceArn()))
                            .initialContactId(event.getDetail().getContactId().toString())
                            .build()
            );

            int satisfaction;

            if (!responseAttributes.attributes().containsKey("Satisfaction")) {
                satisfaction = 3;
            } else {
                satisfaction = Integer.parseInt(responseAttributes.attributes().get("Satisfaction"));
            }

            CreateContactDTO contactDTO = new CreateContactDTO();
            contactDTO.setId(event.getDetail().getContactId().toString());
            contactDTO.setSatisfaction(satisfaction);
            contactDTO.setAgentId(Constants.extractId(event.getDetail().getAgentInfo().getAgentArn()));

            return contactService.createContact(contactDTO);
        }
        return null;
    }

    private CreatedUsersDTO syncUsers(Instance instance,
                                      String supervisorSecurityProfileId,
                                      String userPattern) throws FirebaseAuthException {

        List<AgentUserDTO> agents = new ArrayList<>();
        List<SupervisorUserDTO> supervisors = new ArrayList<>();

        List<UserSummary> userSummaries = connectClient.listUsers(
                ListUsersRequest
                        .builder()
                        .instanceId(instance.getId())
                        .build()
        ).userSummaryList();

        for (UserSummary userSummary : userSummaries) {
            User user = connectClient.describeUser(
                    DescribeUserRequest
                            .builder()
                            .instanceId(instance.getId())
                            .userId(userSummary.id())
                            .build()
            ).user();

            // Decide if an agent or a supervisor will be created or updated
            if (user.securityProfileIds().contains(supervisorSecurityProfileId)) {
                // Check if user is in firebase

                UserRecord userRecord;
                try {
                    userRecord = firebaseAuth.getUserByEmail(user.identityInfo().email());
                }
                catch (FirebaseAuthException e) {
                    if(e.getAuthErrorCode() == USER_NOT_FOUND) {
                        // Create user in firebase with random id
                        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                                .setEmail(user.identityInfo().email())
                                .setDisplayName(user.identityInfo().firstName() + " " + user.identityInfo().lastName())
                                .setEmailVerified(false)
                                .setPassword("ThePassword123")
                                .setDisabled(false);

                        userRecord = firebaseAuth.createUser(request);

                        // Send the email verification only if it contains the user pattern
                        if (user.username().toLowerCase().contains(userPattern)) {
                            firebaseAuth.generateEmailVerificationLink(user.identityInfo().email());
                        }
                    } else {
                        throw e;
                    }
                }

                SupervisorUserDTO supervisorUserDTO = userService.getSupervisorWithConnectId(user.id());

                if (supervisorUserDTO != null) {
                    SupervisorUpdateDTO supervisorUpdateDTO = new SupervisorUpdateDTO();
                    supervisorUpdateDTO.setConnectId(user.id());
                    supervisorUpdateDTO.setEmail(user.identityInfo().email());
                    supervisorUpdateDTO.setFullName(user.identityInfo().firstName() + " " + user.identityInfo().lastName());
                    supervisorUpdateDTO.setCompanyId(instance.getCompany().getId());
                    supervisorUpdateDTO.setFirebaseId(userRecord.getUid());

                    supervisorUserDTO = userService.updateSupervisor(supervisorUserDTO.getId(), supervisorUpdateDTO);
                } else {
                    SupervisorCreateDTO supervisorCreateDTO = getSupervisorCreateDTO(instance, user);

                    supervisorUserDTO = userService.createSupervisor(supervisorCreateDTO);
                }

                supervisors.add(supervisorUserDTO);

            } else {
                // Create agent
                AgentUserDTO agentUserDTO = userService.getAgentWithConnectId(user.id());

                if (agentUserDTO != null) {
                    AgentUpdateDTO agentUserUpdateDTO = new AgentUpdateDTO();
                    agentUserUpdateDTO.setConnectId(user.id());
                    agentUserUpdateDTO.setEmail(user.identityInfo().email());
                    agentUserUpdateDTO.setFullName(user.identityInfo().firstName() + " " + user.identityInfo().lastName());
                    agentUserUpdateDTO.setCompanyId(instance.getCompany().getId());
                    agentUserUpdateDTO.setRoutingProfileId(user.routingProfileId());
                    agentUserDTO = userService.updateAgent(agentUserDTO.getId(), agentUserUpdateDTO);
                } else {
                    AgentCreateDTO agentUserCreateDTO = new AgentCreateDTO();
                    agentUserCreateDTO.setConnectId(user.id());
                    agentUserCreateDTO.setEmail(user.identityInfo().email());
                    agentUserCreateDTO.setFullName(user.identityInfo().firstName() + " " + user.identityInfo().lastName());
                    agentUserCreateDTO.setCompanyId(instance.getCompany().getId());
                    agentUserCreateDTO.setRoutingProfileId(user.routingProfileId());
                    agentUserDTO = userService.createAgent(agentUserCreateDTO);
                }

                agents.add(agentUserDTO);
            }
        }

        CreatedUsersDTO createdUsersDTO = new CreatedUsersDTO();
        createdUsersDTO.setAgents(agents);
        createdUsersDTO.setSupervisors(supervisors);

        return createdUsersDTO;
    }

    private static @NotNull SupervisorCreateDTO getSupervisorCreateDTO(Instance instance, User user) {
        SupervisorCreateDTO supervisorCreateDTO = new SupervisorCreateDTO();
        supervisorCreateDTO.setConnectId(user.id());
        supervisorCreateDTO.setEmail(user.identityInfo().email());
        supervisorCreateDTO.setFullName(user.identityInfo().firstName() + " " + user.identityInfo().lastName());
        supervisorCreateDTO.setCompanyId(instance.getCompany().getId());
        supervisorCreateDTO.setRoutingProfileId(user.routingProfileId());
        return supervisorCreateDTO;
    }

    private void syncRoutingProfileQueues(Instance instance) {
        // Get all the routing profiles
        List<RoutingProfileSummary> routingProfileSummaries = connectClient.listRoutingProfiles(
                ListRoutingProfilesRequest
                        .builder()
                        .instanceId(instance.getId())
                        .build()
        ).routingProfileSummaryList();

        // Get those routing profile queues and start creation
        for (RoutingProfileSummary routingProfileSummary : routingProfileSummaries) {
            List<RoutingProfileQueueConfigSummary> queueConfigSummaries = connectClient.listRoutingProfileQueues(
                    ListRoutingProfileQueuesRequest
                            .builder()
                            .instanceId(instance.getId())
                            .routingProfileId(routingProfileSummary.id())
                            .build()
            ).routingProfileQueueConfigSummaryList();

            // Upsert the routing profile
            RoutingProfileDTO routingProfile = routingProfileService.getRoutingProfile(routingProfileSummary.id());
            if (routingProfile == null) {
                CreateRoutingProfileDTO createRoutingProfileDTO = new CreateRoutingProfileDTO();
                createRoutingProfileDTO.setId(routingProfileSummary.id());
                createRoutingProfileDTO.setName(routingProfileSummary.name());
                routingProfile = routingProfileService.createRoutingProfile(createRoutingProfileDTO);
            }

            for (RoutingProfileQueueConfigSummary queueConfigSummary : queueConfigSummaries) {
                // Get the queue
                QueueDTO queueDTO = queueService.getQueue(queueConfigSummary.queueId());

                // Upsert the queue
                if (queueDTO == null) {
                    CreateQueueDTO createQueueDTO = new CreateQueueDTO();
                    createQueueDTO.setId(queueConfigSummary.queueId());
                    createQueueDTO.setName(queueConfigSummary.queueName());
                    queueDTO = queueService.createQueue(createQueueDTO);
                }

                queueService.addRoutingProfiles(queueDTO.getId(), Set.of(routingProfile.getRoutingProfileId()));
            }
        }
    }

    private String syncSecurityProfiles(Instance instance) {
        String[] agentPatterns = {"agent"};
        String[] supervisorPatterns = {"supervisor", "manager"};

        // Get the security profiles
        List<SecurityProfileSummary> securityProfileSummaries = connectClient.listSecurityProfiles(
                ListSecurityProfilesRequest
                        .builder()
                        .instanceId(instance.getId())
                        .build()
        ).securityProfileSummaryList();

        // Get the agent security profile
        SecurityProfileSummary agentSecurityProfile = securityProfileSummaries.stream()
                .filter(securityProfileSummary -> {
                    for (String pattern : agentPatterns) {
                        if (securityProfileSummary.name().toLowerCase().contains(pattern)) {
                            return true;
                        }
                    }
                    return false;
                })
                .findFirst()
                .orElse(null);

        // Get the supervisor security profile
        SecurityProfileSummary supervisorSecurityProfile = securityProfileSummaries.stream()
                .filter(securityProfileSummary -> {
                    for (String pattern : supervisorPatterns) {
                        if (securityProfileSummary.name().toLowerCase().contains(pattern)) {
                            return true;
                        }
                    }
                    return false;
                })
                .findFirst()
                .orElse(null);

        // Upsert the agent security profile
        if (agentSecurityProfile != null) {
            UserTypeDTO userTypeDTO = userTypeService.getUserTypeBySecurityProfileId(agentSecurityProfile.id());
            if (userTypeDTO == null) {
                UserTypeCreateDTO agentUserType = new UserTypeCreateDTO();
                agentUserType.setSecurityProfileId(agentSecurityProfile.id());
                agentUserType.setTypeName("agent");
                userTypeService.createUserType(agentUserType);
            }
        }

        // Upsert the supervisor security profile
        if (supervisorSecurityProfile != null) {
            UserTypeDTO userTypeDTO = userTypeService.getUserTypeBySecurityProfileId(supervisorSecurityProfile.id());
            if (userTypeDTO == null) {
                UserTypeCreateDTO supervisorUserType = new UserTypeCreateDTO();
                supervisorUserType.setSecurityProfileId(supervisorSecurityProfile.id());
                supervisorUserType.setTypeName("supervisor");
                userTypeService.createUserType(supervisorUserType);
            }
        }

        return supervisorSecurityProfile != null ? supervisorSecurityProfile.id() : null;
    }

    /**
     * Sync the connect data and firebase data
     *
     * @param syncRequestDTO The request with the instance id and the user pattern
     */
    public CreatedUsersDTO syncConnectData(SyncRequestDTO syncRequestDTO) throws FirebaseAuthException {
        Instance instance = instanceRepository.findById(syncRequestDTO.getInstanceId()).orElse(null);
        assert instance != null;
        syncRoutingProfileQueues(instance);
        String supervisorSecurityId = syncSecurityProfiles(instance);
        assert supervisorSecurityId != null;
        return syncUsers(instance, supervisorSecurityId, syncRequestDTO.getUserPattern());
    }
}
