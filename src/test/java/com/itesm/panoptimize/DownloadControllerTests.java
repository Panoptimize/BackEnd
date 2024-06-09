package com.itesm.panoptimize;

import com.itesm.panoptimize.dto.download.DownloadDTO;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class DownloadControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private String firebaseToken;

    @BeforeEach
    public void setUp() throws IOException {
        firebaseToken = getFirebaseToken();
    }

    private String getFirebaseToken() throws IOException {
        String apiKey = "AIzaSyA2efAQdi2Vgtzl7aI080kouPzIiC8C2MA";
        String username = "A01656828@tec.mx";
        String password = "ernesto-561";

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


    @Test
    public void testGetDownload() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/download/getDownload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + firebaseToken)
                        .content("{\"instanceId\":\"7c78bd60-4a9f-40e5-b461-b7a0dfaad848\",\"startDate\":\"2024-05-01\",\"endDate\":\"2024-05-31\",\"routingProfiles\":[\"4896ae34-a93e-41bc-8231-bf189e7628b1\"],\"queues\":[],\"agents\":[]}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_OCTET_STREAM));
    }

    @Test
    public void testGetDownloadInternalServerError() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/download/getDownload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + firebaseToken)
                        .content("{\"instanceId\":\"7c78bd60-4a9f-40e5-b461-b7a0dfaad848\",\"startDate\":\"\",\"endDate\":\"2024-05-31\",\"routingProfiles\":[],\"queues\":[],\"agents\":[]}"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }


}





