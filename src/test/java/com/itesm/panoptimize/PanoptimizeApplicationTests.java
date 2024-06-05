package com.itesm.panoptimize;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itesm.panoptimize.controller.SupervisorController;
import com.itesm.panoptimize.model.*;
import com.itesm.panoptimize.repository.CompanyRepository;
import com.itesm.panoptimize.repository.NotificationRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
class PanoptimizeApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	private String firebaseToken;

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



	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserTypeRepository userTypeRepository;
	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private NotificationRepository notificationRepository;

	@Test
	public void testGetPerformance() throws Exception {
		mockMvc.perform(post("/dashboard/performance")
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", "Bearer " + firebaseToken)
						.content("{\"instanceId\": \"7c78bd60-4a9f-40e5-b461-b7a0dfaad848\",\"startDate\": \"2024-05-10\",\"endDate\": \"2024-05-26\",\"routingProfiles\": [\"4896ae34-a93e-41bc-8231-bf189e7628b1\"],\"queues\": []}"))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetKpis() throws Exception {
		mockMvc.perform(post("/dashboard/combined-metrics")
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", "Bearer " + firebaseToken)
						.content("{\"instanceId\": \"7c78bd60-4a9f-40e5-b461-b7a0dfaad848\",\"startDate\": \"2024-05-10\",\"endDate\": \"2024-05-26\",\"routingProfiles\": [\"4896ae34-a93e-41bc-8231-bf189e7628b1\"],\"agents\": []}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.voice").hasJsonPath())
				.andExpect(jsonPath("$.avgSpeedOfAnswer").hasJsonPath())
				.andExpect(jsonPath("$.chat").hasJsonPath())
				.andExpect(jsonPath("$.activities").hasJsonPath())
				.andExpect(jsonPath("$.avgHoldTime").hasJsonPath())
				.andExpect(jsonPath("$.serviceLevel").hasJsonPath())
				.andExpect(jsonPath("$.abandonmentRate").hasJsonPath())
				.andExpect(jsonPath("$.firstContactResolution").hasJsonPath())
				.andExpect(jsonPath("$.agentScheduleAdherence").hasJsonPath());
		;
	}

	@Test
	public void testGetSupervisors() throws Exception {
		mockMvc.perform(get("/supervisor/")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + firebaseToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content").hasJsonPath())
		;
	}


}