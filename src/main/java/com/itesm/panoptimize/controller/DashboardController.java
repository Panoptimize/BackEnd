package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.connect.GetMetricResponseDTO;
import com.itesm.panoptimize.dto.dashboard.*;
import com.itesm.panoptimize.dto.note.NoteDTO;
import com.itesm.panoptimize.dto.performance.AgentPerformanceDTO;
import com.itesm.panoptimize.dto.performance.MapPerformanceDTO;
import com.itesm.panoptimize.model.Notification;
import com.itesm.panoptimize.service.DashboardService;
import com.itesm.panoptimize.service.CalculateSatisfactionService;
import com.itesm.panoptimize.service.CalculatePerformanceService;

import com.itesm.panoptimize.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Instant;
import java.util.*;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final CalculateSatisfactionService satisfactionService;
    private final DashboardService dashboardService;
    private final CalculatePerformanceService calculatePerformanceService;
    private final UserService userService;

    @Autowired
    public DashboardController(DashboardService dashboardService,
                               CalculateSatisfactionService satisfactionService,
                               CalculatePerformanceService calculatePerformanceService,
                               UserService userService) {
        this.dashboardService = dashboardService;
        this.satisfactionService = satisfactionService;
        this.calculatePerformanceService = calculatePerformanceService;
        this.userService = userService;
    }
    @Operation(summary = "View the customer satisfaction", description = "This endpoint returns the customer satisfaction levels.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Customer satisfaction levels found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerSatisfactionDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Customer satisfaction levels not found.",
                    content = @Content),
    })
    @GetMapping("/customer-satisfaction")
    public ResponseEntity<CustomerSatisfactionDTO> calculateSatisfaction() {
        CustomerSatisfactionDTO result = satisfactionService.getSatisfactionLevels();
        return ResponseEntity.ok(result);
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
    @PostMapping("/combined-metrics")
    public ResponseEntity<CombinedMetricsDTO> getCombinedMetrics(@Valid @RequestBody DashboardDTO dashboardDTO, Principal principal) {
        String firebaseId = principal.getName();
        String instanceId = userService.getInstanceIdFromFirebaseId(firebaseId);
        CombinedMetricsDTO combinedMetrics = dashboardService.getDashboardData(instanceId, dashboardDTO);
        return ResponseEntity.ok(combinedMetrics);
    }

    @Operation(summary = "Read Notifications", description = "This GET request call serves the purpose of returning all the notifications." )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Notes found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Notification.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Notes not found.",
                    content = @Content),
    })
    @GetMapping("/Notifications")
    public ResponseEntity<List<Notification>> getNotifications() {
        List<Notification> notifications = dashboardService.getNotifications().stream().toList();
        if (notifications.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(notifications);
    }

    @Operation(summary = "Read a Notification", description = "This GET request call serves the purpose of returning a Notification with an id." )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Notifications found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Notification.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Notifications not found.",
                    content = @Content),
    })
    @GetMapping("/Notifications/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Long id) {
        Notification notification = dashboardService.getNotificationById(id);
        return ResponseEntity.ok(notification);
    }
    @Operation(summary = "Create a new Notification",
            description = "This POST request call creates a new Notification.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Notification created successfully.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Notification.class))
                    }),
            @ApiResponse(responseCode = "400",
                    description = "Bad request.",
                    content = @Content)
    })
    @PostMapping("/Notifications")
    public ResponseEntity<Notification> addNotification(@RequestBody Notification notification) {
        Notification newNotification = dashboardService.addNotification(notification);
        return ResponseEntity.ok(newNotification);
    }

    @Operation(summary = "Delete a Notification by ID",
            description = "This DELETE request call deletes a Notification by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Notification deleted successfully."),
            @ApiResponse(responseCode = "404",
                    description = "Notification not found.",
                    content = @Content)
    })
    @DeleteMapping("/Notifications/{id}")
    public ResponseEntity<Boolean> deleteNotification(@PathVariable Long id) {
        return ResponseEntity.ok(
                dashboardService.deleteNotification(id)
        );
    }

    @Operation(summary = "Update an existing Notification",
            description = "This PUT request call updates an existing Notification.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Notification updated successfully.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Notification.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Note not found.",
                    content = @Content)
    })
    @PutMapping("/Notifications/{id}")
    public ResponseEntity<Notification> updateNotification(@PathVariable Long id, @RequestBody Notification notification) {
        return ResponseEntity.ok(dashboardService.updateNotification(id, notification));
    }

    @Operation(summary = "Get Filters ", description = "This GET request call serves the purpose of returning Filters by the instance id." )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Filters found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DashboardFiltersDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Filters not found.",
                    content = @Content),
    })
    @GetMapping("/filters")
    public ResponseEntity<DashboardFiltersDTO> getFilters(Principal principal) {
        String firebaseId = principal.getName();
        String instanceId = userService.getInstanceIdFromFirebaseId(firebaseId);
        DashboardFiltersDTO filters = dashboardService.getFilters(instanceId);
        return ResponseEntity.ok(filters);
    }

    @PostMapping("/test")
    public ResponseEntity<MapPerformanceDTO> test(@RequestBody @Valid DashboardDTO dashboardDTO, Principal principal) {
        String firebaseId = principal.getName();
        String instanceId = userService.getInstanceIdFromFirebaseId(firebaseId);
        return ResponseEntity.ok(dashboardService.test(dashboardDTO, instanceId));
    }
}
