package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.dashboard.DashMetricData;
import com.itesm.panoptimize.dto.dashboard.DashboardDTO;
import com.itesm.panoptimize.service.DashboardService;
import com.itesm.panoptimize.service.FCRService;
import com.itesm.panoptimize.dto.performance.PerformanceDTO;
import com.itesm.panoptimize.service.CalculatePerformance;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
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

    private FCRService fcrService;

    @Autowired
    public void DashboardDataController(FCRService fcrService) {
        this.fcrService = fcrService;
    };

    @GetMapping("/metricFCR")
    public ResponseEntity<String> FCRService() {
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

    @GetMapping("/performance") //cambiar dependiendo al timeframe y los otros parametros (checar si los recibe el endpoint en si o el de dashboard)
    public ResponseEntity<PerformanceDTO> getPerformanceData (){
        PerformanceDTO performanceData = new PerformanceDTO();
        List<Double> performance;

        //TODO Creacion de endpoints para extraer esos datos
        List<Double> fcr = List.of(50.0,100.0,30.0,20.0); //se saca del endpoint
        List<Double> sl = List.of(20.0,40.0,10.0,20.0);
        List<Double> ocup = List.of(40.0,40.0,10.0,20.0);

        performance = CalculatePerformance.performanceCalculation(sl,fcr,ocup);

        performanceData.setPerformanceData(performance);

        return  ResponseEntity.ok(performanceData);
    }



}
