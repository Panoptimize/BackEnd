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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StatusTests {

    @Autowired
    private MockMvc mockMvc;
    private String firebaseToken;

    FirebaseTestSetup firebaseTestSetup = new FirebaseTestSetup();

    @BeforeEach
    public void setUp() throws IOException {
        firebaseToken = firebaseTestSetup.getFirebaseToken();
    }

    @Test
    public void test_status_valid() throws Exception {
        mockMvc.perform(get("/status/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + firebaseToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].metricName").value("AGENTS"))
                .andExpect(jsonPath("$[1].metricName").value("AGENTS_ONLINE"))
                .andExpect(jsonPath("$[2].metricName").value("AGENTS_AVAILABLE"))
                .andExpect(jsonPath("$[3].metricName").value("AGENTS_OFFLINE"))
        ;
    }

    @Test
    public void test_status_invalid() throws Exception {
        mockMvc.perform(get("/status?instanceId=7c78bd60-b461-b7a0dfaad848")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + firebaseToken))
                .andExpect(status().isInternalServerError()); // For 500 errors
                //.andExpect(status().isBadRequest()); For 400 errors
    }


}
