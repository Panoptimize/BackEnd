package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.agent.StatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StatusService {

    @Autowired
    private RestTemplate restTemplate;

    public StatusDTO filterMetrics() {
        String url = "http://localhost:8000/metrics";

        String requestBody = "{"
                + "\"Metrics\": ["
                + "{ \"Name\": \"AGENTS_AVAILABLE\", \"Unit\": \"COUNT\" },"
                + "{ \"Name\": \"AGENTS_AFTER_CONTACT_WORK\", \"Unit\": \"COUNT\" },"
                + "{ \"Name\": \"AGENTS_ON_CONTACT\", \"Unit\": \"COUNT\" },"
                + "{ \"Name\": \"AGENTS_ONLINE\", \"Unit\": \"COUNT\" }"
                + "],"
                + "\"DataSnapshotTime\": \"2024-04-29T03:35:15.787830\","
                + "\"Filters\": { \"Channels\": [], \"Queues\": [] },"
                + "\"Message\": null"
                + "}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        StatusDTO data = restTemplate.postForObject(url, entity, StatusDTO.class);


        if (data == null) {
            // handle the case where data is null, for example by returning null or throwing an exception
            return null;
        }

        return data;
    }





}
