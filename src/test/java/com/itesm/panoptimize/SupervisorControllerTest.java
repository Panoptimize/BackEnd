package com.itesm.panoptimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

@SpringBootTest
@AutoConfigureMockMvc
public class SupervisorControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private String firebaseToken;

    FirebaseTestSetup firebaseTestSetup = new FirebaseTestSetup();

    @BeforeEach
    public void setUp() throws IOException {
        firebaseToken = firebaseTestSetup.getFirebaseToken();
    }

    @Test
    public void test_supervisorInfo() throws Exception {
        mockMvc.perform(get("/supervisor/info")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + firebaseToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.connectId").isString())
                .andExpect(jsonPath("$.firebaseId").isString())
                .andExpect(jsonPath("$.email").isString())
                .andExpect(jsonPath("$.fullName").isString())
                .andExpect(jsonPath("$.routingProfileId").isString());
    }

    @Test
    public void test_supervisorInfo_invalid() throws Exception {
        String invalidToken = "Invalid Token";

        mockMvc.perform(get("/supervisor/info")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + invalidToken))
                .andExpect(status().isUnauthorized());
    }
}

