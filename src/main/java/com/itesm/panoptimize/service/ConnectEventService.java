package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.contact.ContactDTO;
import com.itesm.panoptimize.dto.contact_event.ContactEventDTO;
import com.itesm.panoptimize.dto.contact_event.Detail;
import com.itesm.panoptimize.util.Constants;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.connect.ConnectClient;
import software.amazon.awssdk.services.connect.model.GetContactAttributesRequest;
import software.amazon.awssdk.services.connect.model.GetContactAttributesResponse;
import software.amazon.awssdk.services.connect.model.User;

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
                            .instanceId(Constants.extractId(event.getDetail().getInstanceARN()))
                            .initialContactId(event.getDetail().getContactID().toString())
                            .build()
            );

            if (!responseAttributes.attributes().containsKey("Satisfaction")) {
                return null;
            }

            ContactDTO contactDTO = new ContactDTO();
            contactDTO.setId(event.getDetail().getContactID().toString());
            contactDTO.setSatisfaction(Integer.parseInt(responseAttributes.attributes().get("Satisfaction")));
            contactDTO.setAgentId(Constants.extractId(event.getDetail().getAgentInfo().getAgentArn()));

            return contactService.createContact(contactDTO);
        }
        return null;
    }
}
