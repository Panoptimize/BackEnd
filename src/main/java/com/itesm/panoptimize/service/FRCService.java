package com.itesm.panoptimize.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class FRCService {
    public ResponseEntity<String> requestJSONBuild() throws IOException {
        ClassPathResource resource = new ClassPathResource("request.json");
        InputStream inputStream = resource.getInputStream();
        byte[] fileBytes = FileCopyUtils.copyToByteArray(inputStream);
        String fileContents = new String(fileBytes, StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(fileContents, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                "http://localhost:8080/dashboard/dataFRC", requestEntity, String.class
        );

        String responseBody = responseEntity.getBody();

        System.out.println("Response from dataFRC endpoint: " + responseEntity);

        return responseEntity;
    }
}
