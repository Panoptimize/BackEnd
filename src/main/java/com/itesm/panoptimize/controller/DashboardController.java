package com.itesm.panoptimize.controller;


import com.itesm.panoptimize.dto.dashboard.DashMetricData;
import com.itesm.panoptimize.dto.dashboard.DashboardDTO;
import com.itesm.panoptimize.service.DashboardService;
import com.itesm.panoptimize.service.FCRService;

import com.itesm.panoptimize.dto.dashboard.CallMetricsDTO;
import com.itesm.panoptimize.dto.dashboard.DashboardDTO;
import com.itesm.panoptimize.service.CalculateSatisfactionService;

import com.itesm.panoptimize.dto.dashboard.MetricsDTO;
import com.itesm.panoptimize.service.DashboardService;

import com.itesm.panoptimize.dto.performance.PerformanceDTO;
import com.itesm.panoptimize.service.CalculatePerformanceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;

import java.text.ParseException;

import java.util.List;


@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private CalculateSatisfactionService satisfactionService;
    private final DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @Autowired
    private CalculatePerformanceService calculatePerformanceService;

    private static final String API_URL = "http://localhost:8000/get_metric_data"; //To test the consumption of AWS connect


    @Operation(summary = "Download the dashboard data", description = "Download the dashboard data by time frame, agent and workspace number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found the data",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DashboardDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Data not found",
                    content = @Content),
    })

    
    @PostMapping("/data/download")
    public ResponseEntity<Resource> downloadData(@RequestBody DashboardDTO dashboardDTO) throws IOException {
        Path pathFile = Paths.get("../utils/dummy.txt").toAbsolutePath().normalize();

        System.out.println("Path file: " + pathFile);

        Resource resource = new UrlResource(pathFile.toUri());

        if (resource.exists()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/dataFRC")
    public ResponseEntity<String> getFRC(@RequestBody DashMetricData requestDto) {
        //TemplateInstance
        RestTemplate restTemplate = new RestTemplate();
        //Make Post
        ResponseEntity<String> response = restTemplate.postForEntity(API_URL, requestDto,String.class);
        //Return
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    private FCRService fcrService;

    @GetMapping("/customer-satisfaction")
    public ResponseEntity<List<Integer>> calculateSatisfaction() {
        List<CallMetricsDTO> metrics = satisfactionService.getCallMetrics();
        return ResponseEntity.ok(satisfactionService.calculateSatisfaction(metrics));
    }

    @Autowired
    private DashboardService apiClient;

    @Autowired
    private DashboardService metricService;

    @GetMapping("/values")
    public Mono<List<Integer>> getValues() {
        return apiClient.getMetricResults()
                .map(metricService::extractValues);
    }


    @Operation(summary = "Get the dashboard data", description = "Get the dashboard data by time frame, agent and workspace number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found the data",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DashboardDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Data not found",
                    content = @Content),
    })
    @PostMapping("/data")
    public ResponseEntity<String> postData(@RequestBody DashboardDTO dashboardDTO) {
        return new ResponseEntity<>("Data received", HttpStatus.OK);
    }

    @Operation(summary = "Get the metrics data", description = "Get the metrics data by time frame, agent and workspace number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found the data",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MetricsDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Data not found",
                    content = @Content),
    })
    @PostMapping("/metrics")
    public ResponseEntity<Map<String, Double>> getMetrics(@Valid @RequestBody DashboardDTO dashboardDTO) {
        Map<String, Double> metricsData = dashboardService.getMetricsData(dashboardDTO);

        if(metricsData.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(metricsData, HttpStatus.OK);
    }

    @GetMapping("/metricFCR")
    public ResponseEntity<String> FCRService() throws JSONException {
        float firstResponseKPI = fcrService.fcrMetrics();

        JSONObject responseJSON = new JSONObject();
        responseJSON.put("FRC-KPI", firstResponseKPI);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(responseJSON.toString(), headers, HttpStatus.OK);
    }


    //Performance
    @Operation(summary = "Download the dashboard data", description = "Download the dashboard data by time frame, agent and workspace number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Calculated Performance data succesfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PerformanceDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Data not found or calculated incorrectly.",
                    content = @Content),
    })



    @PostMapping("/performance")
    public ResponseEntity<?> calculateAHT(@Valid @RequestBody PerformanceDTO performanceDTO) {
        Map<String, List<Double>> metricsData = calculatePerformanceService.getMetricsData(performanceDTO);

        if(metricsData.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(metricsData, HttpStatus.OK);
    }
}
