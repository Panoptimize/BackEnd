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
import org.springframework.beans.factory.annotation.Value;
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

    FirebaseTestSetup firebaseTestSetup = new FirebaseTestSetup();

    @BeforeEach
    public void setUp() throws IOException {
        firebaseToken = firebaseTestSetup.getFirebaseToken();
    }

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserTypeRepository userTypeRepository;
    @Autowired
    private CompanyRepository companyRepository;


    @Test
    public void testGetDownload() throws Exception {

        String jsonContent = "{\"startDate\":\"2024-05-01\",\"endDate\":\"2024-05-31\",\"routingProfiles\":[\"4896ae34-a93e-41bc-8231-bf189e7628b1\"],\"queues\":[],\"agents\":[]}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/download/getDownload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + firebaseToken)
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_OCTET_STREAM));
    }

    @Test
    public void testGetDownloadInternalServerError() throws Exception{
        String jsonContent = "{\"startDate\":\"\",\"endDate\":\"2024-05-31\",\"routingProfiles\":[],\"queues\":[],\"agents\":[]}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/download/getDownload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + firebaseToken)
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }


}





