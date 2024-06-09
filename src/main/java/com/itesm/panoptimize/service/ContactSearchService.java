package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.contact.SearchContactsDTO;
import com.itesm.panoptimize.dto.contact.SearchContactsResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import software.amazon.awssdk.services.connect.ConnectClient;
import software.amazon.awssdk.services.connect.model.SearchContactsRequest;
import software.amazon.awssdk.services.connect.model.SearchContactsResponse;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        System.out.println("Start Time Epoch: " + startTimeEpoch);
        System.out.println("End Time Epoch: " + endTimeEpoch);

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

        System.out.println("SearchContactsRequest: " + requestBuilder.build());

        SearchContactsResponse response = connectClient.searchContacts(requestBuilder.build());

        System.out.println("SearchContactsResponse: " + response);

        Map<String, SearchContactsResponseDTO.ContactSummaryDTO> uniqueContacts = new HashMap<>();
        response.contacts().forEach(contact -> {
            String agentId = contact.agentInfo() != null && contact.agentInfo().id() != null ? contact.agentInfo().id() : "Unknown Agent";
            if (!uniqueContacts.containsKey(agentId)) {
                SearchContactsResponseDTO.ContactSummaryDTO dto = new SearchContactsResponseDTO.ContactSummaryDTO();
                dto.setContactId(contact.id());
                dto.setChannel(String.valueOf(contact.channel()));
                dto.setInitiationTimestamp(contact.initiationTimestamp().toEpochMilli());
                dto.setAgentId(agentId);
                dto.setSentiment("Neutral");

                uniqueContacts.put(agentId, dto);
            }
        });

        // Convertir el mapa a una lista para el DTO final
        List<SearchContactsResponseDTO.ContactSummaryDTO> contacts = new ArrayList<>(uniqueContacts.values());

        SearchContactsResponseDTO responseDTO = new SearchContactsResponseDTO();
        responseDTO.setContacts(contacts);
        responseDTO.setNextToken(response.nextToken());
        responseDTO.setTotalCount(response.totalCount());

        // Logging de la respuesta DTO
        System.out.println("SearchContactsResponseDTO: " + responseDTO);

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
