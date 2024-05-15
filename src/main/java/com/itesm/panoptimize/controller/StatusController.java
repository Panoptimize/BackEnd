package com.itesm.panoptimize.controller;


import com.itesm.panoptimize.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StatusController {

    private final StatusService statusService;

    @Autowired
    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping("/status")
    public List<StatusService.AgentStatus> getStatus(@RequestParam String instanceId) {
        return statusService.getActiveAgents(instanceId);
    }
}
