package com.itesm.panoptimize.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class FRCService {
    public float requestJSONBuild() {
        JSONObject requestPayload = new JSONObject();
        requestPayload.put("instance_id", "1");
        requestPayload.put("start_time", "2024-01-01");
        requestPayload.put("end_time", "2024-01-31");

        JSONArray metricsArray = new JSONArray();
        metricsArray.put("contactHandled");
        metricsArray.put("contactsAbandoned");
        metricsArray.put("callbackContacts");
        requestPayload.put("metrics", metricsArray);

        WebClient client = WebClient.create();

        ResponseEntity<String> responseEntity = client.post()
                .uri("http://localhost:8080/dashboard/dataFRC")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(requestPayload.toString()))
                .retrieve()
                .toEntity(String.class)
                .block();

        String jsonResponse = responseEntity.getBody();
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
