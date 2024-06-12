package com.itesm.panoptimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DashboardTests {

    @Autowired
    private MockMvc mockMvc;
    private String firebaseToken;

    FirebaseTestSetup firebaseTestSetup = new FirebaseTestSetup();

    @BeforeEach
    public void setUp() throws IOException {
        firebaseToken = firebaseTestSetup.getFirebaseToken();
    }

    @Test
    public void testGetKpis() throws Exception {
        mockMvc.perform(post("/dashboard/combined-metrics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + firebaseToken)
                        .content("{\"startDate\": \"2024-05-01\",\"endDate\": \"2024-06-04\",\"routingProfiles\": [\"4896ae34-a93e-41bc-8231-bf189e7628b1\", \"3b8f514f-cf60-43f9-8ad6-9919951055eb\"]\n}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.metrics.avgSpeedOfAnswer").hasJsonPath())
                .andExpect(jsonPath("$.metrics.avgHoldTime").hasJsonPath())
                .andExpect(jsonPath("$.metrics.serviceLevel").hasJsonPath())
                .andExpect(jsonPath("$.metrics.abandonmentRate").hasJsonPath())
                .andExpect(jsonPath("$.metrics.firstContactResolution").hasJsonPath())
                .andExpect(jsonPath("$.metrics.agentScheduleAdherence").hasJsonPath())
                .andExpect(jsonPath("$.activities.activities").hasJsonPath())
                .andExpect(jsonPath("$.performanceData").hasJsonPath())
                .andExpect(jsonPath("$.voice").hasJsonPath())
                .andExpect(jsonPath("$.chat").hasJsonPath());
    }

    @Test
    public void filtersTest () throws Exception {
        mockMvc.perform(get("/dashboard/filters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + firebaseToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workspaces").hasJsonPath())
                .andExpect(jsonPath("$.instanceCreationDate").hasJsonPath());
    }
}
