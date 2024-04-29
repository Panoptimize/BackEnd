package com.itesm.panoptimize.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FRCService {
    public ResponseEntity<String> requestJSONBuild() {
        JSONObject requestPayload = new JSONObject();
        requestPayload.put("instance_id", "1");
        requestPayload.put("start_time", "2024-01-01");
        requestPayload.put("end_time", "2024-01-31");

        JSONArray metricsArray = new JSONArray();
        metricsArray.put("contactHandled");
        metricsArray.put("contactsAbandoned");
        metricsArray.put("callbackContacts");
        requestPayload.put("metrics", metricsArray);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestPayload.toString(), headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                "http://localhost:8080/dashboard/dataFRC", requestEntity, String.class
        );

        String jsonResponse = responseEntity.getBody();
        JSONObject responseObject = new JSONObject(jsonResponse);
        JSONObject dataObject = responseObject.getJSONObject("Data");

        int contactHandled = dataObject.getInt("contactHandled");
        int contactsAbandoned = dataObject.getInt("contactsAbandoned");
        int callbackContacts = dataObject.getInt("callbackContacts");

        System.out.println("contactHandled: " + contactHandled);
        System.out.println("contactsAbandoned: " + contactsAbandoned);
        System.out.println("callbackContacts: " + callbackContacts);

        return responseEntity;
    }
}
