package com.itesm.panoptimize.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itesm.panoptimize.service.DownloadService;
import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/download")
public class DownloadController {

    private DownloadService downloadService;

    private final ObjectMapper objectMapper;

    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    public DownloadController(DownloadService downloadService, ObjectMapper objectMapper) {
        this.downloadService = downloadService;
        this.objectMapper = objectMapper;
    }

    //Download data from the Dashboard
    // Endpoint to get data in JSON format (This is from the database)


    /*
    // Enpoint to get data from a JSON into a CSV file
    @GetMapping("/getAnyJSONA")
    public ResponseEntity<String> getAnyJSONDataA(){
        String GET_DB_JSON_URL = "";
        ResponseEntity<JsonNode> response = restTemplate.exchange(
                GET_DB_JSON_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<JsonNode>() {}
        );
        JsonNode data = response.getBody();
        if (data == null || !data.isArray()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid JSON response");
        }

        try {
            String filePath = "";
            FileWriter fileWriter = new FileWriter(filePath);
            CSVWriter csvWriter = new CSVWriter(fileWriter);

            // List to hold all headers
            Set<String> headers = new LinkedHashSet<>();
            List<Map<String, String>> rows = new ArrayList<>();

            // Process each element in the JSON array
            for (JsonNode element : data) {
                if (element.isObject()) {
                    Map<String, String> row = new LinkedHashMap<>();
                    Iterator<Map.Entry<String, JsonNode>> fields = element.fields();
                    while (fields.hasNext()) {
                        Map.Entry<String, JsonNode> field = fields.next();
                        headers.add(field.getKey());
                        JsonNode fieldValue = field.getValue();
                        if (fieldValue.isArray()) {
                            List<String> listValues = new ArrayList<>();
                            for (JsonNode item : fieldValue) {
                                listValues.add(item.asText());
                            }
                            row.put(field.getKey(), String.join(";", listValues));
                        } else {
                            row.put(field.getKey(), fieldValue.asText());
                        }
                    }
                    rows.add(row);
                }
            }

            // Write header
            csvWriter.writeNext(headers.toArray(new String[0]));

            // Write rows
            for (Map<String, String> row : rows) {
                List<String> values = new ArrayList<>();
                for (String header : headers) {
                    values.add(row.getOrDefault(header, ""));
                }
                csvWriter.writeNext(values.toArray(new String[0]));
            }

            csvWriter.flush();
            csvWriter.close();

            return ResponseEntity.ok("CSV file saved at: " + filePath);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating JSON file: " + e.getMessage());
        }
    }
    */

    @GetMapping("/getCalculatePerformanceJSON")
    public ResponseEntity<String> getCalculatePerformanceJSON() {
        String GET_DB_JSON_URL = "http://127.0.0.1:8080/dashboard/performance";

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> jsonBodyCPer = new HashMap<>();

        jsonBodyCPer.put("instanceId", "7c78bd60-4a9f-40e5-b461-b7a0dfaad848");
        jsonBodyCPer.put("startDate", "2024-05-11");
        jsonBodyCPer.put("endDate", "2024-05-14");
        jsonBodyCPer.put("routingProfiles", new String[]{"4896ae34-a93e-41bc-8231-bf189e7628b1"});
        jsonBodyCPer.put("queues", new String[]{});

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(jsonBodyCPer, headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(
                GET_DB_JSON_URL,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<JsonNode>() {}
        );
        JsonNode data = response.getBody();

        if (data == null || !data.isObject()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid JSON response");
        }

        try {
            String filePath = "D:\\Tec\\Semestre 2024-1\\Panoptimise\\performancePerAgent.csv";
            FileWriter fileWriter = new FileWriter(filePath);
            CSVWriter csvWriter = new CSVWriter(fileWriter);

            // Extract field names (header)
            Iterator<Map.Entry<String, JsonNode>> fields = data.fields();
            List<String> header = new ArrayList<>();
            List<String> valuess = new ArrayList<>();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                header.add(field.getKey());
            }
            csvWriter.writeNext(header.toArray(new String[0]));

            // Extract field values (row)
            List<String> values = new ArrayList<>();
            for (String fieldName : header) {
                JsonNode fieldValue = data.path(fieldName);
                if (fieldValue.isArray()) {
                    // Concatenate list values into a single string
                    List<String> listValues = new ArrayList<>();
                    for (JsonNode item : fieldValue) {
                        listValues.add(item.asText());
                    }
                    values.add(String.join(", ", listValues));

                } else {
                    values.add(fieldValue.asText());
                }
            }
            csvWriter.writeNext(values.toArray(new String[0]));

            csvWriter.flush();
            csvWriter.close();

            return ResponseEntity.ok("CSV file saved at: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating CSV file: " + e.getMessage());
        }
    }

}
