package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.dashboard.DashboardDTO;
import com.itesm.panoptimize.dto.performance.PerformanceDTO;
import com.itesm.panoptimize.service.CalculatePerformance;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
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
        List<Double> fcr = List.of(50.0,100.0,30.0,20.0);
        List<Double> sl = List.of(20.0,40.0,10.0,20.0);
        List<Double> ocup = List.of(40.0,40.0,10.0,20.0);

        performance = CalculatePerformance.performanceCalculation(sl,fcr,ocup);

        performanceData.setPerformanceData(performance);

        return  ResponseEntity.ok(performanceData);
    }



}
