package com.itesm.panoptimize.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itesm.panoptimize.dto.performance.AgentPerformanceDTO;
import com.itesm.panoptimize.dto.performance.PerformanceDTO;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class DownloadService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Autowired
    private CalculatePerformanceService calculatePerformanceService;

    @Autowired
    public DownloadService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8000").build();
        this.objectMapper = objectMapper;
    }

    public XSSFWorkbook getFinalReport(String url){
        XSSFWorkbook workbook = new XSSFWorkbook();

        getCalculatePerformance(workbook);
        getRestOfData(workbook);
        getActivities(workbook);


        try (FileOutputStream outputStream = new FileOutputStream(url)) {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return workbook;

    }

    public XSSFWorkbook getCalculatePerformance(XSSFWorkbook workbook) {

        List<AgentPerformanceDTO> performanceData = getPerformance( new PerformanceDTO());

        try{
            JsonNode jsonArray = objectMapper.readTree(performanceData.toString());

            if (!jsonArray.isArray()) {
                throw new IOException("Expected an array of JSON objects");
            }

            String sheetname = "Performance Per Agent";
            XSSFSheet sheet = workbook.createSheet(sheetname);

            Row headerRow = sheet.createRow(0);
            int colNum = 0;
            int rowNum = 1;

            headerRow.createCell(colNum++).setCellValue("Agent ID");
            headerRow.createCell(colNum++).setCellValue("Performances");
            headerRow.getRowStyle().setBorderBottom(BorderStyle.valueOf((short) 1));

            for (JsonNode json : jsonArray) {
                if (json.has("agentID") && json.has("performances")) {
                    String agentID = json.get("agentID").asText();
                    JsonNode performances = json.get("performances");

                    if (performances.isArray()) {
                        for (JsonNode performance : performances) {
                            Row row = sheet.createRow(rowNum++);
                            colNum = 0;
                            row.createCell(colNum++).setCellValue(agentID);
                            row.createCell(colNum++).setCellValue(performance.asDouble());
                        }
                    }
                }
            }

            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                sheet.autoSizeColumn(i);
            }

            return workbook;

        }catch (IOException e){
            return null;
        }

    }

    public XSSFWorkbook getRestOfData(XSSFWorkbook workbook){
        JsonNode allData = getAllData();
        try{
            JsonNode json = objectMapper.readTree(allData.toString());

            String sheetname = "General KPIs";
            XSSFSheet sheet = workbook.createSheet(sheetname);

            List<String> keys = new ArrayList<>();
            List<Object> values = new ArrayList<>();
            List<Object> keys2 = new ArrayList<>();
            List<Object> values2 = new ArrayList<>();

            Iterator<Map.Entry<String, JsonNode>> fieldValues = json.fields();
            while (fieldValues.hasNext()) {
                Map.Entry<String, JsonNode> entry = fieldValues.next();
                if(!entry.getKey().equals("activities") && !entry.getKey().equals("voice") && !entry.getKey().equals("chat")){
                    keys.add(entry.getKey());
                    values.add(entry.getValue());
                }
                else if(entry.getKey().equals("voice") || entry.getKey().equals("chat")){
                    keys2.add(entry.getKey());
                    values2.add(entry.getValue());
                }

            }

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("KPI");
            headerRow.createCell(1).setCellValue("Value");
            headerRow.createCell(2).setCellValue("Type of Interaction");
            headerRow.createCell(3).setCellValue("Total Interactions");

            int rowNum = 1;
            Row row = sheet.createRow(rowNum);
            for (int i = 0; i < keys.size(); i++) {
                row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(keys.get(i));
                row.createCell(1).setCellValue(values.get(i).toString());
            }

            rowNum = 1;
            for (int i = 0; i < keys2.size(); i++) {
                Row row2 = sheet.getRow(rowNum++);
                row2.createCell(2).setCellValue(keys2.get(i).toString());
                row2.createCell(3).setCellValue(values2.get(i).toString());
            }


            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);

            return workbook;
        }catch (IOException e){
            return null;
        }

    }

    public XSSFWorkbook getActivities(XSSFWorkbook workbook){
        JsonNode allData = getAllData();
        try{
            JsonNode json = objectMapper.readTree(allData.toString());

            String sheetname = "Total Agent Activities";
            XSSFSheet sheet = workbook.createSheet(sheetname);

            List<String> keys = new ArrayList<>();
            List<Object> values = new ArrayList<>();

            Iterator<Map.Entry<String, JsonNode>> fieldValues = json.fields();
            while (fieldValues.hasNext()) {
                Map.Entry<String, JsonNode> entry = fieldValues.next();
                if(entry.getKey().equals("activities") ){
                    keys.add(entry.getKey());
                    values.add(entry.getValue());
                }

            }

            Row headerRow = sheet.createRow(0);
            int colNum = 0;
            int rowNum = 1;

            headerRow.createCell(colNum++).setCellValue("Total Agent Activities");
            headerRow.createCell(colNum++).setCellValue("Date");

            for (int i = 0; i < keys.size(); i++) {
                JsonNode activities = (JsonNode) values.get(i);
                if (activities.isArray()) {
                    for (JsonNode activity : activities) {
                        if (activity.has("value") && activity.has("startTime")) {

                            int value = activity.get("value").asInt();
                            String startTime = activity.get("startTime").asText();
                            Row row = sheet.createRow(rowNum++);
                            colNum = 0;

                            row.createCell(colNum++).setCellValue(value);
                            row.createCell(colNum++).setCellValue(startTime);
                        }
                    }
                }
            }

            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                sheet.autoSizeColumn(i);
            }


            return workbook;

        }
        catch (IOException e){
            return null;
        }

    }

    private List<AgentPerformanceDTO> getPerformance(PerformanceDTO performanceDTO){
        //TODO
        //When the actual post body comes from an endpoint, changes this to receive the body from the endpoint
        /*
        String requestBody = "{\n" +
                "  \"instanceId\": \"7c78bd60-4a9f-40e5-b461-b7a0dfaad848\",\n" +
                "  \"startDate\": \"2024-05-11\",\n" +
                "  \"endDate\": \"2024-05-14\",\n" +
                "  \"routingProfiles\": [\"4896ae34-a93e-41bc-8231-bf189e7628b1\"],\n" +
                "  \"queues\": []\n" +
                "}";
        */
        List<AgentPerformanceDTO> performanceData = calculatePerformanceService.getMetricsData(performanceDTO);
        /*
        return webClient.post()
                .uri("http://127.0.0.1:8080/dashboard/performance")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

         */
        return performanceData;

    }


    private JsonNode getAllData(){
        //TODO
        //When the actual post body comes from an endpoint, changes this to receive the body from the endpoint
        String requestBody = "{\n" +
                "  \"instanceId\": \"7c78bd60-4a9f-40e5-b461-b7a0dfaad848\",\n" +
                "  \"startDate\": \"2024-05-11\",\n" +
                "  \"endDate\": \"2024-05-14\",\n" +
                "  \"agents\": [],\n" +
                "  \"routingProfiles\": [\"4896ae34-a93e-41bc-8231-bf189e7628b1\"]\n" +
                "}";

        return webClient.post()
                .uri("http://127.0.0.1:8080/dashboard/combined-metrics")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }


}
