package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.supervisor.SupervisorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@RestController
public class SupervisorController {
    @Operation(
            summary = "Get all the supervisors",
            description = "Get the supervisor name and details without passwords or sensitive data"
    )
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",
                    description = "Supervisors found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SupervisorDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Supervisors not found",
                    content = @Content),
    })
    @GetMapping("/supervisors/all")
    public ResponseEntity<List<SupervisorDTO>> getController(){
        List<SupervisorDTO> posts = new ArrayList<>();
        SupervisorDTO supervisorDto= new SupervisorDTO ();
        supervisorDto.setName("Caligula de Argona");
        supervisorDto.setFloor("Sales_Floor");
        supervisorDto.setVerified(true);
        supervisorDto.setPicture("https://example.com/Caligula.jpg");
        SupervisorDTO superDto = new SupervisorDTO();
        superDto.setName("Steven Smith");
        superDto.setFloor("CostumerSupport_Floor");
        superDto.setVerified(true);
        superDto.setPicture("https://example.com/StevenSmith.jpg");


        posts.add(supervisorDto);
        posts.add(superDto);

        return ResponseEntity.ok(posts);
    }

    @Operation(summary = "Deleting a supervisor", description = "Director deletes a supervisor.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Supervisor deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Unable to delete",
                    content = @Content)
    })
    @DeleteMapping("/supervisor/{id}")
    public ResponseEntity<String> deleteSupervisorResponse(@PathVariable Long id){
        return ResponseEntity.ok("Supervisor " + id + " was deleted.");
    }


}
