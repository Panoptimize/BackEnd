package com.itesm.panoptimize.controller;


import com.itesm.panoptimize.dto.routing_profile.CreateRoutingProfileDTO;
import com.itesm.panoptimize.dto.routing_profile.RoutingProfileDTO;
import com.itesm.panoptimize.dto.routing_profile.UpdateRoutingProfileDTO;
import com.itesm.panoptimize.service.RoutingProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/routing-profile")
public class RoutingProfileController {

    private final RoutingProfileService routingProfileService;

    public RoutingProfileController(RoutingProfileService routingProfileService){
        this.routingProfileService = routingProfileService;
    }

    @Operation(summary = "Get routing profile by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Routing profile found"),
            @ApiResponse(responseCode = "404", description = "Routing profile not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RoutingProfileDTO> getRoutingProfile(@PathVariable String id){
        return ResponseEntity.ok(routingProfileService.getRoutingProfile(id));
    }

    @Operation(summary = "Get all routing profiles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Routing profiles found"),
            @ApiResponse(responseCode = "404", description = "Routing profiles not found")
    })
    @GetMapping("/")
    public ResponseEntity<Page<RoutingProfileDTO>> getRoutingProfiles(Pageable pageable){
        return ResponseEntity.ok(routingProfileService.getRoutingProfiles(pageable));
    }

    @Operation(summary = "Create routing profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Routing profile created"),
            @ApiResponse(responseCode = "404", description = "Routing profile not created")
    })
    @PostMapping("/{id}")
    public ResponseEntity<RoutingProfileDTO> createRoutingProfile(@RequestBody CreateRoutingProfileDTO createRoutingProfileDTO){
        return ResponseEntity.ok(routingProfileService.createRoutingProfile(createRoutingProfileDTO));
    }

    @Operation(summary = "Delete routing profile by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Routing profile deleted"),
            @ApiResponse(responseCode = "404", description = "Routing profile not deleted")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoutingProfile(@PathVariable String id){
        routingProfileService.deleteRoutingProfile(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update routing profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Routing profile updated"),
            @ApiResponse(responseCode = "404", description = "Routing profile not updated")
    })
    @PutMapping("/{id}")
    public ResponseEntity<RoutingProfileDTO> getRoutingProfile(@PathVariable String id, @RequestBody UpdateRoutingProfileDTO updateRoutingProfileDTO){
        return ResponseEntity.ok(routingProfileService.updateRoutingProfile(id, updateRoutingProfileDTO));
    }

}
