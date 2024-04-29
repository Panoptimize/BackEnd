package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.dashboard.DashboardDataDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.json.JSONException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import com.itesm.panoptimize.service.DashboardDataService;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/dashboard")
public class DashboardDataController {

    private DashboardDataService dashboardDataService = new DashboardDataService();
    @Autowired
    public DashboardDataController(DashboardDataService dashboardDataService) {
        this.dashboardDataService = dashboardDataService;
    }
    @Operation(summary = "Get the dashboard data", description = "Get the dashboard data by time frame, agent and workspace number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found the data",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DashboardDataDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Data not found",
                    content = @Content),
    })
    @PostMapping("/data")
    public ResponseEntity<String> postData(@RequestBody DashboardDataDTO dashboardDataDTO) {
        return new ResponseEntity<>("Data received", HttpStatus.OK);
    }
    @GetMapping("/metrics")
    public @ResponseBody Map<String, Object> getAllMetrics() throws JSONException {
        Map<String, Object> response = new HashMap<>();
        Double abandonRate = dashboardDataService.getAbandonRate();
        Double agentScheduleAdherence = dashboardDataService.getAgentScheduleAdherence();
        Double avgSumTime = dashboardDataService.getSumHandleTime();
        Double contactsHandled = dashboardDataService.getContactsHandled();
        Double asa = avgSumTime / contactsHandled; // Calculate ASA

        response.put("ABANDON_RATE", abandonRate);
        response.put("AGENT_SCHEDULE_ADHERENCE", agentScheduleAdherence);
        response.put("ASA", asa); // Add ASA to the response

        return response; // This will return JSON with ABANDON_RATE, AGENT_SCHEDULE_ADHERENCE, and ASA
    }



}
