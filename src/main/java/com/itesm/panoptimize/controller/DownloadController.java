package com.itesm.panoptimize.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itesm.panoptimize.service.DownloadService;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
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
            String filePath = "D:\\Tec\\Semestre 2024-1\\Panoptimise\\performancePerAgent.xlsx";
            JsonNode headerNode = data.get("header");
            JsonNode bodyNode = data.get("body");
            System.out.println("Header: " + headerNode);
            System.out.println("Body: " + bodyNode);

            if (headerNode == null || !headerNode.isArray() || bodyNode == null || !bodyNode.isArray()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid JSON structure");
            }

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Performance Per Agent");

            // Write header
            Row headerRow = sheet.createRow(0);
            int colNum = 0;
            for (JsonNode headerField : headerNode) {
                headerRow.createCell(colNum++).setCellValue(headerField.asText());
            }

            // Write body
            int rowNum = 1;
            for (JsonNode rowNode : bodyNode) {
                Row row = sheet.createRow(rowNum++);
                colNum = 0;
                for (JsonNode field : rowNode) {
                    row.createCell(colNum++).setCellValue(field.asText());
                }
            }

            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
            }
            workbook.close();

            return ResponseEntity.ok("Excel file saved at: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating CSV file: " + e.getMessage());
        }
    }

}
