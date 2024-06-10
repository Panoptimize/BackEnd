package com.itesm.panoptimize;

import com.itesm.panoptimize.controller.ContactController;
import com.itesm.panoptimize.dto.contact.SearchContactsDTO;
import com.itesm.panoptimize.dto.contact.SearchContactsResponseDTO;
import com.itesm.panoptimize.service.ContactSearchService;
import com.itesm.panoptimize.service.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ContactControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ContactSearchService contactSearchService;
    private SearchContactsDTO searchContactsDTO;
    private SearchContactsResponseDTO searchContactsResponseDTO;

    private String firebaseToken;
    FirebaseTestSetup firebaseTestSetup = new FirebaseTestSetup();

    @BeforeEach
    void setUp() throws IOException {
        firebaseToken = firebaseTestSetup.getFirebaseToken();
        searchContactsDTO = new SearchContactsDTO();
        searchContactsDTO.setInstanceId("7c78bd60-4a9f-40e5-b461-b7a0dfaad848");
        SearchContactsDTO.TimeRange timeRange = new SearchContactsDTO.TimeRange();
        timeRange.setStartTime("2024-05-01");
        timeRange.setEndTime("2024-05-31");
        timeRange.setType("CONNECTED_TO_AGENT_TIMESTAMP");
        searchContactsDTO.setTimeRange(timeRange);

        SearchContactsResponseDTO.ContactSummaryDTO contactSummaryDTO = new SearchContactsResponseDTO.ContactSummaryDTO();
        contactSummaryDTO.setContactId("contact-id");
        contactSummaryDTO.setChannel("CHAT");
        contactSummaryDTO.setInitiationTimestamp(1622476800000L); // Example timestamp
        contactSummaryDTO.setAgentId("agent-id");
        contactSummaryDTO.setSentiment("Neutral");

        searchContactsResponseDTO = new SearchContactsResponseDTO();
        searchContactsResponseDTO.setContacts(List.of(contactSummaryDTO));
        searchContactsResponseDTO.setNextToken("next-token");
        searchContactsResponseDTO.setTotalCount(1L);

        Mockito.when(contactSearchService.searchContacts(any(SearchContactsDTO.class))).thenReturn(searchContactsResponseDTO);
    }

    // Test for the searchContacts method in ContactController
    // This test checks if the searchContacts method returns the expected correct response
    @Test
    void testSearchContacts() throws Exception {
        String requestJson = "{\n" +
                "  \"instanceId\": \"7c78bd60-4a9f-40e5-b461-b7a0dfaad848\",\n" +
                "  \"timeRange\": {\n" +
                "    \"startTime\": \"2024-05-01\",\n" +
                "    \"endTime\": \"2024-05-31\",\n" +
                "    \"type\": \"CONNECTED_TO_AGENT_TIMESTAMP\"\n" +
                "  }\n" +
                "}";

        String responseJson = "{\n" +
                "  \"contacts\": [\n" +
                "    {\n" +
                "      \"contactId\": \"contact-id\",\n" +
                "      \"channel\": \"CHAT\",\n" +
                "      \"initiationTimestamp\": 1622476800000,\n" +
                "      \"agentId\": \"agent-id\",\n" +
                "      \"sentiment\": \"Neutral\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"nextToken\": \"next-token\",\n" +
                "  \"totalCount\": 1\n" +
                "}";

        mockMvc.perform(post("/contact/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + firebaseToken)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));
    }

    @Test
    void testSearchContactsNoContactsFound() throws Exception {
        String requestJson = "{\n" +
                "  \"instanceId\": \"7c78bd60-4a9f-40e5-b461-b7a0dfaad848\",\n" +
                "  \"timeRange\": {\n" +
                "    \"startTime\": \"2024-07-01\",\n" +
                "    \"endTime\": \"2024-07-31\",\n" +
                "    \"type\": \"CONNECTED_TO_AGENT_TIMESTAMP\"\n" +
                "  }\n" +
                "}";

        when(contactSearchService.searchContacts(any(SearchContactsDTO.class)))
                .thenReturn(new SearchContactsResponseDTO());

        mockMvc.perform(post("/contact/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + firebaseToken)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contacts").isEmpty());
    }


    // Test a wrong authentication
    @Test
    void testSearchContactsInvalidAuth() throws Exception {
        String requestJson = "{\n" +
                "  \"instanceId\": \"7c78bd60-4a9f-40e5-b461-b7a0dfaad848\",\n" +
                "  \"timeRange\": {\n" +
                "    \"startTime\": \"2024-05-01\",\n" +
                "    \"endTime\": \"2024-05-31\",\n" +
                "    \"type\": \"CONNECTED_TO_AGENT_TIMESTAMP\"\n" +
                "  }\n" +
                "}";

        mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer invalid_token")
                        .content(requestJson))
                .andExpect(status().isUnauthorized());
    }
}