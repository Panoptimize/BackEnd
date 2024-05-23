package com.itesm.panoptimize.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class DownloadService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public DownloadService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8000").build();
        this.objectMapper = objectMapper;
    }

    public XSSFWorkbook getFinalReport(String url){
        XSSFWorkbook workbook = new XSSFWorkbook();

        getCalculatePerformance(workbook);
        //TODO
        //Here we are going to add the other functions that to get the rest of the data, 4 more to go.

        try (FileOutputStream outputStream = new FileOutputStream(url)) {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return workbook;

    }

    public XSSFWorkbook getCalculatePerformance(XSSFWorkbook workbook) {

        JsonNode performanceData = getPerformance();
        try{
            JsonNode json = objectMapper.readTree(performanceData.toString());

            List<String> keys = new ArrayList<>();
            List<Object> values = new ArrayList<>();

            Iterator<Map.Entry<String, JsonNode>> fieldValues = json.fields();
            while (fieldValues.hasNext()) {
                Map.Entry<String, JsonNode> entry = fieldValues.next();
                keys.add(entry.getKey());
                values.add(entry.getValue());
            }

            String sheetname = "Performance Per Agent";
            XSSFSheet sheet = workbook.createSheet(sheetname);


            Row headerRow = sheet.createRow(0);
            int colNum = 0;
            int rowNum = 1;
            int alingCol = 0;

            for (String key : keys) {
                headerRow.createCell(colNum++).setCellValue(key);
                sheet.autoSizeColumn(alingCol);
                alingCol++;
            }

            for (Object value : values) {
                Row row = sheet.createRow(rowNum++);
                colNum = 0;
                for (JsonNode field : (JsonNode) value) {
                    row.createCell(colNum++).setCellValue(field.asText());
                }
            }
            return workbook;

        }catch (IOException e){
            return null;
        }

    }

    private JsonNode getPerformance(){
        //TODO
        //When the actual post body comes from an endpoint, changes this to receive the body from the endpoint
        String requestBody = "{\n" +
                "  \"instanceId\": \"7c78bd60-4a9f-40e5-b461-b7a0dfaad848\",\n" +
                "  \"startDate\": \"2024-05-11\",\n" +
                "  \"endDate\": \"2024-05-14\",\n" +
                "  \"routingProfiles\": [\"4896ae34-a93e-41bc-8231-bf189e7628b1\"],\n" +
                "  \"queues\": []\n" +
                "}";

        return webClient.post()
                .uri("http://127.0.0.1:8080/dashboard/performance")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

    }

    //TODO
    //We Will need the rest of the functions to get the data from the other endpoints

}
