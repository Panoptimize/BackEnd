package com.itesm.panoptimize.controller;


import com.itesm.panoptimize.dto.queue.QueueCreateDTO;
import com.itesm.panoptimize.dto.queue.QueueDTO;
import com.itesm.panoptimize.dto.queue.QueueUpdateDTO;
import com.itesm.panoptimize.dto.routing_profile.CreateRoutingProfileDTO;
import com.itesm.panoptimize.dto.routing_profile.RoutingProfileDTO;
import com.itesm.panoptimize.service.QueueService;
import com.itesm.panoptimize.service.RoutingProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/queue")
public class RoutingProfileController {

    private final RoutingProfileService routingProfileService;
    private final QueueService queueService;

    public RoutingProfileController(RoutingProfileService routingProfileService, QueueService queueService){
        this.routingProfileService = routingProfileService;
        this.queueService = queueService;
    }

    @Operation(summary = "Get routing profile by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Routing profile found"),
            @ApiResponse(responseCode = "404", description = "Routing profile not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RoutingProfileDTO> getQueue(@PathVariable String id){
        return ResponseEntity.ok(routingProfileService.getRoutingProfile(id));
    }

    @Operation(summary = "Get all routing profiles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Routing profiles found"),
            @ApiResponse(responseCode = "404", description = "Routing profiles not found")
    })
    @GetMapping("/")
    public ResponseEntity<Page<RoutingProfileDTO>> getQueues(Pageable pageable){
        return ResponseEntity.ok(routingProfileService.getRoutingProfiles(pageable));
    }

    @Operation(summary = "Create routing profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Queue created"),
            @ApiResponse(responseCode = "404", description = "Queue not created")
    })
    @PostMapping("/{id}")
    public ResponseEntity<QueueDTO> createQueue(@RequestBody QueueCreateDTO queueCreateDTO){
        return ResponseEntity.ok(queueService.createQueue(queueCreateDTO));
    }

    @Operation(summary = "Delete queue by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Queue deleted"),
            @ApiResponse(responseCode = "404", description = "Queue not deleted")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<QueueDTO> deteleteQueue(@PathVariable String id){
        queueService.deleteQueue(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update queue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Queue updated"),
            @ApiResponse(responseCode = "404", description = "Queue not updated")
    })
    @PutMapping("/{id}")
    public ResponseEntity<QueueDTO> getQueue(@PathVariable String id, @RequestBody QueueUpdateDTO queueUpdateDTO){
        return ResponseEntity.ok(queueService.updateQueue(id, queueUpdateDTO));
    }

}
