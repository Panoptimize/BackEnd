package com.itesm.panoptimize.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itesm.panoptimize.dto.dashboard.ActivityResponseDTO;
import com.itesm.panoptimize.dto.dashboard.DashboardDTO;
import com.itesm.panoptimize.dto.dashboard.MetricResponseDTO;
import com.itesm.panoptimize.dto.performance.AgentPerformanceDTO;
import com.itesm.panoptimize.dto.performance.PerformanceDTO;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DownloadService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final DashboardService dashboardService;

    @Autowired
    private CalculatePerformanceService calculatePerformanceService;
    @Autowired
    private DashboardService apiClient;
    @Autowired
    private DashboardService metricService;

    @Autowired
    public DownloadService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper, DashboardService dashboardService) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8000").build();
        this.objectMapper = objectMapper;
        this.dashboardService = dashboardService;
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date sDate = new Date();
        Date eDate = new Date();

        try {
            sDate = dateFormat.parse("2024-05-02");
            eDate = dateFormat.parse("2024-05-20");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        PerformanceDTO performanceDTO = new PerformanceDTO();
        performanceDTO.setInstanceId("7c78bd60-4a9f-40e5-b461-b7a0dfaad848");
        performanceDTO.setStartDate(sDate);
        performanceDTO.setEndDate(eDate);
        performanceDTO.setRoutingProfiles(new String[]{"4896ae34-a93e-41bc-8231-bf189e7628b1"});
        performanceDTO.setQueues(new String[]{});

        List<AgentPerformanceDTO> performanceData = getPerformance( performanceDTO);

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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date sDate = new Date();
        Date eDate = new Date();

        try {
            sDate = dateFormat.parse("2024-05-02");
            eDate = dateFormat.parse("2024-05-20");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setInstanceId("7c78bd60-4a9f-40e5-b461-b7a0dfaad848");
        dashboardDTO.setStartDate(sDate);
        dashboardDTO.setEndDate(eDate);
        dashboardDTO.setRoutingProfiles(new String[]{"4896ae34-a93e-41bc-8231-bf189e7628b1"});
        dashboardDTO.setAgents(new String[]{});

        Map<String, Object> combinedMetrics = new HashMap<>();

        MetricResponseDTO allData = getAllData(dashboardDTO);

        combinedMetrics.put("avgHoldTime", allData.getAvgHoldTime());
        combinedMetrics.put("firstContactResolution", allData.getFirstContactResolution());
        combinedMetrics.put("abandonmentRate", allData.getAbandonmentRate());
        combinedMetrics.put("serviceLevel", allData.getServiceLevel());
        combinedMetrics.put("agentScheduleAdherence", allData.getAgentScheduleAdherence());
        combinedMetrics.put("avgSpeedOfAnswer", allData.getAvgSpeedOfAnswer());

        Mono<Map<String, Integer>> valuesMono = apiClient.getMetricResults(dashboardDTO).map(metricService::extractValues);
        valuesMono.subscribe(values -> combinedMetrics.putAll(values));


        JsonNode json = objectMapper.valueToTree(combinedMetrics);

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

    }

    public XSSFWorkbook getActivities(XSSFWorkbook workbook){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date sDate = new Date();
        Date eDate = new Date();

        try {
            sDate = dateFormat.parse("2024-05-02");
            eDate = dateFormat.parse("2024-05-20");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setInstanceId("7c78bd60-4a9f-40e5-b461-b7a0dfaad848");
        dashboardDTO.setStartDate(sDate);
        dashboardDTO.setEndDate(eDate);
        dashboardDTO.setAgents(new String[]{});
        dashboardDTO.setRoutingProfiles(new String[]{"4896ae34-a93e-41bc-8231-bf189e7628b1"});

        ActivityResponseDTO actData = getActivitiesData(dashboardDTO);

        JsonNode json = objectMapper.valueToTree(actData);

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

    private List<AgentPerformanceDTO> getPerformance(PerformanceDTO performanceDTO){
        List<AgentPerformanceDTO> performanceData = calculatePerformanceService.getMetricsData(performanceDTO);
        return performanceData;
    }


    private MetricResponseDTO getAllData(DashboardDTO dashboardDTO){
        MetricResponseDTO metricsData = dashboardService.getMetricsData(dashboardDTO);
        return metricsData;
    }

    private ActivityResponseDTO getActivitiesData(DashboardDTO dashboardDTO){
        ActivityResponseDTO actData = dashboardService.getActivity(dashboardDTO);
        return actData;
    }


}
