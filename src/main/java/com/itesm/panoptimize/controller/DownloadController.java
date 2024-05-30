package com.itesm.panoptimize.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itesm.panoptimize.dto.dashboard.DashboardDTO;
import com.itesm.panoptimize.service.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/download")
public class DownloadController {

    private final DownloadService downloadService;

    private final ObjectMapper objectMapper;

    @Autowired
    public DownloadController(DownloadService downloadService, ObjectMapper objectMapper) {
        this.downloadService = downloadService;
        this.objectMapper = objectMapper;
    }

    //Download data from the Dashboard
    @GetMapping("/getDownload")
    public ResponseEntity<InputStreamResource> getReport() throws IOException {
        
        String homedir = System.getProperty("user.home");
        Date date = new Date();
        String file = "DataReport_" + date.getTime() + ".xlsx";
        String filePath = Paths.get(homedir, "Downloads", file).toString();

        downloadService.getFinalReport(filePath);

        File fileToDownload = new File(filePath);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(fileToDownload));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", fileToDownload.getName()));

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(fileToDownload.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }


}
