package com.itesm.panoptimize;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.itesm.panoptimize.dto.agent.AgentCreateDTO;
import com.itesm.panoptimize.repository.CompanyRepository;
import com.itesm.panoptimize.repository.RoutingProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.io.IOException;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public class AgentControllerTests {
    @Autowired private MockMvc mockMvc;
    @Autowired private RoutingProfileRepository routingProfileRepository;
    @Autowired private CompanyRepository companyRepository;

    private String firebaseToken;
    private final FirebaseTestSetup firebaseTestSetup = new FirebaseTestSetup();
    private final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    public AgentControllerTests() throws Exception {
    }

    @BeforeEach
    public void setUp() throws IOException {
        firebaseToken = firebaseTestSetup.getFirebaseToken();
    }

    String expectedResponsePattern = "Feedback enviado exitosamente\\s*Fecha: .*";

    @Test
    public void testPostAgentFeedbackDB() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/agent/agent/feedback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + firebaseToken).content("{\"supervisorId\": 1, \"agentId\":3, \"date\":\"10/05/2024\", \"title\": \"UnderPerformance\",\"comment\": \"You were lacking today with the speed of the calls\" }"))
                .andExpect(status().isOk())
                .andExpect(content().string(matchesPattern(expectedResponsePattern)));
    }

    @Test
    public void testGetAgentDetails() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/agent/detail/7c78bd60-4a9f-40e5-b461-b7a0dfaad848/d7b861ea-6996-4b90-8b31-9129a1720567")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + firebaseToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.username").value(notNullValue()))
                .andExpect(jsonPath("$.identityInfo").value(notNullValue()))
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.lastModifiedTime").value(notNullValue()));
    }

    @Test
    public void testGetAgentByIdDB() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/agent/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + firebaseToken)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.connectId").value(notNullValue()))
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.email").value(notNullValue()))
                .andExpect(jsonPath("$.fullName").value(notNullValue()))
                .andExpect(jsonPath("$.routingProfileId").value(notNullValue()));
    }

    @Test
    public void testGetAgentConnectIDDB() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/agent/connect/c0899879-15f1-4bad-a862-c92168730040")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + firebaseToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.connectId").value(notNullValue()))
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.email").value(notNullValue()))
                .andExpect(jsonPath("$.fullName").value(notNullValue()))
                .andExpect(jsonPath("$.routingProfileId").value(notNullValue()));
    }

    @Test
    public void testGetAgentList() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/agent/agents-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + firebaseToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        System.out.println("Response: " + responseString);

        mockMvc.perform(asyncDispatch(result))
                .andExpect(jsonPath("$.agents").isArray())
                .andExpect(jsonPath("$.agents").isNotEmpty());
    }

    /* PostCreateNewAgent -- Only create and delete successfull*/
    @Transactional
    @Rollback
    @Test
    public void testPostCreateAgent() throws Exception {
        /*Creating the user for the agent data*/
        AgentCreateDTO agentCreateDTO = new AgentCreateDTO();
        agentCreateDTO.setFullName("Agent Test");
        agentCreateDTO.setEmail("test@test.com");

        // Get first routing profile
        agentCreateDTO.setRoutingProfileId(
                routingProfileRepository.findAll().get(0).getRoutingProfileId()
        );
        agentCreateDTO.setConnectId(UUID.randomUUID().toString());
        agentCreateDTO.setCompanyId(companyRepository.findAll().get(0).getId());

        mockMvc.perform(MockMvcRequestBuilders.post("/agent/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + firebaseToken)
                        .content(ow.writeValueAsBytes(agentCreateDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }


    @Test
    public void testGetAgentListNonExistentId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/agent/agents-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + firebaseToken))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

}
