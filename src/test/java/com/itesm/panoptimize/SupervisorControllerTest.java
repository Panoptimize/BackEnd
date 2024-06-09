package com.itesm.panoptimize;

import com.itesm.panoptimize.controller.SupervisorController;
import com.itesm.panoptimize.service.UserService;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
@AutoConfigureMockMvc
public class SupervisorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private String firebaseToken;

    public SupervisorControllerTest() throws Exception {
    }

    @BeforeEach
    public void setUp() throws IOException {
        firebaseToken = getFirebaseToken();
    }

    private String getFirebaseToken() throws IOException {
        String apiKey = "AIzaSyA2efAQdi2Vgtzl7aI080kouPzIiC8C2MA";
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

    @Test
    public void test_supervisorInfo_valid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.
                        get("/supervisor/info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + firebaseToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.connectId").value(notNullValue()))
                .andExpect(jsonPath("$.firebaseId").value(notNullValue()))
                .andExpect(jsonPath("$.email").value(notNullValue()))
                .andExpect(jsonPath("$.fullName").value(notNullValue()))
                .andExpect(jsonPath("$.routingProfileId").value(notNullValue()))
                .andExpect(jsonPath("$.canSwitch").doesNotExist());
    }

}

