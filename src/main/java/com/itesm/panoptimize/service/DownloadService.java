package com.itesm.panoptimize.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itesm.panoptimize.dto.dashboard.ActivityResponseDTO;
import com.itesm.panoptimize.dto.dashboard.CustomerSatisfactionDTO;
import com.itesm.panoptimize.dto.dashboard.DashboardDTO;
import com.itesm.panoptimize.dto.dashboard.MetricResponseDTO;
import com.itesm.panoptimize.dto.download.DownloadDTO;
import com.itesm.panoptimize.dto.performance.AgentPerformanceDTO;
import com.itesm.panoptimize.dto.performance.PerformanceDTO;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.OutputStream;

import java.util.*;

@Service
public class DownloadService {

    private final ObjectMapper objectMapper;
    private final DashboardService dashboardService;
    private final CalculateSatisfactionService satisfactionService;

    @Autowired
    private CalculatePerformanceService calculatePerformanceService;
    @Autowired
    private DashboardService apiClient;
    @Autowired
    private DashboardService metricService;

    @Autowired
    public DownloadService(ObjectMapper objectMapper, DashboardService dashboardService, CalculateSatisfactionService satisfactionService) {
        this.objectMapper = objectMapper;
        this.dashboardService = dashboardService;
        this.satisfactionService = satisfactionService;
    }

    public XSSFWorkbook getFinalReport(OutputStream url, String instanceId, DownloadDTO downloadDTO) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        getCalculatePerformance(workbook, downloadDTO);
        getRestOfData(workbook, instanceId,downloadDTO);
        getActivities(workbook, instanceId,downloadDTO);

        try {
            workbook.write(url);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return workbook;

    }

    public XSSFWorkbook getCalculatePerformance(XSSFWorkbook workbook, DownloadDTO downloadDTO) {
        List<String> routingProfiles = new ArrayList<>(Arrays.asList(downloadDTO.getRoutingProfiles()));
        PerformanceDTO performanceDTO = new PerformanceDTO();
        performanceDTO.setInstanceId(downloadDTO.getInstanceId());
        performanceDTO.setStartDate(downloadDTO.getStartDate());
        performanceDTO.setEndDate(downloadDTO.getEndDate());
        performanceDTO.setRoutingProfileIds(routingProfiles);


        List<AgentPerformanceDTO> performanceData = getPerformance( performanceDTO);
        try{
            JsonNode jsonArray = objectMapper.valueToTree(performanceData);

            if (!jsonArray.isArray()) {
                throw new IOException("Expected an array of JSON objects");
            }

            String sheetname = "Performance Per Agent";
            XSSFSheet sheet = workbook.createSheet(sheetname);

            Row headerRow = sheet.createRow(0);
            int colNum = 0;
            int rowNum = 1;

            headerRow.createCell(colNum++).setCellValue("Agent name");
            headerRow.createCell(colNum++).setCellValue("Performances");

            if(!jsonArray.isEmpty()) {
                for (JsonNode json : jsonArray) {

                    if (json.has("agentName") && json.has("performances")) {

                        String agentID = json.get("agentName").asText();
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


                XSSFDrawing drawing = sheet.createDrawingPatriarch();
                XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 4, 2, 16, 20);

                XSSFChart chart = drawing.createChart(anchor);
                chart.setTitleText("Performance per Agent");
                chart.setTitleOverlay(false);

                XDDFChartLegend legend = chart.getOrAddLegend();
                legend.setPosition(LegendPosition.TOP_RIGHT);

                XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
                bottomAxis.setTitle("Agent");

                XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
                leftAxis.setTitle("Performance");
                leftAxis.setCrossBetween(AxisCrossBetween.BETWEEN);

                XDDFDataSource<String> agentIDs = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, rowNum - 1, 0, 0));
                XDDFNumericalDataSource<Double> performances = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, rowNum - 1, 1, 1));

                XDDFBarChartData data = (XDDFBarChartData) chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
                XDDFBarChartData.Series series = (XDDFBarChartData.Series) data.addSeries(agentIDs, performances);
                series.setTitle("Performance", null);
                data.setBarDirection(BarDirection.COL);

