package com.itesm.panoptimize;

import com.itesm.panoptimize.repository.CompanyRepository;
import com.itesm.panoptimize.repository.UserRepository;
import com.itesm.panoptimize.repository.UserTypeRepository;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.io.IOException;
@SpringBootTest
@AutoConfigureMockMvc
public class AgentControllerTests {
    @Autowired
    private MockMvc mockMvc;

    private String firebaseToken;

    public AgentControllerTests() throws Exception {
    }

    @BeforeEach
    public void setUp() throws IOException {
        firebaseToken = getFirebaseToken();
    }

    private String getFirebaseToken() throws IOException {
        String apiKey = System.getenv("API_KEY_FIREBASE_TEST");
        String username = "test@example.com";
        String password = "password123";

        String url = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + apiKey;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(url);
            request.addHeader("Content-Type", "application/json");

            JSONObject json = new JSONObject();
            json.put("email", username);
            json.put("password", password);
            json.put("returnSecureToken", true);

            StringEntity entity = new StringEntity(json.toString());
            request.setEntity(entity);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String responseBody = EntityUtils.toString(response.getEntity());
                JSONObject responseJson = new JSONObject(responseBody);
                return responseJson.getString("idToken");
            }
        } catch (JSONException e) {
            throw new IOException("Error parsing JSON response", e);
        }
    }



    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserTypeRepository userTypeRepository;
    @Autowired
    private CompanyRepository companyRepository;

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
    public void testGetAgentDetails() throws Exception{
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
    public void testGetAgentByIdDB() throws Exception{
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
    public void testGetAgentConnectIDDB () throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/agent/connect/c0899879-15f1-4bad-a862-c92168730040")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer"+ firebaseToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.connectId").value(notNullValue()))
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.email").value(notNullValue()))
                .andExpect(jsonPath("$.fullName").value(notNullValue()))
                .andExpect(jsonPath("$.routingProfileId").value(notNullValue()));
    }

    @Test
    public void testGetAgentList() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/agent/agents-list")
                        .param("instanceId", "7c78bd60-4a9f-40e5-b461-b7a0dfaad848")
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

    @Test
    public void testGetAgentListNonExistentId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/agent/agents-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + firebaseToken))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

}
