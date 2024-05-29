package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.contact.SearchContactsDTO;
import com.itesm.panoptimize.dto.contact.SearchContactsResponseDTO;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.connect.ConnectClient;
import software.amazon.awssdk.services.connect.model.SearchContactsRequest;
import software.amazon.awssdk.services.connect.model.SearchContactsResponse;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactSearchService {

    private final ConnectClient connectClient;

    public ContactSearchService(ConnectClient connectClient) {
        this.connectClient = connectClient;
    }

    public SearchContactsResponseDTO searchContacts(SearchContactsDTO searchContactsDTO) {
        SearchContactsRequest.Builder requestBuilder = SearchContactsRequest.builder()
                .instanceId(searchContactsDTO.getInstanceId())
                .timeRange(t -> t
                        .startTime(Instant.ofEpochMilli(searchContactsDTO.getTimeRange().getStartTime()))
                        .endTime(Instant.ofEpochMilli(searchContactsDTO.getTimeRange().getEndTime()))
                        .type(searchContactsDTO.getTimeRange().getType()));

        if (searchContactsDTO.getMaxResults() != null) {
            requestBuilder.maxResults(searchContactsDTO.getMaxResults());
        }

        if (searchContactsDTO.getNextToken() != null) {
            requestBuilder.nextToken(searchContactsDTO.getNextToken());
        }

        SearchContactsResponse response = connectClient.searchContacts(requestBuilder.build());

        List<SearchContactsResponseDTO.ContactSummaryDTO> contacts = response.contacts().stream()
                .map(contact -> {
                    SearchContactsResponseDTO.ContactSummaryDTO dto = new SearchContactsResponseDTO.ContactSummaryDTO();
                    dto.setContactId(contact.id());
                    dto.setChannel(String.valueOf(contact.channel()));
                    dto.setInitiationTimestamp(contact.initiationTimestamp().toEpochMilli());
                    return dto;
                })
                .collect(Collectors.toList());

        SearchContactsResponseDTO responseDTO = new SearchContactsResponseDTO();
        responseDTO.setContacts(contacts);
        responseDTO.setNextToken(response.nextToken());
        responseDTO.setTotalCount(response.totalCount());

        return responseDTO;
    }
}
