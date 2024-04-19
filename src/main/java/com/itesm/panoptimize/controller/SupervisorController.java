package com.itesm.panoptimize.controller;
import com.itesm.panoptimize.dto.supervisor.SupervisorDTO;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping ("/supervisors")
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

    @PatchMapping("/{id}")
    @Operation(summary = "Update a supervisor", description = "Updates the details of an existing supervisor by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Supervisor updated successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SupervisorDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Supervisor not found",
                    content = @Content)
    })
    public ResponseEntity<SupervisorDTO> updateSupervisor(@PathVariable int id, @RequestBody SupervisorDTO supervisorDetails) {
        SupervisorDTO initialSupervisor = new SupervisorDTO();
        createSupervisor(initialSupervisor);

        return ResponseEntity.ok(initialSupervisor);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupervisorDTO> getSupervisorById(@PathVariable int id) {
        List<SupervisorDTO> supervisors = new ArrayList<>();
        SupervisorDTO initialSupervisor = new SupervisorDTO();
        createSupervisor(initialSupervisor);
        List<String> supervisedAgents = new ArrayList<>();
        supervisedAgents.add("Agent1");
        supervisedAgents.add("Agent2");
        initialSupervisor.setSupervisedAgents(supervisedAgents);

        supervisors.add(initialSupervisor);
        //API to get a supervisor by ID
        for (SupervisorDTO supervisor : supervisors) {
            if (supervisor.getId() == id) {
                return ResponseEntity.ok(supervisor);
            }
        }
        return ResponseEntity.notFound().build();
    }

    // Temporal method, to not repeat code
    private void createSupervisor(SupervisorDTO initialSupervisor) {
        initialSupervisor.setId(1);
        initialSupervisor.setName("Supervisor Uno");
        initialSupervisor.setEmail("supervisor1@empresa.com");
        initialSupervisor.setUsername("supervisorUno");
        initialSupervisor.setPassword("123456");
        initialSupervisor.setFloor("Main Floor");
        initialSupervisor.setVerified(true);
        initialSupervisor.setPicture("https://example.com/supervisorUno.jpg");
    }
}