package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.dashboard.DashMetricData;
import com.itesm.panoptimize.dto.dashboard.DashboardDataDTO;
import com.itesm.panoptimize.service.FRCService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@RestController
@RequestMapping("/dashboard")
public class DashboardDataController {
    private static final String API_URL = "http://localhost:8000/get_metric_data"; //To test the consumption of AWS connect
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

    @Operation(summary = "Get the connect metric data", description = "Get the metric data requiered to do the solved on first interactions, CallbackContacts, AbandonedContacts, HandledContacts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found the metrics",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DashMetricData.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Data not found",
                    content = @Content),
    })
    @PostMapping("/dataFRC")
    public ResponseEntity<String> getFRC(@RequestBody DashMetricData requestDto) {
        //TemplateInstance
        RestTemplate restTemplate = new RestTemplate();
        //Make Post
        ResponseEntity<String> response = restTemplate.postForEntity(API_URL, requestDto,String.class);
        //Return
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    private final FRCService frcService;

    @Autowired
    public DashboardDataController(FRCService frcService) {
        this.frcService = frcService;
    };

    @GetMapping("/metrics")
    public String FRCService() throws IOException {
        return frcService.requestJSONBuild().getBody();
    }
}
