package com.itesm.panoptimize.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class FCRService {

    private final WebClient webClient;

    @Autowired
    public FCRService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }

    public float fcrMetrics() {
        String requestBody = "{"
                + "\"instance_id\": \"1\","
                + "\"start_time\": \"2024-01-01\","
                + "\"end_time\": \"2024-01-31\","
                + "\"metrics\": ["
                + "\"contactHandled\","
                + "\"contactsAbandoned\","
                + "\"callbackContacts\""
                + "]}";

        ResponseEntity<String> dashMetricData = webClient.post()
                .uri("/dashboard/dataFRC")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .toEntity(String.class)
                .block();

        String jsonResponse = dashMetricData.getBody();
        JSONObject responseObject = new JSONObject(jsonResponse);
        JSONObject dataObject = responseObject.getJSONObject("Data");

        int contactHandled = dataObject.getInt("contactHandled");
        int contactsAbandoned = dataObject.getInt("contactsAbandoned");
        int callbackContacts = dataObject.getInt("callbackContacts");

        int firstResContact = contactHandled - (contactsAbandoned + callbackContacts);

        float firstResKPI = ((float) firstResContact / contactHandled) * 100;

        return firstResKPI;
    }
}
