package com.itesm.panoptimize.controller;


import com.itesm.panoptimize.dto.connect.GetMetricResponseDTO;
import com.itesm.panoptimize.dto.dashboard.*;
import com.itesm.panoptimize.model.Notification;
import com.itesm.panoptimize.dto.dashboard.DashMetricData;
import com.itesm.panoptimize.dto.dashboard.DashboardDTO;
import com.itesm.panoptimize.dto.performance.AgentPerformanceDTO;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

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
import software.amazon.awssdk.thirdparty.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;

import java.util.List;


@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final CalculateSatisfactionService satisfactionService;
    private final DashboardService dashboardService;
    private final CalculatePerformanceService calculatePerformanceService;

    @Autowired
    public DashboardController(DashboardService dashboardService, CalculateSatisfactionService satisfactionService, CalculatePerformanceService calculatePerformanceService) {
        this.dashboardService = dashboardService;
        this.satisfactionService = satisfactionService;
        this.calculatePerformanceService = calculatePerformanceService;
    }


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

    @GetMapping("/customer-satisfaction")
    public ResponseEntity<List<Integer>> calculateSatisfaction() {
        List<CallMetricsDTO> metrics = satisfactionService.getCallMetrics();
        return ResponseEntity.ok(satisfactionService.calculateSatisfaction(metrics));
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

    @PostMapping("/combined-metrics")
    public ResponseEntity<Map<String, Object>> getCombinedMetrics(@Valid @RequestBody DashboardDTO dashboardDTO) {
        Map<String, Object> combinedMetrics = new HashMap<>();

        // Call the first service method
        Mono<Map<String, Integer>> valuesMono = apiClient.getMetricResults(dashboardDTO).map(metricService::extractValues);
        valuesMono.subscribe(values -> combinedMetrics.putAll(values));
        // Call the second service method
        MetricResponseDTO metricsData = dashboardService.getMetricsData(dashboardDTO);
        combinedMetrics.put("avgHoldTime", metricsData.getAvgHoldTime());
        combinedMetrics.put("firstContactResolution", metricsData.getFirstContactResolution());
        combinedMetrics.put("abandonmentRate", metricsData.getAbandonmentRate());
        combinedMetrics.put("serviceLevel", metricsData.getServiceLevel());
        combinedMetrics.put("agentScheduleAdherence", metricsData.getAgentScheduleAdherence());
        combinedMetrics.put("avgSpeedOfAnswer", metricsData.getAvgSpeedOfAnswer());

        // Call the activity service
        ActivityResponseDTO activityData = dashboardService.getActivity(dashboardDTO);
        combinedMetrics.put("activities", activityData.getActivities());

        return ResponseEntity.ok(combinedMetrics);
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
    public List<AgentPerformanceDTO> getMetricsData(@RequestBody PerformanceDTO performanceDTO) {
        return calculatePerformanceService.getMetricsData(performanceDTO);
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

    @PostMapping("/activity")
    public ResponseEntity<ActivityResponseDTO> getActivity(@RequestBody DashboardDTO dashboardDTO) {
        ActivityResponseDTO response = dashboardService.getActivity(dashboardDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filters/{instanceId}")
    public ResponseEntity<DashboardFiltersDTO> getFilters(@PathVariable String instanceId) {
        DashboardFiltersDTO filters = dashboardService.getFilters(instanceId);

        System.out.println(instanceId);

        return ResponseEntity.ok(filters);
    }


}
