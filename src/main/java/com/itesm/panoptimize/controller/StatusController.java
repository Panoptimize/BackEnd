package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.agent.StatusDTO;
import com.itesm.panoptimize.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for handling requests related to agent status.
 * This class manages the web request to retrieve status metrics of agents
 * by utilizing the StatusService.
 */
@RestController
public class StatusController {

    @Autowired
    private StatusService statusService;

    /**
     * Endpoint to get the current status metrics of agents.
     * This method handles the GET request to "/status", invoking the StatusService
     * to retrieve and return the agent status metrics.
     *
     * @return ResponseEntity containing the StatusDTO, which encapsulates the agent status metrics.
     * The response is returned with an HTTP OK status.
     */

    @GetMapping("/status")
    public ResponseEntity<StatusDTO> getStatus() {
        StatusDTO status = statusService.filterMetrics();
        return ResponseEntity.ok(status);
    }
}
