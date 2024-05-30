package com.itesm.panoptimize.controller;

import java.nio.file.Paths;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itesm.panoptimize.service.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<String> getReport() {

        String homedir = System.getProperty("user.home");
        Date date = new Date();
        String file = "DataReport_" + date.getTime() + ".xlsx";
        String filePath = Paths.get(homedir, "Downloads", file).toString();

        downloadService.getFinalReport(filePath);
        return ResponseEntity.ok("Excel file saved at: " + filePath);
    }


}
