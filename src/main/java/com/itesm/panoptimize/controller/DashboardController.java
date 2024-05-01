package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.dashboard.DashboardDTO;
import com.itesm.panoptimize.dto.dashboard.MetricsDTO;
import com.itesm.panoptimize.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
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
    

    @PostMapping("/metrics")
    public ResponseEntity<double[]> getMetrics() {
        double[] metricsData = dashboardService.getMetricsData();
        return ResponseEntity.ok(metricsData);
    }

}
