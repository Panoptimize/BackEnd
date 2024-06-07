package com.itesm.panoptimize.controller;

import java.io.*;

import com.itesm.panoptimize.dto.download.DownloadDTO;
import com.itesm.panoptimize.service.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/download")
public class DownloadController {

    private final DownloadService downloadService;

    @Autowired
    public DownloadController(DownloadService downloadService) {
        this.downloadService = downloadService;
    }

    //Download data from the Dashboard
    @PostMapping("/getDownload")
    public ResponseEntity<InputStreamResource> getReport(@RequestBody DownloadDTO downloadDTO, @RequestParam String instanceID){
        try{
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            downloadService.getFinalReport(byteArrayOutputStream, instanceID, downloadDTO);

            byte[] byteArray = byteArrayOutputStream.toByteArray();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
            InputStreamResource resource = new InputStreamResource(byteArrayInputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=DataReport.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(byteArray.length)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }


}