                chart.plot(data);


            }else{
                Row row = sheet.createRow(rowNum++);
                colNum = 0;
                row.createCell(colNum++).setCellValue("No data available");
            }


            return workbook;


        }catch (IOException e){
            return null;
        }

    }

    public XSSFWorkbook getRestOfData(XSSFWorkbook workbook,String instanceId, DownloadDTO downloadDTO) {
        List<String> routingProfiles = new ArrayList<>(Arrays.asList(downloadDTO.getRoutingProfiles()));

        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setStartDate(downloadDTO.getStartDate());
        dashboardDTO.setEndDate(downloadDTO.getEndDate());
        dashboardDTO.setRoutingProfiles(routingProfiles);

        Map<String, Object> combinedMetrics = new HashMap<>();

        MetricResponseDTO allData = getAllData( instanceId,dashboardDTO);

        combinedMetrics.put("avgHoldTime", allData.getAvgHoldTime());
        combinedMetrics.put("firstContactResolution", allData.getFirstContactResolution());
        combinedMetrics.put("abandonmentRate", allData.getAbandonmentRate());
        combinedMetrics.put("serviceLevel", allData.getServiceLevel());
        combinedMetrics.put("agentScheduleAdherence", allData.getAgentScheduleAdherence());
        combinedMetrics.put("avgSpeedOfAnswer", allData.getAvgSpeedOfAnswer());

        Mono<Map<String, Integer>> valuesMono = apiClient.getChannelResults(instanceId, dashboardDTO).map(metricService::extractValues);
        valuesMono.subscribe(combinedMetrics::putAll);

        CustomerSatisfactionDTO customerSatisfactionData = getCustomerSatisfactionData(dashboardDTO);
        combinedMetrics.put("customerSatisfaction", customerSatisfactionData.getSatisfaction_levels());


        JsonNode json = objectMapper.valueToTree(combinedMetrics);

        String sheetname = "General KPIs";
        XSSFSheet sheet = workbook.createSheet(sheetname);

        List<String> keys = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        List<Object> keys2 = new ArrayList<>();
        List<Object> values2 = new ArrayList<>();
        List<String> keys3Satisfaction = new ArrayList<>();
        List<Integer> valuesSatisfaction = new ArrayList<>();

        Iterator<Map.Entry<String, JsonNode>> fieldValues = json.fields();
        while (fieldValues.hasNext()) {
            Map.Entry<String, JsonNode> entry = fieldValues.next();
            if (!entry.getKey().equals("activities") && !entry.getKey().equals("voice") && !entry.getKey().equals("chat") && !entry.getKey().equals("customerSatisfaction")) {
                keys.add(entry.getKey());
                values.add(entry.getValue());
            } else if (entry.getKey().equals("voice") || entry.getKey().equals("chat")) {
                keys2.add(entry.getKey());
                values2.add(entry.getValue());

            } else if (entry.getKey().equals("customerSatisfaction")) {
                for (JsonNode val: entry.getValue()){
                    valuesSatisfaction.add(val.asInt());
                }
            }


        }

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("KPI");
        headerRow.createCell(1).setCellValue("Value");
        headerRow.createCell(2).setCellValue("Type of Interaction");
        headerRow.createCell(3).setCellValue("Total Interactions");
        headerRow.createCell(4).setCellValue("Satisfaction Level");
        headerRow.createCell(5).setCellValue("Value");


        int rowNum = 1;
        Row row = sheet.createRow(rowNum);
        for (int i = 0; i < keys.size(); i++) {
            row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(keys.get(i));
            row.createCell(1).setCellValue(values.get(i).toString());
            if(row.getCell(1).getStringCellValue().equals("null")){
                row.getCell(1).setCellValue("0.0");
            }
        }

        rowNum = 1;
        for (int i = 0; i < keys2.size(); i++) {
            Row row2 = sheet.getRow(rowNum++);
            row2.createCell(2).setCellValue(keys2.get(i).toString());
            row2.createCell(3).setCellValue(values2.get(i).toString());
        }

        keys3Satisfaction.add("Very Satisfied");
        keys3Satisfaction.add("Satisfied");
        keys3Satisfaction.add("Neutral");
        keys3Satisfaction.add("Unsatisfied");
        keys3Satisfaction.add("Very Unsatisfied");
        rowNum = 1;
        for (int i = 0; i < keys3Satisfaction.size(); i++) {
            Row row3 = sheet.getRow(rowNum++);
            row3.createCell(4).setCellValue(keys3Satisfaction.get(i));
            row3.createCell(5).setCellValue(valuesSatisfaction.get(i));
        }


        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);

        return workbook;

    }

    public XSSFWorkbook getActivities(XSSFWorkbook workbook, String instanceId, DownloadDTO downloadDTO){
        List<String> routingProfiles = new ArrayList<>(Arrays.asList(downloadDTO.getRoutingProfiles()));

        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setStartDate(downloadDTO.getStartDate());
        dashboardDTO.setEndDate(downloadDTO.getEndDate());
        dashboardDTO.setRoutingProfiles(routingProfiles);

        ActivityResponseDTO actData = getActivitiesData( instanceId,dashboardDTO);

        JsonNode json = objectMapper.valueToTree(actData);

        String sheetname = "Total Agent Activities";
        XSSFSheet sheet = workbook.createSheet(sheetname);

        List<String> keys = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        Iterator<Map.Entry<String, JsonNode>> fieldValues = json.fields();
        Map.Entry<String, JsonNode> firstEntry = fieldValues.next();


        if(!firstEntry.getValue().isEmpty()) {
            if(fieldValues.hasNext()) {
                while (fieldValues.hasNext()) {
                    Map.Entry<String, JsonNode> entry = fieldValues.next();

                    if (entry.getKey().equals("activities")) {
                        keys.add(entry.getKey());
                        values.add(entry.getValue());
                    }

                }
            }else {
                keys.add(firstEntry.getKey());
                values.add(firstEntry.getValue());
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

            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 4, 1, 16, 20);

            XSSFChart chart = drawing.createChart(anchor);
            chart.setTitleText("Total Agent Activities");
            chart.setTitleOverlay(false);

            XDDFChartLegend legend = chart.getOrAddLegend();
            legend.setPosition(LegendPosition.TOP_RIGHT);

            XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
            bottomAxis.setTitle("Date");

            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
            leftAxis.setTitle("Activity");
            leftAxis.setCrossBetween(AxisCrossBetween.BETWEEN);

            XDDFDataSource<String> Dates = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, rowNum - 1, 1, 1));
            XDDFNumericalDataSource<Double> Activities = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, rowNum - 1, 0, 0));

            XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
            XDDFLineChartData.Series series = (XDDFLineChartData.Series) data.addSeries(Dates, Activities);

            series.setTitle("Activities", null);

            chart.plot(data);
        }else {
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("No data available");
        }

        return workbook;

    }

    private List<AgentPerformanceDTO> getPerformance(PerformanceDTO performanceDTO) {
        return calculatePerformanceService.getPerformances(performanceDTO.getStartDate(), performanceDTO.getEndDate(), performanceDTO.getInstanceId(), performanceDTO.getRoutingProfileIds());
    }


    private MetricResponseDTO getAllData(String instanceId, DashboardDTO dashboardDTO){
        MetricResponseDTO metricsData = dashboardService.getMetricsData(instanceId,dashboardDTO);
        return metricsData;
    }

    private ActivityResponseDTO getActivitiesData(String instanceId, DashboardDTO dashboardDTO){
        ActivityResponseDTO actData = dashboardService.getActivity(instanceId, dashboardDTO);
        return actData;
    }

    private CustomerSatisfactionDTO getCustomerSatisfactionData(DashboardDTO dashboardDTO){
        CustomerSatisfactionDTO customerSatisfactionData = satisfactionService.getSatisfactionLevels();
        return customerSatisfactionData;
    }


}
