package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.contact.ContactDTO;
import com.itesm.panoptimize.dto.contact.CreateContactDTO;
import com.itesm.panoptimize.dto.contactevent.ContactEventDTO;
import com.itesm.panoptimize.util.Constants;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.connect.ConnectClient;
import software.amazon.awssdk.services.connect.model.GetContactAttributesRequest;
import software.amazon.awssdk.services.connect.model.GetContactAttributesResponse;
import software.amazon.awssdk.services.connect.model.ListUsersRequest;
import software.amazon.awssdk.services.connect.model.UserSummary;

import java.util.List;

@Service
public class ConnectEventService {
    private final ContactService contactService;
    private final ConnectClient connectClient;

    public ConnectEventService(ContactService contactService, ConnectClient connectClient) {
        this.contactService = contactService;
        this.connectClient = connectClient;
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

    private void syncUsers(String instanceId) {
        List<UserSummary> userSummaries = connectClient.listUsers(
                ListUsersRequest
                        .builder()
                        .instanceId(instanceId)
                        .build()
        ).userSummaryList();
        

    }

    /**
     * Sync the connect data and firebase data
     * @param instanceId the instance id
     */
    public void syncConnectData(String instanceId) {
        syncUsers(instanceId);
    }
}
