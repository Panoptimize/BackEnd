package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.contact.SearchContactsDTO;
import com.itesm.panoptimize.dto.contact.SearchContactsResponseDTO;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.connect.ConnectClient;
import software.amazon.awssdk.services.connect.model.SearchContactsRequest;
import software.amazon.awssdk.services.connect.model.SearchContactsResponse;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactSearchService {

    private final ConnectClient connectClient;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ContactSearchService(ConnectClient connectClient) {
        this.connectClient = connectClient;
    }

    public SearchContactsResponseDTO searchContacts(SearchContactsDTO searchContactsDTO) {
        Long startTimeEpoch = convertToEpoch(searchContactsDTO.getTimeRange().getStartTime());
        Long endTimeEpoch = convertToEpoch(searchContactsDTO.getTimeRange().getEndTime());

        SearchContactsRequest.Builder requestBuilder = SearchContactsRequest.builder()
                .instanceId(searchContactsDTO.getInstanceId())
                .timeRange(t -> t
                        .startTime(Instant.ofEpochMilli(startTimeEpoch))
                        .endTime(Instant.ofEpochMilli(endTimeEpoch))
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
                    dto.setSentiment("Neutral"); // Establecer el valor del Sentiment
                    dto.setAgentId(contact.agentInfo() != null ? contact.agentInfo().id() : null); // Establecer el ID del agente
                    return dto;
                })
                .collect(Collectors.toList());

        SearchContactsResponseDTO responseDTO = new SearchContactsResponseDTO();
        responseDTO.setContacts(contacts);
        responseDTO.setNextToken(response.nextToken());
        responseDTO.setTotalCount(response.totalCount());

        return responseDTO;
    }

    private Long convertToEpoch(String date) {
        try {
            LocalDate localDate = LocalDate.parse(date, formatter);
            return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format, expected YYYY-MM-DD", e);
        }
    }
}