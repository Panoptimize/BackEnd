package com.itesm.panoptimize.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itesm.panoptimize.dto.dashboard.DashboardDataDTO;
import com.itesm.panoptimize.model.Contact;
import com.itesm.panoptimize.service.DownloadService;
import com.itesm.panoptimize.service.TotalContactsService;
import com.opencsv.CSVWriter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@RestController
@RequestMapping("/download")
public class DownloadController {
    @Autowired
    private TotalContactsService totalContactsService;

    private DownloadService downloadService;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public DownloadController(DownloadService downloadService, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.downloadService = downloadService;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    //Download data from the Dashboard (Only work with data from the database for now)
    @Operation(summary = "Download the dashboard data", description = "Download the dashboard data by time frame, agent and workspace number")
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

    @PostMapping("/downloadDataDB")
    public ResponseEntity<String> downloadDashboardData(@RequestBody DashboardDataDTO dashboardDataDTO){
        List<Contact> data = totalContactsService.getAllContacts();
        try{
            String filePath = "./BackEnd/contacts.csv";

            FileWriter fileWriter = new FileWriter(filePath);
            CSVWriter csvWriter = new CSVWriter(new PrintWriter(fileWriter));

            csvWriter.writeNext(new String[]{"id",
                    "startTime",
                    "endTime",
                    "firstContactResolution",
                    "resolutionStatus",
                    "sentimentNegative",
                    "sentimentPositive",
                    "agentId",
                    "satisfaction" });
            for (Contact contact : data) {
                csvWriter.writeNext(new String[]{String.valueOf(contact.getId()),
                        String.valueOf(contact.getStartTime()),
                        String.valueOf(contact.getEndTime()),
                        String.valueOf(contact.isFirstContactResolution()),
                        contact.getResolutionStatus(),
                        String.valueOf(contact.getSentimentNegative()),
                        String.valueOf(contact.getSentimentPositive()),
                        String.valueOf(contact.getAgentId()),
                        String.valueOf(contact.getSatisfaction())});            }
            csvWriter.close();
            fileWriter.close();


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            headers.setContentDispositionFormData("filename", "contacts.csv");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body("CSV file saved at: " + filePath);

        }catch (IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating CSV file: " + e.getMessage());
        }
    }

    @GetMapping("/getDBData")
    public ResponseEntity<List<Contact>> getData(){
        List<Contact> data = totalContactsService.getAllContacts();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/getData")
    public ResponseEntity<String> getJSONData(){
        String GET_DB_DATA_URL = "http://localhost:8080/download/getDBData";
        ResponseEntity<List<Contact>> response = restTemplate.exchange(
                GET_DB_DATA_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Contact>>() {}
        );
        List<Contact> data = response.getBody();

        try{
            String filePath = "D:\\Tec\\Semestre 2024-1\\Panoptimise\\BackEnd/contacts.csv";
            FileWriter csvWriter = new FileWriter(filePath);
            csvWriter.write("id,startTime,endTime,firstContactResolution,resolutionStatus,sentimentNegative,sentimentPositive,agentId,satisfaction\n");
            for (Contact contact : data) {
                csvWriter.write(contact.getId() + "," +
                        contact.getStartTime() + "," +
                        contact.getEndTime() + "," +
                        contact.isFirstContactResolution() + "," +
                        contact.getResolutionStatus() + "," +
                        contact.getSentimentNegative() + "," +
                        contact.getSentimentPositive() + "," +
                        contact.getAgentId() + "," +
                        contact.getSatisfaction() + "\n");
            }
            csvWriter.flush();
            csvWriter.close();

            return ResponseEntity.ok("CSV file saved at: " + filePath);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating JSON file: " + e.getMessage());
        }

    }
}
