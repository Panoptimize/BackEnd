package com.itesm.panoptimize.controller;



import com.itesm.panoptimize.dto.dashboard.*;
import com.itesm.panoptimize.dto.performance.AgentPerformanceDTO;
import com.itesm.panoptimize.model.Notification;
import com.itesm.panoptimize.service.DashboardService;

import com.itesm.panoptimize.dto.dashboard.DashboardDTO;
import com.itesm.panoptimize.service.CalculateSatisfactionService;

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
import org.springframework.http.HttpHeaders;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final CalculateSatisfactionService satisfactionService;
    private final DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService, CalculateSatisfactionService satisfactionService) {
        this.dashboardService = dashboardService;
        this.satisfactionService = satisfactionService;
    }

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

    @GetMapping("/customer-satisfaction")
    public ResponseEntity<CustomerSatisfactionDTO> calculateSatisfaction() {
        CustomerSatisfactionDTO result = satisfactionService.getSatisfactionLevels();
        return ResponseEntity.ok(result);
    }

    @Autowired
    private DashboardService apiClient;

    @Autowired
    private DashboardService metricService;

    //Get the current number of agents in each channel from connect
    @PostMapping("/values")
    public Mono<Map<String, Integer>> getValues(@Valid @RequestBody DashboardDTO dashboardDTO) {
        return apiClient.getMetricResults(dashboardDTO)
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
    public ResponseEntity<MetricResponseDTO> getMetrics(@Valid @RequestBody DashboardDTO dashboardDTO) {
        MetricResponseDTO metricsData = dashboardService.getMetricsData(dashboardDTO);

        return ResponseEntity.ok(metricsData);
    }

    @GetMapping("/Notifications")
    public ResponseEntity<List<Notification>> getNotifications() {
        List<Notification> notifications = dashboardService.getNotifications().stream().toList();
        if (notifications.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/Notifications/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Long id) {
        Notification notification = dashboardService.getNotificationById(id);
        return ResponseEntity.ok(notification);
    }
    @PostMapping("/Notifications")
    public ResponseEntity<Notification> addNotification(@RequestBody Notification notification) {
        Notification newNotification = dashboardService.addNotification(notification);
        return ResponseEntity.ok(newNotification);
    }
    @DeleteMapping("/Notifications/{id}")
    public ResponseEntity<Boolean> deleteNotification(@PathVariable Long id) {
        return ResponseEntity.ok(
                dashboardService.deleteNotification(id)
        );
    }
    @PatchMapping("/Notifications/{id}")
    public ResponseEntity<Notification> updateNotification(@PathVariable Long id, @RequestBody Notification notification) {
        return ResponseEntity.ok(dashboardService.updateNotification(id, notification));
    }

    @GetMapping("/filters/{instanceId}")
    public ResponseEntity<DashboardFiltersDTO> getFilters(@PathVariable String instanceId) {
        DashboardFiltersDTO filters = dashboardService.getFilters(instanceId);

        System.out.println(instanceId);

        return ResponseEntity.ok(filters);
    }

    @Autowired
    private CalculatePerformanceService calculatePerformanceService;

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
    public ResponseEntity<List<AgentPerformanceDTO>> getPerformance(@RequestBody PerformanceDTO performanceDTO) {
        List<AgentPerformanceDTO> performanceData = calculatePerformanceService.getMetricsData(performanceDTO);
        return ResponseEntity.ok(performanceData);
    }

}
