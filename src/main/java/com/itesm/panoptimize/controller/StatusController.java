package com.itesm.panoptimize.controller;


import com.itesm.panoptimize.dto.agent.AgentResponseDTO;
import com.itesm.panoptimize.dto.agent.StatusDTO;
import com.itesm.panoptimize.service.StatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class    StatusController {

    private final StatusService statusService;

    @Autowired
    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @Operation(summary = "Get agents' status", description = "This endpoint returns a list of the agents' status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "List found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = StatusDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "List not found.",
                    content = @Content),
    })
    @GetMapping("/status")
    public List<StatusDTO> getStatus(@RequestParam String instanceId) {
        return statusService.getActiveAgents(instanceId);
    }
}
