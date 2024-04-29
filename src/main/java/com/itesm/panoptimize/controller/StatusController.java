package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.agent.StatusDTO;
import com.itesm.panoptimize.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

    @Autowired
    private StatusService statusService;

    @GetMapping("/status")
    public ResponseEntity<StatusDTO> getStatus() {
        StatusDTO status = statusService.filterMetrics();
        return ResponseEntity.ok(status);
    }
}
